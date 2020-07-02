package org.bbekker.genealogy.dto;

import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.Relationship;

public class IndividualEditDTO {

	private Individual individual;

	private String genderType;

	private Event birth;

	private Event death;

	private Relationship partner;


	public IndividualEditDTO() {
		individual = null;
		birth = null;
		death = null;
	}

	public IndividualEditDTO(Individual individual) {
		this.individual = individual;
	}

	public IndividualEditDTO(Individual individual, String genderType) {
		this.individual = individual;
		this.genderType = genderType;
	}

	public IndividualEditDTO(Individual individual, String genderType, Event birth) {
		this.individual = individual;
		this.genderType = genderType;
		this.birth = birth;
	}

	public IndividualEditDTO(Individual individual, String genderType, Event birth, Event death) {
		this.individual = individual;
		this.genderType = genderType;
		this.birth = birth;
		this.death = death;
	}

	public IndividualEditDTO(Individual individual, String genderType, Event birth, Event death, Relationship partner) {
		this.individual = individual;
		this.genderType = genderType;
		this.birth = birth;
		this.death = death;
		this.partner = partner;
	}

	public Individual getIndividual() {
		return individual;
	}

	public void setIndividual(Individual individual) {
		this.individual = individual;
	}

	public String getGenderType() {
		return genderType;
	}

	public void setGenderType(String genderType) {
		this.genderType = genderType;
	}

	public Event getBirth() {
		return birth;
	}

	public void setBirth(Event birth) {
		this.birth = birth;
	}

	public Event getDeath() {
		return death;
	}

	public void setDeath(Event death) {
		this.death = death;
	}

	public Relationship getPartner() {
		return partner;
	}

	public void setPartner(Relationship partner) {
		this.partner = partner;
	}

}
