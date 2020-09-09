package org.bbekker.genealogy.service;

import java.util.List;

import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.util.IndividualFullView;
import org.springframework.stereotype.Service;

@Service
public interface IndividualService {

	String getSelfUri(String id);

	Individual get(String id);

	Individual save(Individual individual);

	void saveAll(List<Individual> individuals);

	Individual delete(String id);

	Individual delete(Individual individual);

	IndividualFullView getFullView(String id);

	IndividualFullView getFullView(Individual individual);


	PageHandlerUtil<Individual> findAll();

	PageHandlerUtil<Individual> findAllPaged(int currentPage);

	PageHandlerUtil<Individual> findLikePaged(String lastName, int currentPage);

	Integer getNumberOfElements();

	List<Individual> getKids(Individual individual);

	Individual getPartner(Individual individual);

}
