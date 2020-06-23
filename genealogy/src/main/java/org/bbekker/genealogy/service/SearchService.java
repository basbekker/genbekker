package org.bbekker.genealogy.service;

import java.util.List;

import org.bbekker.genealogy.repository.Individual;
import org.springframework.stereotype.Service;

@Service
public interface SearchService {

	List<Individual> SearchByTerm(String term);


}
