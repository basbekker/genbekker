package org.bbekker.genealogy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.EventRepository;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.repository.Relationship;
import org.bbekker.genealogy.repository.RelationshipRepository;

public class IndividualFullView {

	private IndividualRepository individualRepository;
	private EventRepository eventRepository;
	private RelationshipRepository relationshipRepository;

	private Individual individual;
	private List<Event> events;
	private List<RelationshipWithOther> relationshipsWithOther;

	public IndividualFullView(IndividualRepository individualRepository, EventRepository eventRepository, RelationshipRepository relationshipRepository) {
		this.individualRepository = individualRepository;
		this.eventRepository = eventRepository;
		this.relationshipRepository = relationshipRepository;
	}

	public void setIndividual(Individual individual) {
		this.individual = individual;
		setEvents(individual);
		SetRelationships(individual);
	}

	public void setIndividual(String id) {
		Optional<Individual> optionalIndividual = individualRepository.findById(id);
		if (optionalIndividual.isPresent()) {
			this.individual = optionalIndividual.get();
			setEvents(individual);
			SetRelationships(individual);
		}
	}

	public Individual getIndividual() {
		return this.individual;
	}

	private void setEvents(Individual individual) {
		this.events = eventRepository.findByIndividual(individual);
	}

	private void SetRelationships(Individual individual) {
		relationshipsWithOther = new ArrayList<RelationshipWithOther>();
		List<Relationship> relationships1 = relationshipRepository.findByIndividual1(individual);
		List<Relationship> relationships2 = relationshipRepository.findByIndividual2(individual);
		for (Relationship relationship : relationships1) {
			RelationshipWithOther relationshipWithOther = new RelationshipWithOther(individual, relationship);
			this.relationshipsWithOther.add(relationshipWithOther);
			this.events.addAll(relationshipWithOther.getEvents());

		}
		for (Relationship relationship : relationships2) {
			RelationshipWithOther relationshipWithOther = new RelationshipWithOther(individual, relationship);
			this.relationshipsWithOther.add(relationshipWithOther);
			this.events.addAll(relationshipWithOther.getEvents());
		}
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public List<RelationshipWithOther> getRelationshipsWithOther() {
		return this.relationshipsWithOther;
	}

}
