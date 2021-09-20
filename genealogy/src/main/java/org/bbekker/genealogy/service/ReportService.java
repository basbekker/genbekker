package org.bbekker.genealogy.service;

import java.util.Locale;

import org.bbekker.genealogy.api.OffspringListDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {

	OffspringListDTO getOffspringReport(String individualId, Locale locale);

}
