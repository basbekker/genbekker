package org.bbekker.genealogy.repository;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "FAMILY")
@Indexed
public class Family {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", nullable = true, unique = false)
	private Date startDate;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "FAMILY_NAME", nullable = false, unique = false)
	private String familyName;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "DESCRIPTION", nullable = false, unique = false)
	private String description;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", nullable = true, unique = false)
	private Date endDate;

	@Basic(optional = true, fetch = FetchType.LAZY)
	@Column(name = "NOTES", nullable = true, unique = false)
	private String notes;

	protected Family() {
	}

	public Family(String familyName, String description) {
		this.familyName = familyName;
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Family[id=%s familyName='%s' description='%s' starthDate='%s' endDate='%s' notes='%s']", id, familyName, description, startDate, endDate, notes);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setdescription(String description) {
		this.description = description;
	}

	public String getdescription() {
		return description;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

}
