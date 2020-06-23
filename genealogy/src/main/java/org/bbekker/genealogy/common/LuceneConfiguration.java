package org.bbekker.genealogy.common;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;

public class LuceneConfiguration {

	@Bean
	public LuceneIndexServiceBean luceneIndexServiceBean(EntityManagerFactory EntityManagerFactory){
		LuceneIndexServiceBean luceneIndexServiceBean = new LuceneIndexServiceBean(EntityManagerFactory);
		luceneIndexServiceBean.triggerIndexing();
		return luceneIndexServiceBean;
	}

}
