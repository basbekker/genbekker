package org.bbekker.genealogy.service;

import java.util.Date;
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

	void create(String lastName, String firstName, String middleName, String maidenName, String familiarName,
			String genderType, Date birthDate, String birthPlace, Date deathDate, String deathPlace, String note);

}
