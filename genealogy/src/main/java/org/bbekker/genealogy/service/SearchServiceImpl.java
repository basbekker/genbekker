package org.bbekker.genealogy.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.lucene.search.Query;
import org.bbekker.genealogy.repository.Individual;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

	@Autowired
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Individual> SearchByTerm(String term) {

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
