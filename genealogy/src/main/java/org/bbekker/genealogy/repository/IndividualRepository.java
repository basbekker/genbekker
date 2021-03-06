package org.bbekker.genealogy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualRepository extends JpaRepository<Individual, String> {

	@Query("SELECT i FROM Individual i WHERE i.lastName LIKE %:lastName%")
	public Page<Individual> findByLastNameLike(@Param("lastName") String lastName, Pageable pageable);

	@Query("SELECT count(i) FROM Individual i WHERE i.lastName LIKE %:lastName%")
	public Long count(@Param("lastName") String lastName);

}
