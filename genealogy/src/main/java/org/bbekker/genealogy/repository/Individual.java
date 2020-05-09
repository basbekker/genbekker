package org.bbekker.genealogy.repository;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Individual {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "LAST_NAME", nullable = false, unique = false)
	private String lastName;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "FIRST_NAME", nullable = false, unique = false)
	private String firstName;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "MIDDLE_NAME", nullable = true, unique = false)
	private String middleName;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "MAIDEN_NAME", nullable = true, unique = false)
	private String maidenName;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "FAMILIAR_NAME", nullable = true, unique = false)
	private String familiarName;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@Column(name = "GENDER_ID", nullable = false, unique = false)
	@JoinColumn(name = "id", nullable = false)
	private Gender gender;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTH_DATE", nullable = true, unique = false)
	private Date birthDate;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Temporal(TemporalType.DATE)
	@Column(name = "DEATH_DATE", nullable = true, unique = false)
	private Date deathDate;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "BIRTH_PLACE", nullable = true, unique = false)
	private String birthPlace;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "DEATH_PLACE", nullable = true, unique = false)
	private String deathPlace;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "DEATH_CAUSE", nullable = true, unique = false)
	private String deathCause;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "NOTES", nullable = true, unique = false)
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
