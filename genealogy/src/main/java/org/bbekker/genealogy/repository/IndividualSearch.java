package org.bbekker.genealogy.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class IndividualSearch {

	private static final Logger logger = LoggerFactory.getLogger(IndividualSearch.class);

	// Spring will inject here the entity manager object
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Individual> search(String term) {

		List<Individual> individuals = null;

		// Get the FullTextEntityManager
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

		// Create a Hibernate Search DSL query builder for the required entity
		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
				.buildQueryBuilder().forEntity(Individual.class).get();

		// Generate a Lucene query using the builder
		Query query = queryBuilder.keyword().onFields("lastName", "firstName", "middleName", "maidenName", "familiarName").matching(term).createQuery();

		FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Individual.class);

		// returns JPA managed entities

		individuals = fullTextQuery.getResultList();

		logger.info("Found individuals: " + individuals.toString());

		return individuals;
	}
}
