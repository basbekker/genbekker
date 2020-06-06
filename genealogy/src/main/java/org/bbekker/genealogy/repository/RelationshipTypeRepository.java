package org.bbekker.genealogy.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RelationshipTypeRepository extends CrudRepository<RelationshipType, String> {

	public Optional<RelationshipType> findByQualifier(String qualifier);
}
