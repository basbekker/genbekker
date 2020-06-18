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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "INDIVIDUAL_1_ID", nullable = false, unique = false)
	private Individual individual1;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "INDIVIDUAL_2_ID", nullable = false, unique = false)
	private Individual individual2;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_TYPE_QUALIFIER_1", referencedColumnName = "QUALIFIER", nullable = false, unique = false)
	private RoleType roleType1;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_TYPE_QUALIFIER_2", referencedColumnName = "QUALIFIER", nullable = false, unique = false)
	private RoleType roleType2;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "RELATIONSHIP_TYPE_QUALIFIER", referencedColumnName = "QUALIFIER", nullable = false, unique = false)
	private RelationshipType relationshipType;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "FAMILY_ID", nullable = true, unique = false)
	private Family family;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "NOTE", nullable = true, unique = false)
	private String note;

	@OneToMany(mappedBy = "individual", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Event> events;


	protected Relationship() {
	}

	public Relationship(Individual individual1, Individual individual2, RoleType roleType1, RoleType roleType2, RelationshipType relationshipType) {
		this.individual1 = individual1;
		this.individual2 = individual2;
		this.roleType1 = roleType1;
		this.roleType2 = roleType2;
		this.relationshipType = relationshipType;
	}

	@Override
	public String toString() {
		return String.format("Relationship[id=%s individual1='%s' individual2='%s' roleType1='%s' roleType2='%s' relationshipType='%s' family='%s' note='%s']", id, individual1, individual2, roleType1, roleType2, relationshipType, family, note);
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

	public void setRole1(RoleType roleType1) {
		this.roleType1 = roleType1;
	}

	public RoleType getRoleType1() {
		return roleType1;
	}

	public void setRole2(RoleType roleType2) {
		this.roleType2 = roleType2;
	}

	public RoleType getRoleType2() {
		return roleType2;
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
