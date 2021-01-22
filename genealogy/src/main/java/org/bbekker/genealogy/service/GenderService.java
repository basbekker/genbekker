package org.bbekker.genealogy.service;

import org.bbekker.genealogy.repository.Gender;
import org.springframework.stereotype.Service;

@Service
public interface GenderService {

	public Gender getByQualifier(String qualifier);

	public Iterable<Gender> getAllGenders();

	public Gender getMale();

	public Gender getFemale();

	public Gender getIntersexual();

	public Gender getUndefined();

	public Gender getOther();
}
