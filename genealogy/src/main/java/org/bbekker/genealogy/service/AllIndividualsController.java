package org.bbekker.genealogy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/individuals")
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

}
