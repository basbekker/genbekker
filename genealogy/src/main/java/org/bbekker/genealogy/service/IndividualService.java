package org.bbekker.genealogy.service;

import java.util.List;

import org.bbekker.genealogy.repository.Individual;
import org.springframework.stereotype.Service;

@Service
public interface IndividualService {

	void saveAll(List<Individual> individuals);

	PageHandlerUtil<Individual> findAll();

	PageHandlerUtil<Individual> findAllPaged(int currentPage);

	PageHandlerUtil<Individual> findLikePaged(String lastName, int currentPage);

	Integer getNumberOfElements();

}
