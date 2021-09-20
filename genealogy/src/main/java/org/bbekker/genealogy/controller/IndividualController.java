package org.bbekker.genealogy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.bbekker.genealogy.api.IndividualDTO;
import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.Gender;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.service.GenderService;
import org.bbekker.genealogy.service.IndividualService;
import org.bbekker.genealogy.service.PageHandlerUtil;
import org.bbekker.genealogy.service.SearchService;
import org.bbekker.genealogy.util.IndividualFullView;
import org.bbekker.genealogy.util.RelationshipWithOther;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/app/individual")
public class IndividualController {

	private static final Logger logger = LoggerFactory.getLogger(IndividualController.class);

	@Autowired
	IndividualService individualService;

	@Autowired
	SearchService searchService;

	@Autowired
	GenderService genderService;

	@Autowired
	MessageSource messageSource;


	@RequestMapping(path = "/create", method = RequestMethod.GET)
	public String createIndividual(
			Locale locale,
			Model model) {

		IndividualDTO individualForm = new IndividualDTO();

		Individual individual = new Individual(SystemConstants.EMPTY_STRING, SystemConstants.EMPTY_STRING);
		individual.setId(AppConstants.ID_PLACEHOLDER);

		// Create a new male individual
		individualForm.setIndividual(individual);
		individualForm.setGenderType(setGenderTypeText(individual.getGender(), locale));

		model.addAttribute("form", individualForm);
		model.addAttribute("allGenderTypes", getAllGenderTypes());

		return "editIndividualForm";
	}

	@RequestMapping(path = "/update/{id}", method = RequestMethod.GET)
	public String updateIndividual(
			@PathVariable("id") String id,
			Locale locale,
			Model model) {

		IndividualDTO individualForm = new IndividualDTO();

		Individual individual = individualService.get(id);
		if (individual != null) {
			individualForm.setIndividual(individual);
			individualForm.setGenderType(setGenderTypeText(individual.getGender(), locale));
		}

		model.addAttribute("form", individualForm);
		model.addAttribute("allGenderTypes", getAllGenderTypes());

		return "editIndividualForm";
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.GET)
	public String deleteIndividual(
			@PathVariable("id") String id,
			Model model) {

		Individual individual = individualService.get(id);
		if (individual != null) {
			individual = individualService.delete(individual);

			model.addAttribute("individual", individual);
			model.addAttribute("genderName", SystemConstants.EMPTY_STRING);
			model.addAttribute("events", new ArrayList<Event>());
			model.addAttribute("relationshipsWithOther", new ArrayList<RelationshipWithOther>());
		}

		// TODO set proper values;
		model.addAttribute("returnUri", "/app/individual/paged");
		model.addAttribute("page", "1");
		model.addAttribute("currentPage", "1");
		model.addAttribute("searchArg", SystemConstants.EMPTY_STRING);

		return "viewIndividual";
	}

	@RequestMapping(path = "/save/{id}", method = RequestMethod.POST)
	public String saveIndividual(
			@PathVariable("id") String id,
			@ModelAttribute(value = "form") IndividualDTO individualForm,
			@RequestParam(value = "action", required = false) String action,
			Locale locale,
			Model model) {

		boolean save = false;
		if (action != null && !action.isEmpty()) {
			if (action.equals("Save")) {
				save = true;
			} else {
				if (action.equals("Cancel")) {
					save = false;
				} else {
					// no-op
				}
			}
		} else {
			// no submit, just a link, stick with page and search
		}

		// Get the (maybe updated) individual from the form.
		Individual individual = individualForm.getIndividual();
		String genderTypeQualifier = setGenderTypeQualifier(individualForm.getGenderType(), locale);
		Gender gender = genderService.getByQualifier(genderTypeQualifier);
		if (gender != null) {
			individual.setGender(gender);
		}

		if (individual != null) {

			// If creating a individual, reset the ID from placeholder tu null, so the UUID gets generated.
			if (individual.getId().equals(AppConstants.ID_PLACEHOLDER)) {
				individual.setId(null);
			}

			if (save) {
				// save the updated individual
				individual = individualService.save(individual);
			} else {
				// Get the original one
				individual = individualService.get(individual.getId());
			}

			IndividualFullView fullIndividual = individualService.getFullView(individual);
			List<Event> events = fullIndividual.getEvents();
			List<RelationshipWithOther> relationshipsWithOther = fullIndividual.getRelationshipsWithOther();

			model.addAttribute("individual", individual);
			model.addAttribute("genderName", setGenderTypeText(individual.getGender(), locale));
			model.addAttribute("events", events);
			model.addAttribute("relationshipsWithOther", relationshipsWithOther);
		}

		// TODO set proper values;
		model.addAttribute("returnUri", "/app/individual/paged");
		model.addAttribute("page", "1");
		model.addAttribute("currentPage", "1");
		model.addAttribute("searchArg", "");

		return "viewIndividual";
	}

