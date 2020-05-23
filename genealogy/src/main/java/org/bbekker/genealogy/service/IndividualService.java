package org.bbekker.genealogy.service;

import java.util.List;

import org.bbekker.genealogy.repository.Individual;

public interface IndividualService {

	List<Individual> findAll();

	void saveAll(List<Individual> individuals);
}
