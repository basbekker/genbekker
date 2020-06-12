package org.bbekker.genealogy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {

	public List<Event> findByIndividual(Individual individual);
}
