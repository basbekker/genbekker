package org.bbekker.genealogy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.AppConstants.EventTypes;
import org.bbekker.genealogy.common.AppConstants.GenderTypes;
import org.bbekker.genealogy.common.AppConstants.RelationshipTypes;
import org.bbekker.genealogy.common.AppConstants.Roles;
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
import org.bbekker.genealogy.repository.Role;
import org.bbekker.genealogy.repository.RoleRepository;
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
	private RoleRepository roleRepository;

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

		// First, load fixed set of gender types from the enumeration.
		if (GenderTypes.values().length > 0) {

			// Load the roles from the enumeration.
			for (GenderTypes acGenderType : GenderTypes.values()) {
				Gender gender = new Gender(acGenderType.getGenderQualifier(), acGenderType.getGenderSymbol(),
						acGenderType.getGenderDescription());
				gender = genderRepository.save(gender);
			}
			result = Boolean.TRUE;
		}

		// Next, load additional gender types from csv file.
		try {

			final Resource resource = new ClassPathResource(AppConstants.GENDER_CSV_PATH);
			final InputStream inputStream = resource.getInputStream();

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				// Skip empty entries
				final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
				if ((lineList.get(0) != null && !lineList.get(0).isEmpty()) && // qualifier
						(lineList.get(1) != null && !lineList.get(1).isEmpty()) && // symbol
						(lineList.get(2) != null && !lineList.get(2).isEmpty())) // description
				{
					Gender gender = new Gender(lineList.get(0), lineList.get(1), lineList.get(2));
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

		// First, load fixed set of roles from the enumeration.
		if (Roles.values().length > 0) {

			// Load the roles from the enumeration.
			for (Roles acRrole : Roles.values()) {
				Role role = new Role(acRrole.getRoleQualifier(), acRrole.getRoleDescription());
				role = roleRepository.save(role);
			}
			result = Boolean.TRUE;
		}

		// Next, load additional roles types from csv file.
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
					Role role = new Role(lineList.get(0), lineList.get(1));
					role = roleRepository.save(role);
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

		// First, load fixed set of relationship types from the enumeration.
		if (RelationshipTypes.values().length > 0) {

			// Load the relationship types from the enumeration.
			for (RelationshipTypes acRelationshipType : RelationshipTypes.values()) {
				RelationshipType relationshipType = new RelationshipType(
						acRelationshipType.getRelationshipTypeQualifier(),
						acRelationshipType.getRelationshipTypeDescription());
				relationshipType = relationshipTypeRepository.save(relationshipType);
			}
			result = Boolean.TRUE;
		}

		// Next, load additional relationship types from csv file.
		try {

			final Resource resource = new ClassPathResource(AppConstants.RELATIONSHIP_TYPE_CSV_PATH);
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
					RelationshipType role = new RelationshipType(lineList.get(0), lineList.get(1));
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


		// First, load fixed set of relationship types from the enumeration.
		if (EventTypes.values().length > 0) {

			// Load the relationship types from the enumeration.
			for (EventTypes acEventType : EventTypes.values()) {
				EventType eventType = new EventType(
						acEventType.getEventTypeQualifier(),
						acEventType.getEventTypeCategory(),
						acEventType.getEventTypeDescription());
				eventType = eventTypeRepository.save(eventType);
			}
			result = Boolean.TRUE;
		}

		// Next, load additional relationship types from csv file.
		try {

			final Resource resource = new ClassPathResource(AppConstants.EVENT_TYPE_CSV_PATH);
			final InputStream inputStream = resource.getInputStream();

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {

				// Skip empty entries
				final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
				if (
						(lineList.get(0) != null && !lineList.get(0).isEmpty()) && // qualifier
						(lineList.get(1) != null && !lineList.get(1).isEmpty()) && // category
						(lineList.get(2) != null && !lineList.get(2).isEmpty())) // description
				{
					EventType eventType = new EventType(lineList.get(0), lineList.get(1), lineList.get(2));
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
