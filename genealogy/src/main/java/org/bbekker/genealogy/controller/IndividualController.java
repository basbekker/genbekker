package org.bbekker.genealogy.controller;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.service.IndividualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/individual")
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

		return "";
	}

	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ResponseEntity<Object> createIndividual(@RequestBody Individual individual) {
		Individual createdIndividual = individualRepository.save(individual);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(createdIndividual.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(path = "/display/{id}", method = RequestMethod.GET)
	public String displayIndividual(@PathVariable("id") String id, Model model) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		if (optionalIndividual.isPresent()) {
			Individual individual = optionalIndividual.get();
			model.addAttribute("individual", individual);
		}

		return "getIndividual";
	}

	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<Individual> getIndividual(@PathVariable("id") String id) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		return ResponseEntity.of(optionalIndividual);
	}

	@RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Individual> updateIndividual(@PathVariable("id") String id, @Valid Individual individual, BindingResult result, Model model) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		if (optionalIndividual.isPresent()) {
			individual.setId(id);
			individualRepository.save(individual);

			optionalIndividual = Optional.of(individual);
		}

		return ResponseEntity.of(optionalIndividual);
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteIndividual(@PathVariable("id") String id) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		if (!optionalIndividual.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		individualRepository.deleteById(id);

		return ResponseEntity.ok().build();
	}



	@RequestMapping(path = "/all", method = RequestMethod.GET)
	public String showAll(Model model) {
		model.addAttribute("individuals", individualService.findAll());

		return "allIndividuals";
	}

	@RequestMapping(path = "/paged", method = RequestMethod.GET)
	public String showPaged(@RequestParam(value = "page", required = false) String currentPageNumber, Model model) {

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

		model.addAttribute("numberOfPages", numberOfPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("firstPage", firstPage);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("nextPage", nextPage);
		model.addAttribute("lastPage", lastPage);
		model.addAttribute("individuals", individualService.findAllPaged(currentPage));

		return "pagedIndividuals";
	}

}