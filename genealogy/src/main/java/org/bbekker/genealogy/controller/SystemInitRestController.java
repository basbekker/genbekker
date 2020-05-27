package org.bbekker.genealogy.controller;

import java.util.Locale;

import org.bbekker.genealogy.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemInitRestController {

	@Autowired
	private InitService initService;

	@RequestMapping(path = "/init", method = RequestMethod.GET)
	public ResponseEntity<?> systemInitializer(Locale locale) {

		Boolean result = initService.loadInitialData(locale);

		if(result) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
