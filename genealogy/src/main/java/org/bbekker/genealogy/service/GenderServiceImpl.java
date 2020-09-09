package org.bbekker.genealogy.service;

import java.util.Optional;

import org.bbekker.genealogy.repository.Gender;
import org.bbekker.genealogy.repository.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class GenderServiceImpl implements GenderService {

	@Autowired
	private GenderRepository genderRepository;

	@Override
	@Cacheable(value = "gender")
	public Gender getGenderByQualifier(String qualifier) {
		final Optional<Gender> optGender = genderRepository.findByQualifier(qualifier);
		if (optGender.isPresent()) {
			return optGender.get();
		} else {
			return null;
		}
	}

	@Override
	public Iterable<Gender> getAllGenders() {
		return genderRepository.findAll();
	}

}
