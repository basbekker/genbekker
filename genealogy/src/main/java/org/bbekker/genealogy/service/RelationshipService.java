package org.bbekker.genealogy.service;

import java.util.List;

import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.Relationship;
import org.bbekker.genealogy.repository.RelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class RelationshipService {

	@Value("${spring.data.web.pageable.default-page-size}")
	private int defaultPageSize;

	@Autowired
	MessageSource messageSource;

	@Autowired
	IndividualService individualService;

	@Autowired
	private RelationshipRepository relationshipRepository;

	void deleteRelationshipsForIndividual(String id) {

		Individual individual = individualService.get(id);
		if (individual != null) {
			deleteRelationshipsForIndividual(individual);
		}
	}

	void deleteRelationshipsForIndividual(Individual individual) {
		// Get all relationships where individual is involved
		List<Relationship> relationships = relationshipRepository.findByIndividual1(individual);
		relationships.addAll(relationshipRepository.findByIndividual2(individual));

		// Delete them, related events should be removed by cascading
		for (Relationship relationship : relationships) {
			relationshipRepository.delete(relationship);
		}

	}

}
