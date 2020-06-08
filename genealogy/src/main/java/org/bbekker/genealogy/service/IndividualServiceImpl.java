package org.bbekker.genealogy.service;

import java.util.Date;
import java.util.List;

import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
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

	private PageHandlerUtil<Individual> pageHandler;

	public void create(String lastName, String firstName, String middleName, String maidenName, String familiarName,
			String genderType, Date birthDate, String birthPlace, Date deathDate, String deathPlace, String note) {

		Individual individual = new Individual(lastName, firstName, middleName, maidenName, familiarName, genderType);

		Event event = new Event("birth", birthDate);
		event.setEventPlace(birthPlace);

	}

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
}
