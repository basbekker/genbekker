package org.bbekker.genealogy.configuration;

import java.util.Locale;

import org.bbekker.genealogy.common.LuceneIndexServiceBean;
import org.bbekker.genealogy.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SystemInitialization implements CommandLineRunner {

	@Autowired
	private InitService initService;

	@Autowired
	private LuceneIndexServiceBean luceneIndexServiceBean;

	@Value("${spring.jpa.properties.hibernate.search.default.indexBase}")
	private String luceneIndexPath;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String databaseDdlAuto;

	@Override
	public void run(String... args) throws Exception {

		luceneInitialization();

		dataInitialization();
	}

	private void dataInitialization() {

		// On startup get reference data loaded in tables.
		initService.loadInitialData(Locale.ENGLISH);
	}

	private void luceneInitialization() {

		// If we drop/create the database on restart, reset the Lucene index files as well.
		if (databaseDdlAuto != null && databaseDdlAuto.equals("create-drop")) {
			luceneIndexServiceBean.resetIndex(luceneIndexPath);
		}

		// On startup always do a re-index.
		luceneIndexServiceBean.triggerIndexing();
	}

}
