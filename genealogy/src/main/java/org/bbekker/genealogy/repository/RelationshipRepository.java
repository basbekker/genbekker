package org.bbekker.genealogy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, String> {

	public List<Relationship> findByIndividual1(Individual individual);

	public List<Relationship> findByIndividual2(Individual individual);
}
