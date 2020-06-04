package org.bbekker.genealogy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.AppConstants.RelationshipTypes;
import org.bbekker.genealogy.common.AppConstants.Roles;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.BaseName;
import org.bbekker.genealogy.repository.BaseNamePrefix;
import org.bbekker.genealogy.repository.BaseNamePrefixRepository;
import org.bbekker.genealogy.repository.BaseNameRepository;
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

	@Override
	public Boolean loadInitialData(Locale locale) {

		Boolean result = Boolean.TRUE;

		if (!loadBaseNames())  {
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
					if ( (lineList.get(1) != null && !lineList.get(1).isEmpty())
							&& (lineList.get(0) != null && !lineList.get(0).isEmpty())
							) {
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
					if ( (lineList.get(1) != null && !lineList.get(1).isEmpty())
							&& (lineList.get(0) != null && !lineList.get(0).isEmpty())
							) {
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

		// load name prefixes from csv file, but only if the table is still emtpy
		long numOfGenderEntries = genderRepository.count();
		if (numOfGenderEntries == 0) {
			try {

				final Resource resource = new ClassPathResource(AppConstants.GENDER_CSV_PATH);
				final InputStream inputStream = resource.getInputStream();

				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;

				while ((line = bufferedReader.readLine()) != null) {

					// Skip empty entries
					final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
					if ( (lineList.get(1) != null && !lineList.get(1).isEmpty())
							&& (lineList.get(0) != null && !lineList.get(0).isEmpty())
							) {
						Gender gnd = new Gender(lineList.get(0), lineList.get(1), lineList.get(2));
						gnd = genderRepository.save(gnd);
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

	private Boolean loadRoles() {

		Boolean result = Boolean.FALSE;

		if (Roles.values().length > 0) {

			// Load the roles from the enumeration.
			for (Roles acRrole : Roles.values()) {
				Role role = new Role(acRrole.getRoleId(), acRrole.getRoleName());
				role = roleRepository.save(role);
			}
			result = Boolean.TRUE;
		}

		return result;
	}

	private Boolean loadRelationshipTypes() {

		Boolean result = Boolean.FALSE;

		if (RelationshipTypes.values().length > 0) {

			// Load the relationship types from the enumeration.
			for (RelationshipTypes acRelationshipType : RelationshipTypes.values()) {
				RelationshipType relationshipType = new RelationshipType(acRelationshipType.getRelationshipTypeId(), acRelationshipType.getRelationshipTypeName());
				relationshipType = relationshipTypeRepository.save(relationshipType);
			}
			result = Boolean.TRUE;
		}

		return result;
	}

}
