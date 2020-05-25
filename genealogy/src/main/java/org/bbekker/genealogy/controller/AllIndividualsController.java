package org.bbekker.genealogy.controller;

import org.bbekker.genealogy.service.IndividualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/individual")
public class AllIndividualsController {

	private static final Logger logger = LoggerFactory.getLogger(AllIndividualsController.class);

	@Autowired
	IndividualService individualService;

	@Autowired
	MessageSource messageSource;

	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public String showAll(Model model) {
		model.addAttribute("individuals", individualService.findAll());

		return "allIndividuals";
	}

	@RequestMapping(path = "/paged", method = RequestMethod.GET)
	public String showPaged(@RequestParam(value = "page", required = false) String currentPageNumber, Model model) {

		Long currentPage = 0L;
		if (currentPageNumber != null && !currentPageNumber.isEmpty()) {
			try {
				currentPage = Long.parseLong(currentPageNumber);
			} catch (Exception e) {
				currentPage = 0L;
			}
		}
		long prevPage = Long.max(currentPage - 1L, 0);
		long nextPage = Long.min(currentPage + 1L, individualService.getNumberOfPages());

		model.addAttribute("numberOfPages", individualService.getNumberOfPages());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("individuals", individualService.findAllPaged(currentPage.intValue()));

		return "pagedIndividuals";
	}

}