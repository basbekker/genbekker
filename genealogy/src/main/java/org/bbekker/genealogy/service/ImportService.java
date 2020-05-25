package org.bbekker.genealogy.service;

import org.springframework.stereotype.Service;

@Service
public interface ImportService {

	Boolean parseBekkerCsvFile(String fileName);

	Boolean verifyUploadedFile(String fileName);
}
