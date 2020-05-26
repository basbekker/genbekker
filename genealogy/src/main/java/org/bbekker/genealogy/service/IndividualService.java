package org.bbekker.genealogy.service;

import java.util.List;

import org.bbekker.genealogy.repository.Individual;
import org.springframework.stereotype.Service;

@Service
public interface IndividualService {

	List<Individual> findAll();

	void saveAll(List<Individual> individuals);

	List<Individual> findAllPaged(int currentPage);

	Integer getNumberOfPages();

	Integer getNumberOfRecords();
}
