package org.bbekker.genealogy.controller;

import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.bbekker.genealogy.common.AppConstants.GenderType;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.service.IndividualService;
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
	MessageSource messageSource;


	@RequestMapping(path = "/add", method = RequestMethod.POST)
	public String createIndividual(@Valid Individual individual, BindingResult result, Model model) {
		return "addIndividual";
	}

	@RequestMapping(path = "/display/{id}", method = RequestMethod.GET)
	public String displayIndividual(@PathVariable("id") String id, Model model, Locale locale) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		if (optionalIndividual.isPresent()) {
			Individual individual = optionalIndividual.get();
			model.addAttribute("individual", individual);
			model.addAttribute("genderName", setGenderText(individual.getGenderType(), locale));
		}

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
			Model model) {

		int currentPage = 0;
		if (currentPageNumber != null && !currentPageNumber.isEmpty()) {
			try {
				currentPage = Integer.valueOf(currentPageNumber);
			} catch (Exception e) {
				currentPage = 0;
			}
		}

		int numberOfPages = individualService.getNumberOfPages();
		int firstPage = 0;
		int prevPage = Integer.max(currentPage - 1, 0);
		int nextPage = Integer.min(currentPage + 1, numberOfPages);
		int lastPage = numberOfPages;

		logger.info(currentPage + " " + nameSearch);

		model.addAttribute("numberOfPages", numberOfPages);
		model.addAttribute("currentPage", currentPage + 1);
		model.addAttribute("firstPage", firstPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("lastPage", lastPage);

		if (nameSearch != null && !nameSearch.isEmpty()) {
			model.addAttribute("individuals", individualService.findLikePaged(nameSearch, currentPage));
		} else {
			model.addAttribute("individuals", individualService.findAllPaged(currentPage));
		}

		return "pagedIndividuals";
	}

	private String setGenderText(String genderType, Locale locale) {
		String genderString = messageSource.getMessage("individual.genderType.undefined", null, locale);
		if (genderType.equals(GenderType.MALE.getGenderId())) {
			genderString = messageSource.getMessage("individual.genderType.male", null, locale);
		} else {
			if (genderType.equals(GenderType.FEMALE.getGenderId())) {
				genderString = messageSource.getMessage("individual.genderType.female", null, locale);
			} else {
				if (genderType.equals(GenderType.INTERSEXUAL.getGenderId())) {
					genderString = messageSource.getMessage("individual.genderType.intersexual", null, locale);
				} else {
					if (genderType.equals(GenderType.UNDEFINED.getGenderId())) {
						genderString = messageSource.getMessage("individual.genderType.undefined", null, locale);
					}
				}
			}
		}
		return genderString;
	}

}
