package org.bbekker.genealogy.controller;

import java.util.Locale;

import org.bbekker.genealogy.dto.OffspringListDTO;
import org.bbekker.genealogy.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportsController {

	@Autowired
	private ReportService reportService;

	@RequestMapping(path = "/offspringreport", method = RequestMethod.GET)
	public ResponseEntity<OffspringListDTO>  getOffspringReport(
			@RequestParam("id") String id,
			Locale locale) {

		OffspringListDTO offspringList = reportService.getOffspringReport(id, locale);

		return new ResponseEntity<OffspringListDTO>(offspringList, HttpStatus.OK);
	}

}
