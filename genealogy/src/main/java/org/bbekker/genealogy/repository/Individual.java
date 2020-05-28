package org.bbekker.genealogy.repository;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "MIDDLE_NAME", nullable = true, unique = false)
	private String middleName;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "MAIDEN_NAME", nullable = true, unique = false)
	private String maidenName;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "FAMILIAR_NAME", nullable = true, unique = false)
	private String familiarName;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "GENDER_TYPE", nullable = false, unique = false)
	private String genderType;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTH_DATE", nullable = true, unique = false)
	private Date birthDate;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Temporal(TemporalType.DATE)
	@Column(name = "DEATH_DATE", nullable = true, unique = false)
	private Date deathDate;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "BIRTH_PLACE", nullable = true, unique = false)
	private String birthPlace;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "DEATH_PLACE", nullable = true, unique = false)
	private String deathPlace;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "DEATH_CAUSE", nullable = true, unique = false)
	private String deathCause;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "NOTES", nullable = true, unique = false)
	private String notes;


	protected Individual() {
	}

	public Individual(String lastName, String firstName, String middleName, String maidenName, String familiarName, String genderType) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.maidenName = maidenName;
		this.familiarName = familiarName;
		this.genderType = genderType;
	}

	@Override
	public String toString() {
		return String.format("Individual[id=%s lastName='%s' firstName='%s' middleName='%s' maidenName='%s' familiarName='%s' gender='%s' birthDate='%s' birthPlace='%s' deathDate='%s' deathPlace='%s' deathCause='%s' notes='%s']", id, lastName, firstName, middleName, maidenName, familiarName, genderType, birthDate, birthPlace, deathDate, deathPlace, deathCause, notes);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setFamiliarName(String familiarName) {
		this.familiarName = familiarName;
	}

	public String getFamiliarName() {
		return familiarName;
	}

	public void setGender(String genderType) {
		this.genderType = genderType;
	}

	public String getGenderType() {
		return genderType;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setDeathPlace(String deathPlace) {
		this.deathPlace = deathPlace;
	}

	public String getDeathPlace() {
		return deathPlace;
	}

	public void setDeathCause(String deathCause) {
		this.deathCause = deathCause;
	}

	public String getDeathCause() {
		return deathCause;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

}
