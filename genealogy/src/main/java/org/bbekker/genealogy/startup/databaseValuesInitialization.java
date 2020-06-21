package org.bbekker.genealogy.startup;

import java.util.Locale;

import org.bbekker.genealogy.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class databaseValuesInitialization implements CommandLineRunner {

	@Autowired
	private InitService initService;

	@Override
	public void run(String... args) throws Exception {
		// Get OOTB reference data loaded in tables.
		Boolean result = initService.loadInitialData(Locale.ENGLISH);
	}

}
