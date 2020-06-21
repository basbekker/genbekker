package org.bbekker.genealogy.common;

import java.util.Locale;

import org.bbekker.genealogy.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
//@Order(1)
public class DatabaseInitialization implements CommandLineRunner {

	@Autowired
	private InitService initService;

	@Override
	public void run(String... args) throws Exception {
		// Get OOTB reference data loaded in tables.
		Boolean result = initService.loadInitialData(Locale.ENGLISH);
	}

}
