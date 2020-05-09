package org.bbekker.genealogy.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Individual {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@NotNull
	private String lastName;

	@NotNull
	private String firstName;

	private String middleName;

	private String maidenName;

	private String familiarName;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id", nullable = false)
	private Gender gender;

	private Date birthDate;

	private Date deathDate;

	private String birthPlace;

	private String deathPlace;

	private String deathCause;

	private String notes;


	protected Individual() {
	}

	public Individual(String lastName, String firstName, String middleName, String maidenName, String familiarName, Gender gender) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
	}

	@Override
	public String toString() {
		return String.format("Individual[id=%s lastName='%s' firstName='%s' gender='%s']", id, lastName, firstName, gender.getId());
	}

	public String getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getfirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public String getFamiliarName() {
		return familiarName;
	}

	public Gender getGender() {
		return gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public String getDeathPlace() {
		return deathPlace;
	}

	public String getDeathCause() {
		return deathCause;
	}

	public String getNotes() {
		return notes;
	}

}
