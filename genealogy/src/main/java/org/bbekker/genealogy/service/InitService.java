package org.bbekker.genealogy.service;

import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public interface InitService {

	Boolean loadInitialData(Locale locale);

}
