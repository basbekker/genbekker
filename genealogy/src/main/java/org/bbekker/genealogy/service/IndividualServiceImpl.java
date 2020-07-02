package org.bbekker.genealogy.service;

import java.util.List;
import java.util.Optional;

import org.bbekker.genealogy.repository.EventRepository;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.repository.RelationshipRepository;
import org.bbekker.genealogy.util.IndividualFullView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IndividualServiceImpl implements IndividualService {

	@Value("${spring.data.web.pageable.default-page-size}")
	private int defaultPageSize;

	@Autowired
	MessageSource messageSource;

	@Autowired
	private IndividualRepository individualRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private RelationshipRepository relationshipRepository;

	private PageHandlerUtil<Individual> pageHandler;


	@Override
	public PageHandlerUtil<Individual> findAll() {

		pageHandler = new PageHandlerUtil<Individual>(individualRepository.findAll());
		return pageHandler;
	}

	@Override
	public PageHandlerUtil<Individual> findAllPaged(int currentPage) {

		Pageable newPage = PageRequest.of(currentPage, defaultPageSize);
		pageHandler = new PageHandlerUtil<Individual>(individualRepository.findAll(newPage));
		return pageHandler;
	}

	@Override
	public PageHandlerUtil<Individual> findLikePaged(String lastName, int currentPage) {

		Pageable newPage = PageRequest.of(currentPage, defaultPageSize);
		pageHandler = new PageHandlerUtil<Individual>(individualRepository.findByLastNameLike(lastName, newPage));
		return pageHandler;
	}

	@Override
	public Individual save(Individual individual) {

		return individualRepository.save(individual);
	}

	@Override
	public void saveAll(List<Individual> individuals) {

		for (Individual individual : individuals) {
			individualRepository.save(individual);
		}
	}

	public Integer getPageSize() {
		return Integer.valueOf(defaultPageSize);
	}

	@Override
	public Integer getNumberOfElements() {
		Long elementCount = individualRepository.count();
		return elementCount.intValue();
	}

	@Override
	public Individual get(String id) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);
		if (optionalIndividual.isPresent()) {
			return optionalIndividual.get();
		}
		return null;
	}

	@Override
	public IndividualFullView getFullView(String id) {
		IndividualFullView fullIndividual = new IndividualFullView(individualRepository, eventRepository, relationshipRepository);
		fullIndividual.setIndividual(id);
		return fullIndividual;
	}

	@Override
	public IndividualFullView getFullView(Individual individual) {
		IndividualFullView fullIndividual = new IndividualFullView(individualRepository, eventRepository, relationshipRepository);
		fullIndividual.setIndividual(individual);
		return fullIndividual;
	}



}
