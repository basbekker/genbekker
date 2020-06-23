package org.bbekker.genealogy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseNameRepository extends CrudRepository<BaseName, String> {

}
