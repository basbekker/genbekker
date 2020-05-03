package org.bbekker.genealogy.imports;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportController {

	@RequestMapping(path = "/import/*", method = RequestMethod.GET)
	public String doImports() {
		return "doImport";
	}
}
