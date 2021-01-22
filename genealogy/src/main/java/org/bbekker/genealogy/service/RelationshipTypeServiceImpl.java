package org.bbekker.genealogy.service;

import java.util.Optional;

import org.bbekker.genealogy.repository.RelationshipType;
import org.bbekker.genealogy.repository.RelationshipTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationshipTypeServiceImpl implements RelationshipTypeService {

	@Autowired
	private RelationshipTypeRepository relationshipTypeRepository;

	@Override
	public RelationshipType getRelationshipTypeByQualifier(String qualifier) {
		final Optional<RelationshipType> optRelationshipType = relationshipTypeRepository.findByQualifier(qualifier);
		if (optRelationshipType.isPresent()) {
			return optRelationshipType.get();
		} else {
			return null;
		}
	}

}
