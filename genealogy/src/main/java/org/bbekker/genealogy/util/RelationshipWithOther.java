package org.bbekker.genealogy.util;

import java.util.List;

import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.Family;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.Relationship;
import org.bbekker.genealogy.repository.RelationshipType;
import org.bbekker.genealogy.repository.RoleType;

public class RelationshipWithOther {

	private Individual individual;
	private RoleType roleType;

	private Individual otherIndividual;
	private RoleType otherRoleType;
	private RelationshipType relationshipType;
	private Family family;
	private String note;
	private List<Event> events;

	public RelationshipWithOther(Individual individual, Relationship relationship) {

		this.individual = individual;

		boolean found = false;
		this.roleType = null;
		this.otherIndividual = null;
		this.otherRoleType = null;
		this.family = null;
		this.relationshipType = null;
		this.note = null;
		this.events = null;

		if (relationship.getIndividual1().getId().equals(individual.getId())) {
			this.roleType = relationship.getRoleType1();

			this.otherIndividual = relationship.getIndividual2();
			this.otherRoleType = relationship.getRoleType2();

			found = true;
		} else {
			if (relationship.getIndividual2().getId().equals(individual.getId())) {
				this.roleType = relationship.getRoleType2();

				this.otherIndividual = relationship.getIndividual1();
				this.otherRoleType = relationship.getRoleType1();

				found = true;
			} else {
				// Passed in individual is not one of the individuals in the passed in relationship
				// Will return null values
			}
		}

		if (found) {
			this.relationshipType = relationship.getRelationshipType();
			this.note = relationship.getNote();
			this.events = relationship.getEvents();
		}
	}

	public Individual getIndividual() {
		return individual;
	}

	public Individual getOtherIndividual() {
		return otherIndividual;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public RoleType getOtherRoleType() {
		return otherRoleType;
	}

	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	public Family getFamily() {
		return family;
	}

	public String getNote() {
		return note;
	}

	public List<Event> getEvents() {
		return events;
	}

}
