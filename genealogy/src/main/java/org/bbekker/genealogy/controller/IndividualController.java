package org.bbekker.genealogy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.bbekker.genealogy.common.AppConstants.GenderTypes;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.EventRepository;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.repository.Relationship;
import org.bbekker.genealogy.repository.RelationshipRepository;
import org.bbekker.genealogy.service.IndividualService;
import org.bbekker.genealogy.service.PageHandlerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
	IndividualRepository individualRepository;

	@Autowired
	EventRepository eventRepository;

	@Autowired
	RelationshipRepository relationshipRepository;

	@Autowired
	MessageSource messageSource;


	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public String createIndividual(@Valid Individual individual, BindingResult result, Model model) {
		return "addIndividual";
	}

	@RequestMapping(path = "/display/{id}", method = RequestMethod.GET)
	public String displayIndividual(
			@PathVariable("id") String id, Model model,
			@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "searchArg", required = false) String searchArg,
			Locale locale) {

		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		if (optionalIndividual.isPresent()) {
			Individual individual = optionalIndividual.get();
			model.addAttribute("individual", individual);
			model.addAttribute("genderName", setGenderText(individual.getGenderType(), locale));

			List<Event> events = eventRepository.findByIndividual(individual);
			model.addAttribute("events", events);

			List<Relationship> relationships1 = relationshipRepository.findByIndividual1(individual);
			List<Relationship> relationships2 = relationshipRepository.findByIndividual2(individual);
			List<Relationship> outputList = new ArrayList<Relationship>(relationships1);
			outputList.addAll(relationships2);
			model.addAttribute("relationships", outputList);
		}
		model.addAttribute("page", page);
		model.addAttribute("searchArg", searchArg);

		return "getIndividual";
	}

	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public String showAll(Model model) {
		model.addAttribute("individuals", individualService.findAll());

		return "allIndividuals";
	}

	@RequestMapping(path = "/paged", method = RequestMethod.GET)
	public String showPaged(
			@RequestParam(value = "page", required = false) String currentPageNumber,
			@RequestParam(value = "nameSearch", required = false) String nameSearch,
			@RequestParam(value = "searchArg", required = false) String searchArg,
			@RequestParam(value = "action", required = false) String action,
			Model model) {

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
			if (action.equals("clear")) {
				currentPage = 1;
				searchString = SystemConstants.EMPTY_STRING;
				noSearch = true;
			} else {
				if (action.equals("search")) {
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
		model.addAttribute("searchArg", searchString);

		return "pagedIndividuals";
	}

	private String setGenderText(String genderType, Locale locale) {
		String genderString = messageSource.getMessage("individual.genderType.undefined", null, locale);
		if (genderType.equals(GenderTypes.MALE.getGenderQualifier())) {
			genderString = messageSource.getMessage("individual.genderType.male", null, locale);
		} else {
			if (genderType.equals(GenderTypes.FEMALE.getGenderQualifier())) {
				genderString = messageSource.getMessage("individual.genderType.female", null, locale);
			} else {
				if (genderType.equals(GenderTypes.INTERSEXUAL.getGenderQualifier())) {
					genderString = messageSource.getMessage("individual.genderType.intersexual", null, locale);
				} else {
					if (genderType.equals(GenderTypes.UNDEFINED.getGenderQualifier())) {
						genderString = messageSource.getMessage("individual.genderType.undefined", null, locale);
					}
				}
			}
		}
		return genderString;
	}

}
