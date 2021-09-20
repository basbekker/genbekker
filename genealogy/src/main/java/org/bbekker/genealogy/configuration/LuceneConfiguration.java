package org.bbekker.genealogy.configuration;

import javax.persistence.EntityManagerFactory;

import org.bbekker.genealogy.common.LuceneIndexServiceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LuceneConfiguration {

	@Bean
	public LuceneIndexServiceBean luceneIndexServiceBean(EntityManagerFactory EntityManagerFactory){
		LuceneIndexServiceBean luceneIndexServiceBean = new LuceneIndexServiceBean(EntityManagerFactory);
		luceneIndexServiceBean.triggerIndexing();
		return luceneIndexServiceBean;
	}

}