	@RequestMapping(path = "/view/{id}", method = RequestMethod.GET)
	public String displayIndividual(
			@PathVariable("id") String id,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "searchArg", required = false) String searchArg,
			@RequestParam(value = "returnUri", required = false) String returnUri,
			Locale locale,
			Model model) {

		Individual individual = individualService.get(id);
		if (individual != null) {

			IndividualFullView fullIndividual = individualService.getFullView(individual);
			List<Event> events = fullIndividual.getEvents();
			List<RelationshipWithOther> relationshipsWithOther = fullIndividual.getRelationshipsWithOther();

			model.addAttribute("individual", individual);
			model.addAttribute("genderName", setGenderTypeText(individual.getGender(), locale));
			model.addAttribute("events", events);
			model.addAttribute("relationshipsWithOther", relationshipsWithOther);
		}

		model.addAttribute("returnUri", returnUri);
		model.addAttribute("page", page);
		model.addAttribute("currentPage", page);
		model.addAttribute("searchArg", searchArg);

		return "viewIndividual";
	}

	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public String showAll(
			Locale locale,
			Model model) {

		model.addAttribute("individuals", individualService.findAll());

		return "allIndividuals";
	}

	@RequestMapping(path = "/search", method = RequestMethod.GET)
	public String searchIndividuals(
			@RequestParam(value = "nameSearch", required = false) String nameSearch,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "searchArg", required = false) String searchArg,
			@RequestParam(value = "action", required = false) String action,
			HttpServletRequest request,
			Locale locale,
			Model model) {

		String requestUri = request.getRequestURI();

		String searchString = null;
		if (action != null && !action.isEmpty()) {
			if (action.equals("Search")) {
				searchString = nameSearch;
			}
		}

		List<Individual> individuals = new ArrayList<Individual>();
		if (searchString != null && !searchString.isEmpty()) {
			individuals = searchService.SearchByTerm(searchString);
			searchArg = searchString;
		}
		model.addAttribute("individuals", individuals);
		model.addAttribute("searchArg", searchString);
		model.addAttribute("page", page);
		model.addAttribute("currentPage", page);
		model.addAttribute("returnUri", requestUri);

		return "searchIndividuals";
	}

	@RequestMapping(path = "/paged", method = RequestMethod.GET)
	public String showPaged(
			@RequestParam(value = "page", required = false) String currentPageNumber,
			@RequestParam(value = "nameSearch", required = false) String nameSearch,
			@RequestParam(value = "searchArg", required = false) String searchArg,
			@RequestParam(value = "action", required = false) String action,
			HttpServletRequest request,
			Locale locale,
			Model model) {

		String requestUri = request.getRequestURI();

		int currentPage = 1;
		if (currentPageNumber != null && !currentPageNumber.isEmpty()) {
			try {
				currentPage = Integer.valueOf(currentPageNumber);
			} catch (Exception e) {
				currentPage = 1;
			}
		}

		boolean noSearch = false;
		String searchString = null;
		if (searchArg != null && !searchArg.isEmpty()) {
			searchString = searchArg;
		} else {
			searchString = SystemConstants.EMPTY_STRING;
			noSearch = true;
		}


		if (action != null && !action.isEmpty()) {
			if (action.equals("Reset")) {
				currentPage = 1;
				searchString = SystemConstants.EMPTY_STRING;
				noSearch = true;
			} else {
				if (action.equals("Filter")) {
					currentPage = 1;
					searchString = nameSearch;
				} else {
					// no-op
				}
			}
		} else {
			// no submit, just a link, stick with page and search
		}

		logger.info(currentPage + " " + searchString);

		int numberOfPages = 0;
		PageHandlerUtil<Individual> pageHandler = null;
		if (noSearch) {
			// Our first page is 1, but the paging service is zero based.
			pageHandler = individualService.findAllPaged(currentPage - 1);
		} else {
			pageHandler = individualService.findLikePaged(searchString, currentPage - 1);
		}
		model.addAttribute("individuals", pageHandler.get());
		numberOfPages = pageHandler.getTotalPages();
		logger.info("no search # pages: " + numberOfPages);

		int firstPage = 1;
		int prevPage = Integer.max(currentPage - 1, 1);
		int nextPage = Integer.min(currentPage + 1, numberOfPages);
		int lastPage = numberOfPages;

		model.addAttribute("numberOfPages", numberOfPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("firstPage", firstPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("returnUri", requestUri);
		model.addAttribute("searchArg", searchString);

		return "pagedIndividuals";
	}

	private String setGenderTypeText(Gender gender, Locale locale) {
		String genderString = messageSource.getMessage("individual.genderType.undefined", null, locale);
		if (gender.getQualifier().equals("MALE")) {
			genderString = messageSource.getMessage("individual.genderType.male", null, locale);
		} else {
			if (gender.getQualifier().equals("FEMALE")) {
				genderString = messageSource.getMessage("individual.genderType.female", null, locale);
			} else {
				if (gender.getQualifier().equals("INTERSEXUAL")) {
					genderString = messageSource.getMessage("individual.genderType.intersexual", null, locale);
				} else {
					if (gender.getQualifier().equals("UNDEFINED")) {
						genderString = messageSource.getMessage("individual.genderType.undefined", null, locale);
					} else {
						genderString = messageSource.getMessage("individual.genderType.other", null, locale);
					}
				}
			}
		}
		return genderString;
	}

	private String setGenderTypeQualifier(String genderType, Locale locale) {
		// TODO Search returned values from descriptions & locale combination.
		String genderQualifier = "MALE";
		return genderQualifier;
	}

	private List<String> getAllGenderTypes() {
		List<String> allGenderTypes = new ArrayList<String>();
		Iterable<Gender> genders = genderService.getAllGenders();
		for (Gender gender : genders) {
			allGenderTypes.add(gender.getDescription());
		}
		return allGenderTypes;
	}

}
