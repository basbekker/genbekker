package org.bbekker.genealogy.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.service.IndividualService;
import org.bbekker.genealogy.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/individual")
public class IndividualRestController {

	@Autowired
	IndividualService individualService;

	@Autowired
	SearchService searchService;

	@Autowired
	IndividualRepository individualRepository;

	@Autowired
	MessageSource messageSource;


	@RequestMapping(path = "/create", method = RequestMethod.POST)
	public ResponseEntity<Object> createIndividual(
			@RequestBody Individual individual) {
		Individual createdIndividual = individualRepository.save(individual);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(createdIndividual.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Individual> updateIndividual(
			@PathVariable("id") String id,
			@Valid Individual individual,
			BindingResult result,
			Model model) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		if (optionalIndividual.isPresent()) {
			individual.setId(id);
			individualRepository.save(individual);

			optionalIndividual = Optional.of(individual);
		}

		return ResponseEntity.of(optionalIndividual);
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteIndividual(
			@PathVariable("id") String id) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		if (!optionalIndividual.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		individualRepository.deleteById(id);

		return ResponseEntity.ok().build();
	}

	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<Individual> getIndividual(
			@PathVariable("id") String id) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);

		return ResponseEntity.of(optionalIndividual);
	}

	@RequestMapping(path = "/search", method = RequestMethod.GET)
	public ResponseEntity<Object> searchIndividual(
			@RequestParam("search") String term) {

		List<Individual> individuals = searchService.SearchByTerm(term);

		return ResponseEntity.ok(individuals);
	}

}
