package org.bbekker.genealogy.repository;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity(name = "Individual")
@Table(name = "INDIVIDUAL")
@Indexed
public class Individual implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "ID")
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "LAST_NAME", nullable = false, unique = false)
	@Field
	private String lastName;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "FIRST_NAME", nullable = false, unique = false)
	@Field
	private String firstName;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "MIDDLE_NAME", nullable = true, unique = false)
	@Field
	private String middleName;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "MAIDEN_NAME", nullable = true, unique = false)
	@Field
	private String maidenName;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "FAMILIAR_NAME", nullable = true, unique = false)
	@Field
	private String familiarName;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "GENDER_TYPE", nullable = false, unique = false)
	private String genderType;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "NOTE", nullable = true, unique = false)
	private String note;

	@OneToMany(mappedBy = "individual", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private List<Event> events;


	protected Individual() {
	}

	public Individual(String lastName, String firstName, String middleName, String maidenName, String familiarName,
			String genderType) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleName = middleName;
		this.maidenName = maidenName;
		this.familiarName = familiarName;
		this.genderType = genderType;
	}

	@Override
	public String toString() {
		return String.format(
				"Individual[id=%s lastName='%s' firstName='%s' middleName='%s' maidenName='%s' familiarName='%s' gender='%s' note='%s']",
				id, lastName, firstName, middleName, maidenName, familiarName, genderType, note);
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

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<Event> getEvents() {
		return events;
	}

}
