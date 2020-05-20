package org.bbekker.genealogy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GenderRepository extends CrudRepository<Gender, String> {

	@Query("SELECT g.name FROM Gender g WHERE g.id = :id")
	Optional<String> findNameById(@Param("id") String id);

	@Query("SELECT g FROM Gender g WHERE g.gender = :gender AND g.languageCode = :languageCode")
	List<Gender> findByTypeAndLanguage(@Param("gender") String gender, @Param("languageCode") String languageCode);

}
