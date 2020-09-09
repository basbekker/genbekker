package org.bbekker.genealogy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends CrudRepository<Gender, String> {

	@Query("SELECT g.description FROM Gender g WHERE g.id = :id")
	public Optional<String> findDescriptionById(@Param("id") String id);

	@Query("SELECT g.description FROM Gender g WHERE g.qualifier = :qualifier")
	public Optional<String> findDescriptionByQualifier(@Param("qualifier") String qualifier);

	public Optional<Gender> findByQualifier(String qualifier);
}
