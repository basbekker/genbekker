package org.bbekker.genealogy.service;

import java.util.ArrayList;
import java.util.List;

import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class IndividualServiceImpl  implements IndividualService {

	private static final Logger logger = LoggerFactory.getLogger(IndividualServiceImpl.class);

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
	public void saveAll(List<Individual> individuals) {
		for (Individual individual : individuals) {
			individualRepository.save(individual);
		}
	}

}
