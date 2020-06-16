package org.bbekker.genealogy.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.AppConstants.EventTypes;
import org.bbekker.genealogy.common.AppConstants.GenderTypes;
import org.bbekker.genealogy.common.AppConstants.RelationshipTypes;
import org.bbekker.genealogy.common.AppConstants.Roles;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.EventRepository;
import org.bbekker.genealogy.repository.EventType;
import org.bbekker.genealogy.repository.EventTypeRepository;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.repository.Relationship;
import org.bbekker.genealogy.repository.RelationshipRepository;
import org.bbekker.genealogy.repository.RelationshipType;
import org.bbekker.genealogy.repository.RelationshipTypeRepository;
import org.bbekker.genealogy.repository.RoleType;
import org.bbekker.genealogy.repository.RoleTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class ImportServiceImpl implements ImportService {

	private static final Logger logger = LoggerFactory.getLogger(ImportServiceImpl.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	private IndividualRepository individualRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventTypeRepository eventTypeRepository;

	@Autowired
	private RelationshipRepository relationshipRepository;

	@Autowired
	private RelationshipTypeRepository relationshipTypeRepository;

	@Autowired
	private RoleTypeRepository roleTypeRepository;

	@Value("${bekker.csv.blacklist}")
	private String EigenCodeBlackList;


	@Override
	public Boolean parseBekkerCsvFile(String fileName) {

		Boolean parseResult = Boolean.FALSE;
		long lineNum = 1;

		try {
			File uploadedFile = new File(fileName);
			InputStream inputStream = new FileInputStream(uploadedFile);

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;


			Map<Integer, String> headerMappings = null;
			final Map<String, String> eigenCodeToIdMapping = new HashMap<String, String>();
			final Map<String, String> parentToChildMapping = new HashMap<String, String>();

			final List<String> blackList = loadBlacklistedLines(EigenCodeBlackList);

			while ((line = bufferedReader.readLine()) != null) {

				logger.info("line=" + line);

				if (lineNum == 1) {
					// First line of the csv has the column names.
					headerMappings = importCSVHeaderLine(line);
				} else {
					// Other lines are data lines.
					parseResult = importCSVDataLine(line, headerMappings, blackList, eigenCodeToIdMapping, parentToChildMapping);
				}

				lineNum++;

			} // end while

			bufferedReader.close();
			inputStream.close();

			Long numberOfIndividualEntries = individualRepository.count();
			logger.info("Individual entries=" + numberOfIndividualEntries.toString());

			creatParentChildRelationships(eigenCodeToIdMapping, parentToChildMapping);

			Long numberOfRelationshipEntries = relationshipRepository.count();
			logger.info("Relationship entries=" + numberOfRelationshipEntries.toString());

		} catch (Exception e) {
			parseResult = Boolean.FALSE;
			logger.error("Parse " + fileName + " failed on line " + lineNum + ": ", e.getLocalizedMessage());
			e.printStackTrace();
		}

		return parseResult;
	}

	private String stripQuotes(String field) {
		return field.replaceAll(SystemConstants.DOUBLE_QUOTE, SystemConstants.EMPTY_STRING).trim();
	}

	private List<String> loadBlacklistedLines(String listOfCodes) {
		// Load lines to be skipped as they contain junk, or are not relevant.
		final List<String> blacklist = Arrays.asList(EigenCodeBlackList.split(SystemConstants.COMMA));
		return blacklist;
	}

	private Map<Integer, String> importCSVHeaderLine(String line) {

		Map<Integer, String> headerMappings = new HashMap<Integer, String>();

		// "PROJNR","EIGENCODE","HUWNR","HUWTYPE","XHUW","VADERCODE","MOEDER","REF1","REF2","REFH","RNAAM","TITEL","INITS5","VNAMEN","VVOEG","ANAAM","NAAMGRP","NAAMCODE","GESLACHT","BEROEP","GEBDP","GEBDATUM","GEBPLTS","GEBPROV","GEBLAND","GEBAKTDAT","GEBGEM","GEBARCH","GEBREG","GEBFOL","GEBAKTE","GEBGEZDTE","GEBBRON","OVLBG","OVLDATUM","OVLPLTS","OVLPROV","OVLLAND","OVLAKTDAT","OVLGEM","OVLARCH","OVLREG","OVLFOL","OVLAKTE","OVLBRON","PRNAAM","PTITEL","PVNAMEN","PVVOEG","PANAAM","PNAAMGRP","PGESLACHT","PBEROEP","PGEBDP","PGEBDATUM","PGEBPLTS","PGEBPROV","PGEBLAND","PGEBAKTDAT","PGEBGEM","PGEBARCH","PGEBREG","PGEBFOL","PGEBAKTE","PGEBBRON","PGEBGEZDTE","POVLBG","POVLDATUM","POVLPLTS","POVLPROV","POVLLAND","POVLAKTDAT","POVLGEM","POVLARCH","POVLREG","POVLFOL","POVLAKTE","POVLBRON","HUWTR","HUWDATUM","HUWPLTS","HUWPROV","HUWLAND","HUWAKTDAT","HUWGEM","HUWARCH","HUWREG","HUWFOL","HUWAKTE","HUWGEZDTE","HUWBRON","SCHRB","SCHDATUM","SCHPLTS","SCHPROV","SCHLAND","SCHAKTE","SCHAKTDAT","SCHGEM","SCHARCH","SCHREG","SCHFOL","SCHBRON","OPM1","OPM2","OPMH","KIND1","KIND2","KIND3","KIND4","KIND5","KIND6","KIND7","KIND8","KIND9","KIND10","KIND11","KIND12","KIND13","KIND14","KIND15","KIND16","KIND17","KIND18","PRIVACY","RELATIE"
		final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
		int position = 0;

		for (String fieldName : lineList) {
			headerMappings.put(position, stripQuotes(fieldName));
			position++;
		}

		return headerMappings;
	}

	private Boolean importCSVDataLine(String line, Map<Integer, String> headerMappings, List<String> blackListedLines, Map<String, String> eigenCodeToIdMapping, Map<String, String> parentToChildMapping) {

		final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));

		String eigenCode = SystemConstants.EMPTY_STRING;

		String fatherEigenCode = SystemConstants.EMPTY_STRING;
		String motherEigenCode = SystemConstants.EMPTY_STRING;
		String ref1 = SystemConstants.EMPTY_STRING;
		String ref2 = SystemConstants.EMPTY_STRING;

		String kind1 = SystemConstants.EMPTY_STRING;
		String kind2 = SystemConstants.EMPTY_STRING;
		String kind3 = SystemConstants.EMPTY_STRING;
		String kind4 = SystemConstants.EMPTY_STRING;
		String kind5 = SystemConstants.EMPTY_STRING;
		String kind6 = SystemConstants.EMPTY_STRING;
		String kind7 = SystemConstants.EMPTY_STRING;
		String kind8 = SystemConstants.EMPTY_STRING;
		String kind9 = SystemConstants.EMPTY_STRING;
		String kind10 = SystemConstants.EMPTY_STRING;
		String kind11 = SystemConstants.EMPTY_STRING;
		String kind12 = SystemConstants.EMPTY_STRING;
		String kind13 = SystemConstants.EMPTY_STRING;
		String kind14 = SystemConstants.EMPTY_STRING;
		String kind15 = SystemConstants.EMPTY_STRING;
		String kind16 = SystemConstants.EMPTY_STRING;
		String kind17 = SystemConstants.EMPTY_STRING;
		String kind18 = SystemConstants.EMPTY_STRING;

		// Principal individual.
		String lastName = SystemConstants.EMPTY_STRING;
		String firstName = SystemConstants.EMPTY_STRING;
		String middleName = SystemConstants.EMPTY_STRING;
		String maidenName = SystemConstants.EMPTY_STRING;
		String familiarName = SystemConstants.EMPTY_STRING;
		String genderType = null;
		Date birthDate = null;
		String birthPlace = SystemConstants.EMPTY_STRING;
		String birthNote = SystemConstants.EMPTY_STRING;
		Date deathDate = null;
		String deathPlace = SystemConstants.EMPTY_STRING;
		String deathNote = SystemConstants.EMPTY_STRING;
		String notes = SystemConstants.EMPTY_STRING;

		// Partner individual.
		String partnerLastName = SystemConstants.EMPTY_STRING;
		String partnerFirstName = SystemConstants.EMPTY_STRING;
		String partnerMiddleName = SystemConstants.EMPTY_STRING;
		String partnerMaidenName = SystemConstants.EMPTY_STRING;
		String partnerFamiliarName = SystemConstants.EMPTY_STRING;
		String partnerGenderType = null;
		Date partnerBirthDate = null;
		String partnerBirthPlace = SystemConstants.EMPTY_STRING;
		String partnerBirthNote = SystemConstants.EMPTY_STRING;
		Date partnerDeathDate = null;
		String partnerDeathPlace = SystemConstants.EMPTY_STRING;
		String partnerDeathNote = SystemConstants.EMPTY_STRING;
		String partnerNotes = SystemConstants.EMPTY_STRING;

		int position = 0;
		boolean parseOk = true;
		boolean parsePartnerOk = true;
		boolean blackListed = false;

		for (String field : lineList) {

			String fieldName = headerMappings.get(position);

			if (fieldName == null) {
				fieldName = SystemConstants.EMPTY_STRING;
			}

			if (fieldName.equals(AppConstants.EIGENCODE_NL)) {
				eigenCode = stripQuotes(field);
				// Some lines are to be skipped for being junk, irrelevant etc.
				if (blackListedLines.contains(eigenCode)) {
					blackListed = true;
					parseOk = false;
					logger.debug("line skip - blacklisted");
				}
			}

			if (!blackListed && parseOk) {

				if (fieldName.equals(AppConstants.VADERCODE_NL)) {
					fatherEigenCode = emptyToNull(stripQuotes(field)) + "Z";
					motherEigenCode = emptyToNull(stripQuotes(field)) + "A";
					logger.debug("fatherEigenCode=" + fatherEigenCode);
				}

				if (fieldName.equals(AppConstants.REF1_NL)) {
					ref1 = emptyToNull(stripQuotes(field));
					logger.debug("ref1=" + ref1);
				}

				if (fieldName.equals(AppConstants.REF2_NL)) {
					ref2 = emptyToNull(stripQuotes(field));
					logger.debug("ref2=" + ref2);
				}

				if (fieldName.equals(AppConstants.KIND1_NL)) {
					kind1 = emptyToNull(stripQuotes(field));
					logger.debug("kind1=" + kind1);
				}

				if (fieldName.equals(AppConstants.KIND2_NL)) {
					kind2 = emptyToNull(stripQuotes(field));
					logger.debug("kind2=" + kind2);
				}

				if (fieldName.equals(AppConstants.KIND3_NL)) {
					kind3 = emptyToNull(stripQuotes(field));
					logger.debug("kind3=" + kind3);
				}

				if (fieldName.equals(AppConstants.KIND4_NL)) {
					kind4 = emptyToNull(stripQuotes(field));
					logger.debug("kind4=" + kind4);
				}

				if (fieldName.equals(AppConstants.KIND5_NL)) {
					kind5 = emptyToNull(stripQuotes(field));
					logger.debug("kind5=" + kind5);
				}

				if (fieldName.equals(AppConstants.KIND6_NL)) {
					kind6 = emptyToNull(stripQuotes(field));
					logger.debug("kind6=" + kind6);
				}

				if (fieldName.equals(AppConstants.KIND7_NL)) {
					kind7 = emptyToNull(stripQuotes(field));
					logger.debug("kind7=" + kind7);
				}

				if (fieldName.equals(AppConstants.KIND8_NL)) {
					kind8 = emptyToNull(stripQuotes(field));
					logger.debug("kind8=" + kind8);
				}

				if (fieldName.equals(AppConstants.KIND9_NL)) {
					kind9 = emptyToNull(stripQuotes(field));
					logger.debug("kind9=" + kind9);
				}

				if (fieldName.equals(AppConstants.KIND10_NL)) {
					kind10 = emptyToNull(stripQuotes(field));
					logger.debug("kind10=" + kind10);
				}

				if (fieldName.equals(AppConstants.KIND11_NL)) {
					kind11 = emptyToNull(stripQuotes(field));
					logger.debug("kind11=" + kind11);
				}

				if (fieldName.equals(AppConstants.KIND12_NL)) {
					kind12 = emptyToNull(stripQuotes(field));
					logger.debug("kind12=" + kind12);
				}

				if (fieldName.equals(AppConstants.KIND13_NL)) {
					kind13 = emptyToNull(stripQuotes(field));
					logger.debug("kind13=" + kind13);
				}

				if (fieldName.equals(AppConstants.KIND14_NL)) {
					kind14 = emptyToNull(stripQuotes(field));
					logger.debug("kind14=" + kind14);
				}

				if (fieldName.equals(AppConstants.KIND15_NL)) {
					kind15 = emptyToNull(stripQuotes(field));
					logger.debug("kind15=" + kind15);
				}

				if (fieldName.equals(AppConstants.KIND16_NL)) {
					kind16 = emptyToNull(stripQuotes(field));
					logger.debug("kind16=" + kind16);
				}

				if (fieldName.equals(AppConstants.KIND17_NL)) {
					kind17 = emptyToNull(stripQuotes(field));
					logger.debug("kind17=" + kind17);
				}

				if (fieldName.equals(AppConstants.KIND18_NL)) {
					kind18 = emptyToNull(stripQuotes(field));
					logger.debug("kind18=" + kind18);
				}

				// Principal individual parsing.
				if (fieldName.equals(AppConstants.RNAAM_NL)) {
					familiarName = emptyToNull(stripQuotes(field));
					logger.debug("familiarName=" + familiarName);
				}

				if (fieldName.equals(AppConstants.VVOEG_NL)) {
					middleName = emptyToNull(stripQuotes(field));
					logger.debug("middleName=" + middleName);
				}

				if (fieldName.equals(AppConstants.ANAAM_NL)) {
					lastName = emptyToNull(stripQuotes(field));
					if (lastName == null) {
						parseOk = false;
					}
					logger.debug("lastName=" + lastName);
				}

				if (fieldName.equals(AppConstants.VNAMEN_NL)) {
					firstName = emptyToNull(stripQuotes(field));
					if (firstName == null) {
						parseOk = false;
					}
					logger.debug("firstName=" + firstName);
				}

				if (fieldName.equals(AppConstants.GESLACHT_NL)) {
					final String importGenderType = stripQuotes(field);
					genderType = setGender(importGenderType);
					if (genderType != null) {
						logger.debug("gender=" + genderType);
					} else {
						logger.debug("gender=null");
					}
				}

				if (fieldName.equals(AppConstants.GEBDATUM_NL)) {
					final String birthDateString = stripQuotes(field);
					final Optional<Date> optionalBirthDate = setDate(birthDateString);
					birthDate = null;
					if (optionalBirthDate != null && optionalBirthDate.isPresent()) {
						birthDate = optionalBirthDate.get();
					} else {
						if (birthDateString != null && !birthDateString.isEmpty()) {
							// The birth date field has data, only not in proper format, maybe it has just
							// the year
							// or something alike, so do not loose that info and save it to the notes field.
							if (birthNote.length() > 0) {
								birthNote = birthNote + SystemConstants.SPACE;
							}
							birthNote = birthNote + AppConstants.GEBDATUM_NL + SystemConstants.EQUALS + birthDateString
									+ SystemConstants.SEMICOLON;
						}
					}
					logger.debug("birthDate=" + birthDate);
				}

				if (fieldName.equals(AppConstants.GEBPLTS_NL)) {
					if (field != null && !stripQuotes(field).isEmpty()) {
						if (birthPlace != null && !birthPlace.isEmpty()) {
							birthPlace = birthPlace + SystemConstants.COMMA + SystemConstants.SPACE
									+ stripQuotes(field);
						} else {
							birthPlace = birthPlace + stripQuotes(field);
						}
					}
					logger.debug("birthPlace=" + birthPlace);
				}

				if (fieldName.equals(AppConstants.GEBPROV_NL)) {
					if (field != null && !stripQuotes(field).isEmpty()) {
						if (birthPlace != null && !birthPlace.isEmpty()) {
							birthPlace = birthPlace + SystemConstants.COMMA + SystemConstants.SPACE
									+ stripQuotes(field);
						} else {
							birthPlace = birthPlace + stripQuotes(field);
						}
					}
					logger.debug("birthPlace=" + birthPlace);
				}

				if (fieldName.equals(AppConstants.GEBLAND_NL)) {
					if (field != null && !stripQuotes(field).isEmpty()) {
						if (birthPlace != null && !birthPlace.isEmpty()) {
							birthPlace = birthPlace + SystemConstants.COMMA + SystemConstants.SPACE
									+ stripQuotes(field);
						} else {
							birthPlace = birthPlace + stripQuotes(field);
						}
					}
					logger.debug("birthPlace=" + birthPlace);
				}

				if (fieldName.equals(AppConstants.OVLDATUM_NL)) {
					final String deathDateString = stripQuotes(field);
					final Optional<Date> optionalDeathDate = setDate(deathDateString);
					deathDate = null;
					if (optionalDeathDate != null && optionalDeathDate.isPresent()) {
						deathDate = optionalDeathDate.get();
					} else {
						if (deathDateString != null && !deathDateString.isEmpty()) {
							// The birth date field has data, only not in proper format, maybe it has just
							// the year
							// or something alike, so do not loose that info and save it to the notes field.
							if (deathNote.length() > 0) {
								deathNote = deathNote + SystemConstants.SPACE;
							}
							deathNote = deathNote + AppConstants.GEBDATUM_NL + SystemConstants.EQUALS + deathDateString
									+ SystemConstants.SEMICOLON;
						}
					}
					logger.debug("deathDate=" + deathDate);
				}

				// Partner individual parsing.
				if (fieldName.equals(AppConstants.PRNAAM_NL)) {
					partnerFamiliarName = emptyToNull(stripQuotes(field));
					logger.debug("partnerFamiliarName=" + partnerFamiliarName);
				}

				if (fieldName.equals(AppConstants.PANAAM_NL)) {
					partnerLastName = emptyToNull(stripQuotes(field));
					if (partnerLastName == null) {
						parsePartnerOk = false;
					}
					logger.debug("partnerLastName=" + partnerLastName);
				}

				if (fieldName.equals(AppConstants.PVNAMEN_NL)) {
					partnerFirstName = emptyToNull(stripQuotes(field));
					if (partnerFirstName == null) {
						parsePartnerOk = false;
					}
					logger.debug("partnerFirstName=" + partnerFirstName);
				}

				if (fieldName.equals(AppConstants.PVVOEG_NL)) {
					partnerMiddleName = emptyToNull(stripQuotes(field));
					logger.debug("partnerMiddleName=" + partnerMiddleName);
				}

				if (fieldName.equals(AppConstants.PGESLACHT_NL)) {
					final String importGenderType = stripQuotes(field);
					partnerGenderType = setGender(importGenderType);
					if (partnerGenderType != null) {
						logger.debug("gender=" + partnerGenderType);
					} else {
						logger.debug("gender=null");
					}
				}

				if (fieldName.equals(AppConstants.PGEBDATUM_NL)) {
					final String partnerBirthDateString = stripQuotes(field);
					final Optional<Date> optionalPartnerBirthDate = setDate(partnerBirthDateString);
					partnerBirthDate = null;
					if (optionalPartnerBirthDate != null && optionalPartnerBirthDate.isPresent()) {
						partnerBirthDate = optionalPartnerBirthDate.get();
					} else {
						if (partnerBirthDateString != null && !partnerBirthDateString.isEmpty()) {
							// The partner birth date field has data, only not in proper format, maybe it
							// has just the year
							// or something alike, so do not loose that info and save it to the notes field.
							if (partnerBirthNote.length() > 0) {
								partnerBirthNote = partnerBirthNote + SystemConstants.SPACE;
							}
							partnerBirthNote = partnerBirthNote + AppConstants.PGEBDATUM_NL + SystemConstants.EQUALS
									+ partnerBirthDateString + SystemConstants.SEMICOLON;
						}
					}
					logger.debug("partnerBirthDate=" + partnerBirthDate);
				}

				if (fieldName.equals(AppConstants.PGEBPLTS_NL)) {
					if (field != null && !stripQuotes(field).isEmpty()) {
						if (partnerBirthPlace != null && !partnerBirthPlace.isEmpty()) {
							partnerBirthPlace = partnerBirthPlace + SystemConstants.COMMA + SystemConstants.SPACE
									+ stripQuotes(field);
						} else {
							partnerBirthPlace = partnerBirthPlace + stripQuotes(field);
						}
					}
					logger.debug("partnerBirthPlace=" + partnerBirthPlace);
				}

				if (fieldName.equals(AppConstants.PGEBPROV_NL)) {
					if (field != null && !stripQuotes(field).isEmpty()) {
						if (partnerBirthPlace != null && !partnerBirthPlace.isEmpty()) {
							partnerBirthPlace = partnerBirthPlace + SystemConstants.COMMA + SystemConstants.SPACE
									+ stripQuotes(field);
						} else {
							partnerBirthPlace = partnerBirthPlace + stripQuotes(field);
						}
					}
					logger.debug("partnerBirthPlace=" + partnerBirthPlace);
				}

				if (fieldName.equals(AppConstants.PGEBLAND_NL)) {
					if (field != null && !stripQuotes(field).isEmpty()) {
						if (partnerBirthPlace != null && !partnerBirthPlace.isEmpty()) {
							partnerBirthPlace = partnerBirthPlace + SystemConstants.COMMA + SystemConstants.SPACE
									+ stripQuotes(field);
						} else {
							partnerBirthPlace = partnerBirthPlace + stripQuotes(field);
						}
					}
					logger.debug("partnerBirthPlace=" + partnerBirthPlace);
				}

				if (fieldName.equals(AppConstants.POVLDATUM_NL)) {
					final String partnerDeathDateString = stripQuotes(field);
					final Optional<Date> optionalPartnerDeathDate = setDate(partnerDeathDateString);
					partnerDeathDate = null;
					if (optionalPartnerDeathDate != null && optionalPartnerDeathDate.isPresent()) {
						partnerDeathDate = optionalPartnerDeathDate.get();
					} else {
						if (partnerDeathDateString != null && !partnerDeathDateString.isEmpty()) {
							// The partner birth date field has data, only not in proper format, maybe it
							// has just the year
							// or something alike, so do not loose that info and save it to the notes field.
							if (partnerDeathNote.length() > 0) {
								partnerDeathNote = partnerDeathNote + SystemConstants.SPACE;
							}
							partnerDeathNote = partnerDeathNote + AppConstants.PGEBDATUM_NL + SystemConstants.EQUALS
									+ partnerDeathDateString + SystemConstants.SEMICOLON;
						}
					}
					logger.debug("partnerDeathDate=" + partnerDeathDate);
				}

			}

			position++;
		}

		if (parseOk) {

			Individual individual = new Individual(lastName, firstName, middleName, maidenName, familiarName,
					genderType);
			individual.setNote(notes);
			individual = individualRepository.save(individual);
			logger.info("individual=" + individual.toString());

			if (ref1 != null) {
				eigenCodeToIdMapping.put(ref1, individual.getId());
			}

			if (birthDate != null  || (birthNote != null && !birthNote.isEmpty())) {
				EventType birthEventType = eventTypeRepository.findByQualifier(EventTypes.BIRTH.getEventTypeQualifier());
				Event birthEvent = new Event(individual, birthEventType, birthDate);
				birthEvent.setEventPlace(birthPlace);
				birthEvent.setEventNote(birthNote);
				birthEvent = eventRepository.save(birthEvent);
			}

			if (deathDate != null || (deathNote != null && !deathNote.isEmpty())) {
				EventType deathEventType = eventTypeRepository.findByQualifier(EventTypes.DEATH.getEventTypeQualifier());
				Event deathEvent = new Event(individual, deathEventType, deathDate);
				deathEvent.setEventPlace(deathPlace);
				deathEvent.setEventNote(deathNote);
				deathEvent = eventRepository.save(deathEvent);
			}

			if (parsePartnerOk) {

				Individual partnerIndividual = new Individual(partnerLastName, partnerFirstName, partnerMiddleName,
						partnerMaidenName, partnerFamiliarName, partnerGenderType);
				partnerIndividual.setNote(partnerNotes);
				partnerIndividual = individualRepository.save(partnerIndividual);
				logger.info("partnerIndividual=" + partnerIndividual.toString());

				if (ref2 != null) {
					eigenCodeToIdMapping.put(ref2, partnerIndividual.getId());
				}

				// Add to mapping to later on add parent-child relationships.
				if (kind1 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind1);
				}
				if (kind2 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind2);
				}
				if (kind3 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind3);
				}
				if (kind4 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind4);
				}
				if (kind5 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind5);
				}
				if (kind6 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind6);
				}
				if (kind7 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind7);
				}
				if (kind8 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind8);
				}
				if (kind9 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind9);
				}
				if (kind10 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind10);
				}
				if (kind11 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind11);
				}
				if (kind12 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind12);
				}
				if (kind13 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind13);
				}
				if (kind14 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind14);
				}
				if (kind15 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind15);
				}
				if (kind16 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind16);
				}
				if (kind17 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind17);
				}
				if (kind18 != null) {
					parentToChildMapping.put(individual.getId() + ":" + partnerIndividual.getId(), kind18);
				}


				if (partnerBirthDate != null || (partnerBirthNote != null && !partnerBirthNote.isEmpty())) {
					EventType birthEventType = eventTypeRepository.findByQualifier(EventTypes.BIRTH.getEventTypeQualifier());
					Event birthEvent = new Event(partnerIndividual, birthEventType, partnerBirthDate);
					birthEvent.setEventPlace(partnerBirthPlace);
					birthEvent.setEventNote(partnerBirthNote);
					logger.info("birthEvent=" + birthEvent.toString());
					birthEvent = eventRepository.save(birthEvent);
					logger.info("birthEvent=" + birthEvent.toString());
				}

				if (partnerDeathDate != null || (partnerDeathNote != null && !partnerDeathNote.isEmpty())) {
					EventType deathEventType = eventTypeRepository.findByQualifier(EventTypes.DEATH.getEventTypeQualifier());
					Event deathEvent = new Event(partnerIndividual, deathEventType, partnerDeathDate);
					deathEvent.setEventPlace(partnerDeathPlace);
					deathEvent.setEventNote(partnerDeathNote);
					logger.info("deathEvent=" + deathEvent.toString());
					deathEvent = eventRepository.save(deathEvent);
					logger.info("deathEvent=" + deathEvent.toString());
				}


				// The relationship is husband-wife.
				RoleType partner1RoleType = getRoleType(individual, Roles.HUSBAND.getRoleQualifier(), Roles.WIFE.getRoleQualifier(), Roles.HUSBAND.getRoleQualifier());
				RoleType partner2RoleType = getRoleType(partnerIndividual, Roles.HUSBAND.getRoleQualifier(), Roles.WIFE.getRoleQualifier(), Roles.HUSBAND.getRoleQualifier());
				RelationshipType relationshipType = null;
				Optional<RelationshipType> optionalRelationshipType = relationshipTypeRepository.findByQualifier(RelationshipTypes.MARRIED.getRelationshipTypeQualifier());
				if (optionalRelationshipType.isPresent()) {
					relationshipType = optionalRelationshipType.get();
				}

				if (partner1RoleType != null && partner2RoleType != null && relationshipType != null) {
					Relationship relationship = new Relationship(individual, partnerIndividual, partner1RoleType, partner2RoleType, relationshipType);
					relationship = relationshipRepository.save(relationship);
					logger.info("relationship=" + relationship.toString());
				}


				String parent1Id = null;
				String parent2Id = null;
				if (fatherEigenCode != null && motherEigenCode != null) {

					// Get the role of the individual (son or daughter)
					Optional<RoleType> optionalRoleType3 = Optional.empty();
					if (individual.getGenderType().equals(GenderTypes.MALE.getGenderQualifier())) {
						optionalRoleType3 = roleTypeRepository.findByQualifier(Roles.SON.getRoleQualifier());
					} else {
						if (individual.getGenderType().equals(GenderTypes.FEMALE.getGenderQualifier())) {
							optionalRoleType3 = roleTypeRepository.findByQualifier(Roles.DAUGHTER.getRoleQualifier());
						} else {
							optionalRoleType3 = roleTypeRepository.findByQualifier(Roles.SON.getRoleQualifier()); // TODO should be something else than SON
						}
					}

					// The relationship is parent-child.
					Optional<RelationshipType> optionalRelationshipType2 = relationshipTypeRepository.findByQualifier(RelationshipTypes.PARENT_CHILD.getRelationshipTypeQualifier());

					if ((parent1Id = eigenCodeToIdMapping.get(fatherEigenCode)) != null) {

						// So we have an individual with a first parent (we name it father, but could be either.

						// Get the role of the first parent.
						Optional<RoleType> optionalRoleType4 = Optional.empty();
						Optional<Individual> optionalPerson = individualRepository.findById(parent1Id);
						if (optionalPerson.isPresent()) {
							if (optionalPerson.get().getGenderType().equals(GenderTypes.MALE.getGenderQualifier())) {
								//father = optionalPerson.get();
								optionalRoleType4 = roleTypeRepository.findByQualifier(Roles.FATHER.getRoleQualifier());
							} else {
								if (optionalPerson.get().getGenderType().equals(GenderTypes.FEMALE.getGenderQualifier())) {
									//mother = optionalPerson.get();
									optionalRoleType4 = roleTypeRepository.findByQualifier(Roles.MOTHER.getRoleQualifier());
								} else  {
									//father = optionalPerson.get(); // TODO should be something else than father
									optionalRoleType4 = roleTypeRepository.findByQualifier(Roles.FATHER.getRoleQualifier());
								}
							}
						}

						// Save the relationship of the individual and its first parent.
						if (optionalPerson.isPresent() && optionalRoleType3.isPresent() && optionalRoleType4.isPresent()) {
							Relationship relationship = new Relationship(individual, optionalPerson.get(), optionalRoleType3.get(),
									optionalRoleType4.get(), optionalRelationshipType2.get());
							logger.info("relationship=" + relationship.toString());
							relationship = relationshipRepository.save(relationship);
							logger.info("relationship=" + relationship.toString());
						}

					}

					if ((parent2Id = eigenCodeToIdMapping.get(motherEigenCode)) != null) {

						// So the individual has a second parent as well.

						// Get the role of the second parent.
						Optional<RoleType> optionalRoleType5 = Optional.empty();
						Optional<Individual> optionalPerson = individualRepository.findById(parent2Id);
						if (optionalPerson.isPresent()) {
							if (optionalPerson.get().getGenderType().equals(GenderTypes.MALE.getGenderQualifier())) {
								//father = optionalPerson.get();
								optionalRoleType5 = roleTypeRepository.findByQualifier(Roles.FATHER.getRoleQualifier());
							} else {
								if (optionalPerson.get().getGenderType().equals(GenderTypes.FEMALE.getGenderQualifier())) {
									//mother = optionalPerson.get();
									optionalRoleType5 = roleTypeRepository.findByQualifier(Roles.MOTHER.getRoleQualifier());
								} else  {
									//father = optionalPerson.get(); // TODO should be something else than father
									optionalRoleType5 = roleTypeRepository.findByQualifier(Roles.FATHER.getRoleQualifier());
								}
							}
						}

						// Save the relationship of the individual and its second parent.
						if (optionalPerson.isPresent() && optionalRoleType3.isPresent() && optionalRoleType5.isPresent()) {
							Relationship relationship = new Relationship(individual, optionalPerson.get(), optionalRoleType3.get(),
									optionalRoleType5.get(), optionalRelationshipType2.get());
							logger.info("relationship=" + relationship.toString());
							relationship = relationshipRepository.save(relationship);
							logger.info("relationship=" + relationship.toString());
						}

					}
				}
			}
		}

		return Boolean.TRUE;
	}

	private void creatParentChildRelationships(Map<String, String> eigenCodeToIdMapping,
			Map<String, String> parentToChildMapping) {

		for (Map.Entry<String, String> entry : parentToChildMapping.entrySet()) {

			String parentIds = entry.getKey();
			String parent1Id = null;
			String parent2Id = null;
			if (parentIds != null && !parentIds.isEmpty()) {
				if (parentIds.indexOf(":") > -1) {
					// two parents
					parent1Id = parentIds.substring(0, parentIds.indexOf(":"));
					parent2Id = parentIds.substring(parentIds.indexOf(":") + 1, parentIds.length());
				} else {
					// just one parent.
					parent1Id = parentIds;
				}
			}

			// The eigenCode is a 5 digit string, which should be left-padded with zeros.
			String childEigenCode = entry.getValue();
			String childId = null;
			if (childEigenCode != null && !childEigenCode.isEmpty()) {
				childEigenCode = padZeros(childEigenCode, 5) + "Z";
				childId = eigenCodeToIdMapping.get(childEigenCode);
			}

			Individual parent1 = null;
			RoleType roleTypeParent1 = null;
			Individual parent2 = null;
			RoleType roleTypeParent2 = null;
			Individual child = null;
			RoleType roleTypeChild = null;
			if (parent1Id != null && !parent1Id.isEmpty()) {
				Optional<Individual> optionalParent1 = individualRepository.findById(parent1Id);
				if (optionalParent1.isPresent()) {
					parent1 = optionalParent1.get();
					roleTypeParent1 = getRoleType(parent1, Roles.FATHER.getRoleQualifier(), Roles.MOTHER.getRoleQualifier(), Roles.FATHER.getRoleQualifier());
				}
			}
			if (parent2Id != null && !parent2Id.isEmpty()) {
				Optional<Individual> optionalParent2 = individualRepository.findById(parent2Id);
				if (optionalParent2.isPresent()) {
					parent2 = optionalParent2.get();
					roleTypeParent2 = getRoleType(parent2, Roles.FATHER.getRoleQualifier(), Roles.MOTHER.getRoleQualifier(), Roles.FATHER.getRoleQualifier());
				}
			}
			if (childId != null && !childId.isEmpty()) {
				Optional<Individual> optionalChild = individualRepository.findById(childId);
				if (optionalChild.isPresent()) {
					child = optionalChild.get();
					// Get the role of the child (son or daughter)
					roleTypeChild = getRoleType(child, Roles.SON.getRoleQualifier(), Roles.DAUGHTER.getRoleQualifier(), Roles.SON.getRoleQualifier());
				}
			}

			// The relationship is parent-child.
			RelationshipType relationshipType = null;
			Optional<RelationshipType> optionalRelationshipType = relationshipTypeRepository.findByQualifier(RelationshipTypes.PARENT_CHILD.getRelationshipTypeQualifier());
			if (optionalRelationshipType.isPresent()) {
				relationshipType = optionalRelationshipType.get();
			}

			// Finally save the relationship of the child and its first parent.
			if (child != null && roleTypeChild != null && parent1 != null && roleTypeParent1 != null && relationshipType != null) {
				Relationship relationship = new Relationship(child, parent1, roleTypeChild, roleTypeParent1, relationshipType);
				relationship = relationshipRepository.save(relationship);
				logger.info("relationship=" + relationship.toString());
			}

			// And save the relationship for the same child and its second parent.
			if (child != null && roleTypeChild != null && parent2 != null && roleTypeParent2 != null && relationshipType != null) {
				Relationship relationship = new Relationship(child, parent2, roleTypeChild, roleTypeParent2, relationshipType);
				relationship = relationshipRepository.save(relationship);
				logger.info("relationship=" + relationship.toString());
			}
		}
	}

	/**
	 * Determine role type on individual based on gender.
	 * @param individual the individual
	 * @param roleQualifierMale role qualifier if individual is male
	 * @param roleQualifierFemale role qualifier if individual is female
	 * @param roleQualifierOther role qualifier if individual is otherwise
	 * @return RoleType, null if no role was found for the role qualifier passed
	 */
	private RoleType getRoleType(Individual individual,
			String roleQualifierMale, String roleQualifierFemale, String roleQualifierOther) {

		Optional<RoleType> optionalRoleType = Optional.empty();

		String genderType = individual.getGenderType();
		if (genderType.equals(GenderTypes.MALE.getGenderQualifier())) {
			optionalRoleType = roleTypeRepository.findByQualifier(roleQualifierMale);
		} else {
			if (genderType.equals(GenderTypes.FEMALE.getGenderQualifier())) {
				optionalRoleType = roleTypeRepository.findByQualifier(roleQualifierFemale);
			} else {
				optionalRoleType = roleTypeRepository.findByQualifier(roleQualifierOther);
			}
		}
		if (optionalRoleType.isPresent()) {
			return optionalRoleType.get();
		} else {
			return null;
		}
	}



	private String padZeros(String input, int length) {
		int inputLenght = input.length();
		if (inputLenght >= length) {
			return input;
		}

		int padLenght = length - inputLenght;
		String pad = "";
		for (int i = 0; i < padLenght; i++) {
			pad = pad + "0";
		}

		return pad + input;
	}

	/**
	 * Determine the gender type using for using with the GENPTT Dutch gender
	 * identifiers.
	 *
	 * @param field imported string with Dutch gender indication, ie M, V
	 * @return gender identifier
	 */
	private String setGender(String field) {

		String genderIdentifier = null;

		if (field.equals(AppConstants.MALE_NL)) {
			genderIdentifier = GenderTypes.MALE.getGenderQualifier();
		} else {
			if (field.equals(AppConstants.FEMALE_NL)) {
				genderIdentifier = GenderTypes.FEMALE.getGenderQualifier();
			} else {
				genderIdentifier = GenderTypes.UNDEFINED.getGenderQualifier();
			}
		}

		return genderIdentifier;
	}

	/**
	 * Convert a date string to a date object.
	 *
	 * @param dateString date in dd-mm-yyyy format
	 * @return a date if string was parsed successfully, empty if the string was
	 *         null or empty, or null otherwise
	 */
	private Optional<Date> setDate(String dateString) {
		Optional<Date> date = null;
		try {
			if (dateString != null && !dateString.isEmpty()) {
				Date tempDate = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
				date = Optional.of(tempDate);
			} else {
				date = Optional.empty();
			}

		} catch (ParseException e) {
			date = null;
			logger.error(e.getLocalizedMessage());
		}
		return date;
	}

	@Override
	public Boolean verifyUploadedFile(String fileName) {
		// TODO Auto-generated method stub
		return Boolean.TRUE;
	}

	private String emptyToNull(String input) {
		if (input != null && !input.isEmpty()) {
			return input;
		} else {
			return null;
		}
	}

}
