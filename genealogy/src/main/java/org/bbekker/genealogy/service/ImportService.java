package org.bbekker.genealogy.service;

import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

@Service
public interface ImportService {

	Future<Boolean> parseBekkerCsvFile(String fileName);

}
