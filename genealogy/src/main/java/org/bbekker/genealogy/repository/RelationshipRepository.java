package org.bbekker.genealogy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, String> {

	public List<Relationship> findByIndividual1(Individual individual);

	public List<Relationship> findByIndividual2(Individual individual);
}
