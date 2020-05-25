package org.bbekker.genealogy.controller;

import java.util.Locale;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.service.ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/import")
public class ImportController {

	private static final Logger logger = LoggerFactory.getLogger(ImportController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	ImportService importService;

	@Value("${upload.folder}")
	String UPLOAD_FOLDER;

	@RequestMapping(path = "/bekkercsv", method = RequestMethod.GET)
	public ResponseEntity<?> doBekkerCsvImport(Locale locale) {

		Boolean parseResult = Boolean.FALSE;
		String return_msg = SystemConstants.EMPTY_STRING;

		parseResult = importService.parseBekkerCsvFile(UPLOAD_FOLDER + AppConstants.BEKKER_CSV_NAME);

		return_msg = messageSource.getMessage("import.finished " + parseResult, null, locale);
		logger.info(return_msg);

		if (parseResult) {
			return new ResponseEntity<>(return_msg, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(return_msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(path = "/bekkertestcsv", method = RequestMethod.GET)
	public ResponseEntity<?> doBekkerTestCsvImport(Locale locale) {

		Boolean parseResult = Boolean.FALSE;
		String return_msg = SystemConstants.EMPTY_STRING;

		parseResult = importService.parseBekkerCsvFile(UPLOAD_FOLDER + AppConstants.BEKKER_TEST_CSV_NAME);

		return_msg = messageSource.getMessage("import.finished " + parseResult, null, locale);
		logger.info(return_msg);

		if (parseResult) {
			return new ResponseEntity<>(return_msg, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(return_msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
