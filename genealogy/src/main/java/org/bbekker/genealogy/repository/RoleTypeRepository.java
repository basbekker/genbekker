package org.bbekker.genealogy.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RoleTypeRepository extends CrudRepository<RoleType, String> {

	public Optional<RoleType> findByQualifier(String qualifier);
}
