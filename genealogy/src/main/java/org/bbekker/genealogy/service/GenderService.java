package org.bbekker.genealogy.service;

import org.bbekker.genealogy.repository.Gender;
import org.springframework.stereotype.Service;

@Service
public interface GenderService {

	public Gender getGenderByQualifier(String qualifier);

	public Iterable<Gender> getAllGenders();

}
