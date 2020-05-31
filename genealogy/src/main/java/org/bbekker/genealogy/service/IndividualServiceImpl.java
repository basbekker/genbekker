package org.bbekker.genealogy.service;

import java.util.ArrayList;
import java.util.List;

import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IndividualServiceImpl  implements IndividualService {

	private static final Logger logger = LoggerFactory.getLogger(IndividualServiceImpl.class);
	private static final int pageSize = 20;

	@Autowired
	MessageSource messageSource;

	@Autowired
	private IndividualRepository individualRepository;

	@Override
	public List<Individual> findAll() {

		List<Individual> allIndividuals = new ArrayList<Individual>();

		Iterable<Individual> foundIndividuals = individualRepository.findAll();
		for (Individual individual : foundIndividuals) {
			allIndividuals.add(individual);
		}

		logger.info("return allIndividuals");
		return allIndividuals;
	}


	@Override
	public List<Individual> findAllPaged(int currentPage) {

		List<Individual> allIndividuals = new ArrayList<Individual>();
		Pageable newPage = PageRequest.of(currentPage, pageSize);

		Page<Individual> foundIndividuals = individualRepository.findAll(newPage);
		for (Individual individual : foundIndividuals) {
			allIndividuals.add(individual);
		}

		logger.info("return pagedIndividuals");
		return allIndividuals;
	}

	@Override
	public List<Individual> findLikePaged(String lastName, int currentPage) {

		List<Individual> allIndividuals = new ArrayList<Individual>();
		Pageable newPage = PageRequest.of(currentPage, pageSize);

		Page<Individual> foundIndividuals = individualRepository.findByLastNameLike(lastName, newPage);
		for (Individual individual : foundIndividuals) {
			allIndividuals.add(individual);
		}

		logger.info("return pagedIndividuals");
		return allIndividuals;
	}

	@Override
	public void saveAll(List<Individual> individuals) {
		for (Individual individual : individuals) {
			individualRepository.save(individual);
		}
	}

	@Override
	public Integer getNumberOfRecords() {
		Long numberOfRecords = individualRepository.count();
		return numberOfRecords.intValue();
	}

	@Override
	public Integer getNumberOfPages() {
		Long numberOfpages = (individualRepository.count() / pageSize);
		return numberOfpages.intValue();
	}

	@Override
	public Integer getNumberOfLikePages(String lastName) {
		Long numberOfRecords = individualRepository.count(lastName);
		if (numberOfRecords == 0L) {
			return 0;
		} else {
			return roundUp(individualRepository.count(lastName), new Long(pageSize)).intValue();
		}
	}

	private Long roundUp(Long recordCount, Long pageSize) {
		// TODO still not rounding Ok
		return (long) Math.ceil(recordCount / pageSize);

	}

}
