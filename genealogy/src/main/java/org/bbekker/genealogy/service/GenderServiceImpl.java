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

	/**
	 * The gender type defines the biological sexuality of an individual.
	 * Used field:
	 * - the character qualifier used by the application
	 */
	private enum GenderTypes {
		MALE ("MALE"), // For male sexuality.
		FEMALE ("FEMALE"), // For female sexuality.
		INTERSEXUAL ("INTERSEXUAL"), // For hermaphrodites, androgynous, transgendered sexualities (and probably others).
		UNDEFINED ("UNDEFINED"), // For use when sexuality is (still) unknown.
		OTHER ("OTHER"); // For if it somehow doesn't fit in the previous ones.

		private final String qualifier;

		GenderTypes(String qualifier) {
			this.qualifier = qualifier;
		}

		private String getName() {
			return this.getName();
		}

		private String getQualifier() {
			return qualifier;
		}
	}

	@Override
	@Cacheable(value = "Gender")
	public Gender getByQualifier(String qualifier) {
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

	@Override
	public Gender getMale() {
		return getByQualifier(GenderTypes.MALE.getQualifier());
	}

	@Override
	public Gender getFemale() {
		return getByQualifier(GenderTypes.FEMALE.getQualifier());
	}

	@Override
	public Gender getIntersexual() {
		return getByQualifier(GenderTypes.INTERSEXUAL.getQualifier());
	}

	@Override
	public Gender getUndefined() {
		return getByQualifier(GenderTypes.UNDEFINED.getQualifier());
	}

	@Override
	public Gender getOther() {
		return getByQualifier(GenderTypes.OTHER.getQualifier());
	}

}
