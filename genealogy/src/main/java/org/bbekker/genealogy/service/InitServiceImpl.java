package org.bbekker.genealogy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.BaseName;
import org.bbekker.genealogy.repository.BaseNamePrefix;
import org.bbekker.genealogy.repository.BaseNamePrefixRepository;
import org.bbekker.genealogy.repository.BaseNameRepository;
import org.bbekker.genealogy.repository.EventType;
import org.bbekker.genealogy.repository.EventTypeRepository;
import org.bbekker.genealogy.repository.Gender;
import org.bbekker.genealogy.repository.GenderRepository;
import org.bbekker.genealogy.repository.RelationshipType;
import org.bbekker.genealogy.repository.RelationshipTypeRepository;
import org.bbekker.genealogy.repository.RoleType;
import org.bbekker.genealogy.repository.RoleTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class InitServiceImpl implements InitService {

	private static final Logger logger = LoggerFactory.getLogger(InitServiceImpl.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	private BaseNameRepository baseNameRepository;

	@Autowired
	private BaseNamePrefixRepository baseNamePrefixRepository;

	@Autowired
	private GenderRepository genderRepository;

	@Autowired
	private RoleTypeRepository roleTypeRepository;

	@Autowired
	private RelationshipTypeRepository relationshipTypeRepository;

	@Autowired
	private EventTypeRepository eventTypeRepository;

	@Override
	public Boolean loadInitialData(Locale locale) {

		Boolean result = Boolean.TRUE;

		if (!loadBaseNames()) {
			result = Boolean.FALSE;
		}

		if (!loadBaseNamePrefixes()) {
			result = Boolean.FALSE;
		}

		if (!loadGenders()) {
			result = Boolean.FALSE;
		}

		if (!loadRelationshipTypes()) {
			result = Boolean.FALSE;
		}

		if (!loadRoles()) {
			result = Boolean.FALSE;
		}

		if (!loadEventTypes()) {
			result = Boolean.FALSE;
		}

		final String return_msg = messageSource.getMessage("init.finished", null, locale);
		logger.info(return_msg);

		return result;
	}

	private Boolean loadBaseNames() {

		Boolean result = Boolean.FALSE;

		// load names from csv file, but only if the table is still emtpy
		long numOfBaseNameEntries = baseNameRepository.count();
		if (numOfBaseNameEntries == 0) {

			try {
				final Resource resource = new ClassPathResource(AppConstants.BASE_NAME_CSV_PATH);
				final InputStream inputStream = resource.getInputStream();

				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;

				while ((line = bufferedReader.readLine()) != null) {

					// Skip empty entries
					final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
					if ((lineList.get(1) != null && !lineList.get(1).isEmpty())
							&& (lineList.get(0) != null && !lineList.get(0).isEmpty())) {
						BaseName bn = new BaseName(lineList.get(1), lineList.get(0));
						bn = baseNameRepository.save(bn);
					}
				}

				bufferedReader.close();
				inputStream.close();

				result = Boolean.TRUE;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Table has rows, assume it's loaded already, do nothing.
			// TODO: do a table content refresh, maybe there is changed content.
			result = Boolean.TRUE;
		}

		return result;
	}

	private Boolean loadBaseNamePrefixes() {

		Boolean result = Boolean.FALSE;

		// load name prefixes from csv file, but only if the table is still emtpy
		long numOfBaseNamePrefixEntries = baseNamePrefixRepository.count();
		if (numOfBaseNamePrefixEntries == 0) {
			try {

				final Resource resource = new ClassPathResource(AppConstants.BASE_NAME_PREFIX_CSV_PATH);
				final InputStream inputStream = resource.getInputStream();

				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;

				while ((line = bufferedReader.readLine()) != null) {

					// Skip empty entries
					final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
					if ((lineList.get(1) != null && !lineList.get(1).isEmpty())
							&& (lineList.get(0) != null && !lineList.get(0).isEmpty())) {
						BaseNamePrefix bnp = new BaseNamePrefix(lineList.get(1), lineList.get(0));
						bnp = baseNamePrefixRepository.save(bnp);
					}
				}

				bufferedReader.close();
				inputStream.close();

				result = Boolean.TRUE;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// Table has rows, assume it's loaded already, do nothing.
			// TODO: do a table content refresh, maybe there is changed content.
			result = Boolean.TRUE;
		}

		return result;
	}

	private Boolean loadGenders() {

		Boolean result = Boolean.FALSE;

		// Load additional gender types from csv file.
		try {

			final Resource resource = new ClassPathResource(AppConstants.GENDER_CSV_PATH);
			final InputStream inputStream = resource.getInputStream();

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				// Skip empty entries
				final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
				if (
						(lineList.get(0) != null && !lineList.get(0).isEmpty()) && // id
						(lineList.get(1) != null && !lineList.get(1).isEmpty()) && // qualifier
						(lineList.get(2) != null && !lineList.get(2).isEmpty()) && // description
						(lineList.get(3) != null && !lineList.get(3).isEmpty()) // symbol
						)
				{
					Gender gender = new Gender(lineList.get(0), lineList.get(1), lineList.get(2), lineList.get(3));
					gender = genderRepository.save(gender);
				}
			}

			bufferedReader.close();
			inputStream.close();

			result = Boolean.TRUE;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private Boolean loadRoles() {

		Boolean result = Boolean.FALSE;

		// Load additional roles types from csv file.
		try {

			final Resource resource = new ClassPathResource(AppConstants.ROLE_CSV_PATH);
			final InputStream inputStream = resource.getInputStream();

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				// Skip empty entries
				final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
				if (
						(lineList.get(0) != null && !lineList.get(0).isEmpty()) && // qualifier
						(lineList.get(1) != null && !lineList.get(1).isEmpty())) // description
				{
					RoleType roleType = new RoleType(lineList.get(0), lineList.get(1));
					roleType = roleTypeRepository.save(roleType);
				}
			}

			bufferedReader.close();
			inputStream.close();

			result = Boolean.TRUE;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private Boolean loadRelationshipTypes() {

		Boolean result = Boolean.FALSE;

		// Load additional relationship types from csv file.
		try {

			final Resource resource = new ClassPathResource(AppConstants.RELATIONSHIP_TYPE_CSV_PATH);
			final InputStream inputStream = resource.getInputStream();

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				// Skip empty entries
				final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
				if (
						(lineList.get(0) != null && !lineList.get(0).isEmpty()) && // id
						(lineList.get(1) != null && !lineList.get(1).isEmpty()) && // qualifier
						(lineList.get(2) != null && !lineList.get(2).isEmpty()) // description
						)
				{
					RelationshipType role = new RelationshipType(lineList.get(0), lineList.get(1), lineList.get(2));
					role = relationshipTypeRepository.save(role);
				}
			}

			bufferedReader.close();
			inputStream.close();

			result = Boolean.TRUE;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	private Boolean loadEventTypes() {

		Boolean result = Boolean.FALSE;

		// Load additional relationship types from csv file.
		try {

			final Resource resource = new ClassPathResource(AppConstants.EVENT_TYPE_CSV_PATH);
			final InputStream inputStream = resource.getInputStream();

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				// Skip empty entries
				final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
				if (
						(lineList.get(0) != null && !lineList.get(0).isEmpty()) && // id
						(lineList.get(1) != null && !lineList.get(1).isEmpty()) && // category
						(lineList.get(2) != null && !lineList.get(2).isEmpty()) && // qualifier
						(lineList.get(3) != null && !lineList.get(3).isEmpty()) // description
						)
				{
					EventType eventType = new EventType(lineList.get(0), lineList.get(1), lineList.get(2), lineList.get(3));
					logger.info("EventType: " + eventType.toString());
					eventType = eventTypeRepository.save(eventType);
				}
			}

			bufferedReader.close();
			inputStream.close();

			result = Boolean.TRUE;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}


}
