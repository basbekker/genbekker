package org.bbekker.genealogy.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, String> {

	public Optional<Role> findByQualifier(String qualifier);
}
