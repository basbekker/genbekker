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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "INDIVIDUAL_1_ID", nullable = false, unique = false)
	private Individual individual1;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "INDIVIDUAL_2_ID", nullable = false, unique = false)
	private Individual individual2;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_1_ID", nullable = false, unique = false)
	private Role role1;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_2_ID", nullable = false, unique = false)
	private Role role2;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "RELATIONSHIP_TYPE_ID", nullable = false, unique = false)
	private RelationshipType relationshipType;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "FAMILY_ID", nullable = true, unique = false)
	private Family family;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", nullable = true, unique = false)
	private Date startDate;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", nullable = true, unique = false)
	private Date endDate;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "PLACE", nullable = true, unique = false)
	private String place;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "NOTES", nullable = true, unique = false)
	private String notes;


	protected Relationship() {
	}

	public Relationship(Individual individual1, Individual individual2, Role role1, Role role2, RelationshipType relationshipType) {
		this.individual1 = individual1;
		this.individual2 = individual2;
		this.role1 = role1;
		this.role2 = role2;
		this.relationshipType = relationshipType;
	}

	@Override
	public String toString() {
		return String.format("Relationship[id=%s individual1='%s' individual2='%s' role1='%s' role2='%s' relationshipType='%s' family='%s' starthDate='%s' endDate='%s' place='%s' notes='%s']", id, individual1, individual2, role1, role2, relationshipType, family, startDate, endDate, place, notes);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setIndividual1(Individual individual1) {
		this.individual1 = individual1;
	}

	public Individual getIndividual1() {
		return individual1;
	}

	public void setIndividual2(Individual individual2) {
		this.individual2 = individual2;
	}

	public Individual getIndividual2() {
		return individual2;
	}

	public void setRole1(Role role1) {
		this.role1 = role1;
	}

	public Role getRole1() {
		return role1;
	}

	public void setRole2(Role role2) {
		this.role2 = role2;
	}

	public Role getRole2() {
		return role2;
	}

	public void setRelationshipType(RelationshipType relationshipType) {
		this.relationshipType = relationshipType;
	}

	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	public void setFamily(Family family) {
		this.family = family;
	}

	public Family getFamily() {
		return family;
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

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPlace() {
		return place;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

}
