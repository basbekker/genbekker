package org.bbekker.genealogy.service;

import java.util.Locale;

import org.bbekker.genealogy.dto.ExtendedOffspringListDTO;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {

	ExtendedOffspringListDTO getExtendedReportOfOffspring(String individualId, Locale locale);

}
