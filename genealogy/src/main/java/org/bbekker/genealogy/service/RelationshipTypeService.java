package org.bbekker.genealogy.service;

import org.bbekker.genealogy.repository.RelationshipType;
import org.springframework.stereotype.Service;

@Service
public interface RelationshipTypeService {

	public RelationshipType getRelationshipTypeByQualifier(String qualifier);

}
