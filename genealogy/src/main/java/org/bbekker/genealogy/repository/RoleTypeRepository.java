package org.bbekker.genealogy.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleTypeRepository extends CrudRepository<RoleType, String> {

	Optional<RoleType> findByQualifier(String qualifier);
}
