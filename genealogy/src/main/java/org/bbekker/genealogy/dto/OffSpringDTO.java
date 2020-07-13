package org.bbekker.genealogy.dto;

import java.util.Date;

import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.Individual;

public class OffSpringDTO {

	private String level;
	private String name;
	private String self_ref;
	private Date birthDate;
	private Date deathDate;
	private String partnerName;

	public OffSpringDTO() { }

	public OffSpringDTO(String level, String name, String self_ref, Date birthDate, Date deathDate, String partnerName) {
		this.level = level;
		this.name = name;
		this.self_ref = self_ref;
		this.birthDate = birthDate;
		this.deathDate = deathDate;
		this.partnerName = partnerName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelf_ref() {
		return self_ref;
	}

	public void setSelf_ref(String self_ref) {
		this.self_ref = self_ref;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public void setPartnerName(Individual partner) {
		if (partner != null) {
			this.partnerName = buildName(partner);
		} else {
			this.partnerName = SystemConstants.EMPTY_STRING;
		}
	}

	public void buildFromIndividual(Individual individual, String level) {
		this.level = level;
		this.name = buildName(individual);
		this.self_ref = "http://URI/" + individual.getId(); // build self URI inidividual
		// TODO get event birth
		// TODO get event death
	}

	private String buildName(Individual individual) {
		return individual.getFamiliarName() + SystemConstants.SPACE + ((individual.getMiddleName() != null && !individual.getMiddleName().isEmpty()) ? individual.getMiddleName() + SystemConstants.SPACE : SystemConstants.EMPTY_STRING ) + individual.getLastName();
	}
}
