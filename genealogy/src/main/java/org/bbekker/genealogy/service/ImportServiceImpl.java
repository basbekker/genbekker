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
import java.util.concurrent.Future;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.AppConstants.EventTypes;
import org.bbekker.genealogy.common.AppConstants.RelationshipTypes;
import org.bbekker.genealogy.common.AppConstants.Roles;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.Event;
import org.bbekker.genealogy.repository.EventRepository;
import org.bbekker.genealogy.repository.EventType;
import org.bbekker.genealogy.repository.EventTypeRepository;
import org.bbekker.genealogy.repository.Gender;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
import org.bbekker.genealogy.repository.Relationship;
import org.bbekker.genealogy.repository.RelationshipRepository;
import org.bbekker.genealogy.repository.RelationshipType;
import org.bbekker.genealogy.repository.RoleType;
import org.bbekker.genealogy.repository.RoleTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class ImportServiceImpl implements ImportService {

	private static final Logger logger = LoggerFactory.getLogger(ImportServiceImpl.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	private IndividualRepository individualRepository;

	@Autowired
	private GenderService genderService;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventTypeRepository eventTypeRepository;

	@Autowired
	private RelationshipRepository relationshipRepository;

	@Autowired
	private RelationshipTypeService relationshipTypeService;

	@Autowired
	private RoleTypeRepository roleTypeRepository;

	@Value("${org.bbekker.genealogy.bekker.csv.blacklist}")
	private String EIGENCODE_BLACKLIST;

	@Override
	@Async
	public Future<Boolean> parseBekkerCsvFile(String fileName) {

		Boolean parseResult = parseBekkerCsvFileLocal(fileName);
		return new AsyncResult<Boolean>(parseResult);
	}

	public Boolean parseBekkerCsvFileLocal(String fileName) {

		Boolean parseResult = Boolean.FALSE;
		long lineNum = 2; // line 2 has data, line 1 has the headers.

		File uploadedFile = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		try {
			uploadedFile = new File(fileName);
			inputStream = new FileInputStream(uploadedFile);

			reader = new BufferedReader(new InputStreamReader(inputStream));

			final Map<String, String> eigenCodeToIdMapping = new HashMap<String, String>(); // (Eigencode, Id)
			final Map<String, String> childToParentMapping = new HashMap<String, String>(); // (childId,
			// parent1Eigencode:parent2Eigencode)

			final List<String> blackList = loadBlacklistedLines(EIGENCODE_BLACKLIST);

			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
					.withQuoteMode(QuoteMode.MINIMAL).withIgnoreHeaderCase().withTrim());

			for (CSVRecord csvRecord : csvParser) {

				logger.debug("line=" + csvRecord.toString());

				parseResult = importCSVRecord(csvRecord, blackList, eigenCodeToIdMapping, childToParentMapping);

				lineNum++;
			}

			csvParser.close();
			reader.close();
			inputStream.close();

			Long numberOfIndividualEntries = individualRepository.count();
			logger.info("Individual entries=" + numberOfIndividualEntries.toString());

			createParentChildRelationships(eigenCodeToIdMapping, childToParentMapping);

			Long numberOfRelationshipEntries = relationshipRepository.count();
			logger.info("Relationship entries=" + numberOfRelationshipEntries.toString());

		} catch (Exception e) {
			parseResult = Boolean.FALSE;
			logger.error("Parse " + fileName + " failed on line " + lineNum + ": ", e.getLocalizedMessage());
			e.printStackTrace();

			try {
				reader.close();
			} catch (Exception re) { }
			try {
				inputStream.close();
			} catch (Exception ie) { }

		}

		return parseResult;
	}

	private List<String> loadBlacklistedLines(String listOfCodes) {
		// Load lines to be skipped as they contain junk, or are not relevant.
		final List<String> blacklist = Arrays.asList(listOfCodes.split(SystemConstants.COMMA));
		return blacklist;
	}

	private Boolean importCSVRecord(CSVRecord csvRecord, List<String> blackListedLines,
			Map<String, String> eigenCodeToIdMapping, Map<String, String> childToParentMapping) {

		String eigenCode = SystemConstants.EMPTY_STRING;

		// Record fields.
		String fatherEigenCode = SystemConstants.EMPTY_STRING;
		String motherEigenCode = SystemConstants.EMPTY_STRING;
		String ref1 = SystemConstants.EMPTY_STRING;
		String ref2 = SystemConstants.EMPTY_STRING;

		// First individual.
		String lastName = SystemConstants.EMPTY_STRING;
		String firstName = SystemConstants.EMPTY_STRING;
		String middleName = SystemConstants.EMPTY_STRING;
		String maidenName = SystemConstants.EMPTY_STRING;
		String familiarName = SystemConstants.EMPTY_STRING;
		Gender gender = null;
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
		Gender partnerGender = null;
		Date partnerBirthDate = null;
		String partnerBirthPlace = SystemConstants.EMPTY_STRING;
		String partnerBirthNote = SystemConstants.EMPTY_STRING;
		Date partnerDeathDate = null;
		String partnerDeathPlace = SystemConstants.EMPTY_STRING;
		String partnerDeathNote = SystemConstants.EMPTY_STRING;
		String partnerNotes = SystemConstants.EMPTY_STRING;

		// Partners events
		Date partnersRelationDate = null;
		String partnersRelationPlace = SystemConstants.EMPTY_STRING;
		String relationshipNote = SystemConstants.EMPTY_STRING;
		Date partnersSeparationDate = null;
		String partnersSeparationPlace = SystemConstants.EMPTY_STRING;
		String separationNote = SystemConstants.EMPTY_STRING;

		// Kids between the two
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

		// Notes
		String note1 = SystemConstants.EMPTY_STRING;
		String note2 = SystemConstants.EMPTY_STRING;
		String noteH = SystemConstants.EMPTY_STRING;

		boolean parseOk = true;
		boolean parsePartnerOk = true;
		boolean blackListed = false;

		eigenCode = nullToEmpty(csvRecord.get(AppConstants.EIGENCODE_NL));
		logger.debug("eigenCode=" + eigenCode);
		// Some lines are to be skipped for being junk, irrelevant etc.
		if (blackListedLines.contains(eigenCode)) {
			blackListed = true;
			parseOk = false;
			logger.debug("record " + csvRecord.getRecordNumber() + " skipped - blacklisted " + eigenCode);
		}

		if (!blackListed && parseOk) {

			fatherEigenCode = nullToEmpty(csvRecord.get(AppConstants.VADERCODE_NL));
			motherEigenCode = nullToEmpty(csvRecord.get(AppConstants.MOEDER_NL));
			motherEigenCode = fatherEigenCode + motherEigenCode;
			fatherEigenCode = fatherEigenCode + "Z";

			ref1 = nullToEmpty(csvRecord.get(AppConstants.REF1_NL));
			ref2 = nullToEmpty(csvRecord.get(AppConstants.REF2_NL));

			note1 = nullToEmpty(csvRecord.get(AppConstants.OPM1_NL));
			note2 = nullToEmpty(csvRecord.get(AppConstants.OPM2_NL));
			noteH = nullToEmpty(csvRecord.get(AppConstants.OPMH_NL));

			kind1 = nullToEmpty(csvRecord.get(AppConstants.KIND1_NL));
			kind2 = nullToEmpty(csvRecord.get(AppConstants.KIND2_NL));
			kind3 = nullToEmpty(csvRecord.get(AppConstants.KIND3_NL));
			kind4 = nullToEmpty(csvRecord.get(AppConstants.KIND4_NL));
			kind5 = nullToEmpty(csvRecord.get(AppConstants.KIND5_NL));
			kind6 = nullToEmpty(csvRecord.get(AppConstants.KIND6_NL));
			kind7 = nullToEmpty(csvRecord.get(AppConstants.KIND7_NL));
			kind8 = nullToEmpty(csvRecord.get(AppConstants.KIND8_NL));
			kind9 = nullToEmpty(csvRecord.get(AppConstants.KIND9_NL));
			kind10 = nullToEmpty(csvRecord.get(AppConstants.KIND10_NL));
			kind11 = nullToEmpty(csvRecord.get(AppConstants.KIND11_NL));
			kind12 = nullToEmpty(csvRecord.get(AppConstants.KIND12_NL));
			kind13 = nullToEmpty(csvRecord.get(AppConstants.KIND13_NL));
			kind14 = nullToEmpty(csvRecord.get(AppConstants.KIND14_NL));
			kind15 = nullToEmpty(csvRecord.get(AppConstants.KIND15_NL));
			kind16 = nullToEmpty(csvRecord.get(AppConstants.KIND16_NL));
			kind17 = nullToEmpty(csvRecord.get(AppConstants.KIND17_NL));
			kind18 = nullToEmpty(csvRecord.get(AppConstants.KIND18_NL));

			// Principal individual.
			lastName = nullToEmpty(csvRecord.get(AppConstants.ANAAM_NL));
			if (lastName.isEmpty()) {
				parseOk = false;
			}
			firstName = nullToEmpty(csvRecord.get(AppConstants.VNAMEN_NL));
			if (firstName.isEmpty()) {
				parseOk = false;
			}
			familiarName = nullToEmpty(csvRecord.get(AppConstants.RNAAM_NL));
			middleName = nullToEmpty(csvRecord.get(AppConstants.VVOEG_NL));
			final String importGenderType = nullToEmpty(csvRecord.get(AppConstants.GESLACHT_NL));
			gender = setGender(importGenderType);

			final String birthDateString = nullToEmpty(csvRecord.get(AppConstants.GEBDATUM_NL));
			final Optional<Date> optionalBirthDate = setDate(birthDateString);
			birthDate = null;
			if (optionalBirthDate != null && optionalBirthDate.isPresent()) {
				birthDate = optionalBirthDate.get();
			} else {
				if (birthDateString != null && !birthDateString.isEmpty()) {
					// The birth date field has data, only not in proper format, maybe it has just
					// the year or something alike, so do not loose that info and save it to the
					// notes field.
					if (birthNote.length() > 0) {
						birthNote = birthNote + SystemConstants.SPACE;
					}
					birthNote = birthNote + AppConstants.GEBDATUM_NL + SystemConstants.EQUALS + birthDateString
							+ SystemConstants.SEMICOLON;
				}
			}

			birthPlace = mergeAddress(nullToEmpty(csvRecord.get(AppConstants.GEBPLTS_NL)),
					nullToEmpty(csvRecord.get(AppConstants.GEBPROV_NL)),
					nullToEmpty(csvRecord.get(AppConstants.GEBLAND_NL)));

			final String deathDateString = nullToEmpty(csvRecord.get(AppConstants.OVLDATUM_NL));
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
					deathNote = deathNote + AppConstants.OVLDATUM_NL + SystemConstants.EQUALS + deathDateString
							+ SystemConstants.SEMICOLON;
				}
			}

			// Partner individual parsing.
			partnerLastName = nullToEmpty(csvRecord.get(AppConstants.PANAAM_NL));
			if (partnerLastName.isEmpty()) {
				parsePartnerOk = false;
			}
			partnerFirstName = nullToEmpty(csvRecord.get(AppConstants.PVNAMEN_NL));
			if (partnerFirstName.isEmpty()) {
				parsePartnerOk = false;
			}
			partnerFamiliarName = nullToEmpty(csvRecord.get(AppConstants.PRNAAM_NL));
			partnerMiddleName = nullToEmpty(csvRecord.get(AppConstants.PVVOEG_NL));
			final String importPartnerGenderType = nullToEmpty(csvRecord.get(AppConstants.PGESLACHT_NL));
			partnerGender = setGender(importPartnerGenderType);

			final String partnerBirthDateString = nullToEmpty(csvRecord.get(AppConstants.PGEBDATUM_NL));
			final Optional<Date> optionalPartnerBirthDate = setDate(partnerBirthDateString);
			partnerBirthDate = null;
			if (optionalPartnerBirthDate != null && optionalPartnerBirthDate.isPresent()) {
				partnerBirthDate = optionalPartnerBirthDate.get();
			} else {
				if (partnerBirthDateString != null && !partnerBirthDateString.isEmpty()) {
					// The partner birth date field has data, only not in proper format, maybe it
					// has just
					// the year or something alike, so do not loose that info and save it to the
					// notes field.
					if (partnerBirthNote.length() > 0) {
						partnerBirthNote = partnerBirthNote + SystemConstants.SPACE;
					}
					partnerBirthNote = partnerBirthNote + AppConstants.PGEBDATUM_NL + SystemConstants.EQUALS
							+ partnerBirthDateString + SystemConstants.SEMICOLON;
				}
			}

			partnerBirthPlace = mergeAddress(nullToEmpty(csvRecord.get(AppConstants.PGEBPLTS_NL)),
					nullToEmpty(csvRecord.get(AppConstants.PGEBPLTS_NL)),
					nullToEmpty(csvRecord.get(AppConstants.PGEBLAND_NL)));

			final String partnerDeathDateString = nullToEmpty(csvRecord.get(AppConstants.POVLDATUM_NL));
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
					partnerDeathNote = partnerDeathNote + AppConstants.POVLDATUM_NL + SystemConstants.EQUALS
							+ partnerDeathDateString + SystemConstants.SEMICOLON;
				}
			}

			// Partner event: a relation
			final String partnersRelationDateString = nullToEmpty(csvRecord.get(AppConstants.HUWDATUM_NL));
			final Optional<Date> optionalpartnersRelationDate = setDate(partnersRelationDateString);
			partnersRelationDate = null;
			if (optionalpartnersRelationDate != null && optionalpartnersRelationDate.isPresent()) {
				partnersRelationDate = optionalpartnersRelationDate.get();
			} else {
				if (partnersRelationDateString != null && !partnersRelationDateString.isEmpty()) {
					// The partners relation (marriage?) date field has data, only not in proper
					// format, maybe it
					// has just the year
					// or something alike, so do not loose that info and save it to the notes field.
					if (relationshipNote.length() > 0) {
						relationshipNote = relationshipNote + SystemConstants.SPACE;
					}
					relationshipNote = relationshipNote + AppConstants.HUWDATUM_NL + SystemConstants.EQUALS
							+ partnersRelationDateString + SystemConstants.SEMICOLON;
				}
			}

			partnersRelationPlace = mergeAddress(nullToEmpty(csvRecord.get(AppConstants.HUWPLTS_NL)),
					nullToEmpty(csvRecord.get(AppConstants.HUWPROV_NL)),
					nullToEmpty(csvRecord.get(AppConstants.HUWLAND_NL)));

			// Partner event: a separation
			final String partnersSeparationDateString = nullToEmpty(csvRecord.get(AppConstants.SCHDATUM_NL));
			final Optional<Date> optionalPartnersSeparationDate = setDate(partnersSeparationDateString);
			partnersSeparationDate = null;
			if (optionalPartnersSeparationDate != null && optionalPartnersSeparationDate.isPresent()) {
				partnersSeparationDate = optionalPartnersSeparationDate.get();
			} else {
				if (partnersSeparationDateString != null && !partnersSeparationDateString.isEmpty()) {
					// The partners relation (marriage?) date field has data, only not in proper
					// format, maybe it has just the year
					// or something alike, so do not loose that info and save it to the notes field.
					if (separationNote.length() > 0) {
						separationNote = separationNote + SystemConstants.SPACE;
					}
					separationNote = separationNote + AppConstants.SCHDATUM_NL + SystemConstants.EQUALS
							+ partnersSeparationDateString + SystemConstants.SEMICOLON;
				}
			}

			partnersSeparationPlace = mergeAddress(nullToEmpty(csvRecord.get(AppConstants.SCHPLTS_NL)),
					nullToEmpty(csvRecord.get(AppConstants.SCHPROV_NL)),
					nullToEmpty(csvRecord.get(AppConstants.SCHLAND_NL)));
		}

		if (parseOk) {

			// Check if the individual already exits.
			Individual individual = null;
			String individualId = eigenCodeToIdMapping.get(ref1);
			if (individualId != null) {
				Optional<Individual> optionalIndividual = individualRepository.findById(individualId);
				if (optionalIndividual.isPresent()) {
					individual = optionalIndividual.get();

					// TODO we should do some name comparisons here between stored and csv entries,
					// but for now only we append the note field.
					String tmpNote = individual.getNote();
					if (tmpNote != null && !tmpNote.isEmpty()) {
						notes = tmpNote + "; " + notes;
					}
					individual.setNote(note1 + ";" + note2 + ";" + notes);
					individual = individualRepository.save(individual);
				}
			} else {
				individual = new Individual(lastName, firstName, middleName, maidenName, familiarName, gender);
				individual
				.setNote((note1.isEmpty() ? SystemConstants.EMPTY_STRING
						: AppConstants.OPM1_NL + SystemConstants.EQUALS + note1 + SystemConstants.SEMICOLON
						+ SystemConstants.SPACE)
						+ (note2.isEmpty() ? SystemConstants.EMPTY_STRING
								: AppConstants.OPM2_NL + SystemConstants.EQUALS + note2
								+ SystemConstants.SEMICOLON + SystemConstants.SPACE)
						+ (noteH.isEmpty() ? SystemConstants.EMPTY_STRING
								: AppConstants.OPMH_NL + SystemConstants.EQUALS + noteH
								+ SystemConstants.SEMICOLON + SystemConstants.SPACE)
						+ notes);
				individual = individualRepository.save(individual);

				// only if the individual did not exist already, add birth/death dates,
				// otherwise there is the risk of duplicating those dates in the db.
				if (birthDate != null || (birthNote != null && !birthNote.isEmpty())) {
					EventType birthEventType = eventTypeRepository
							.findByQualifier(EventTypes.BIRTH.getQualifier());
					Event birthEvent = new Event(individual, birthEventType, birthDate);
					birthEvent.setEventPlace(birthPlace);
					birthEvent.setEventNote(birthNote);
					birthEvent = eventRepository.save(birthEvent);
				}

				if (deathDate != null || (deathNote != null && !deathNote.isEmpty())) {
					EventType deathEventType = eventTypeRepository
							.findByQualifier(EventTypes.DEATH.getQualifier());
					Event deathEvent = new Event(individual, deathEventType, deathDate);
					deathEvent.setEventPlace(deathPlace);
					deathEvent.setEventNote(deathNote);
					deathEvent = eventRepository.save(deathEvent);
				}

				if (ref1 != null) {
					eigenCodeToIdMapping.put(ref1, individual.getId());
				}
			}
			logger.debug("individual=" + individual.toString());

			if (partnersRelationDate != null || (relationshipNote != null && !relationshipNote.isEmpty())) {
				EventType partnerEventType = eventTypeRepository
						.findByQualifier(EventTypes.MARRIAGE.getQualifier());
				Event partnerEvent = new Event(individual, partnerEventType, partnersRelationDate);
				partnerEvent.setEventPlace(partnersRelationPlace);
				partnerEvent.setEventNote(relationshipNote);
				partnerEvent = eventRepository.save(partnerEvent);
			}

			if (partnersSeparationDate != null || (separationNote != null && !separationNote.isEmpty())) {
				EventType partnerEventType = eventTypeRepository
						.findByQualifier(EventTypes.DIVORCE.getQualifier());
				Event partnerEvent = new Event(individual, partnerEventType, partnersSeparationDate);
				partnerEvent.setEventPlace(partnersSeparationPlace);
				partnerEvent.setEventNote(separationNote);
				partnerEvent = eventRepository.save(partnerEvent);
			}

			if (parsePartnerOk) {

				// Not checking if the partner individual already exists, should not be the
				// case,
				// the partner should be of a second marriage.
				Individual partnerIndividual = new Individual(partnerLastName, partnerFirstName, partnerMiddleName,
						partnerMaidenName, partnerFamiliarName, partnerGender);
				partnerIndividual.setNote(partnerNotes);
				partnerIndividual = individualRepository.save(partnerIndividual);
				logger.debug("partnerIndividual=" + partnerIndividual.toString());

				if (ref2 != null) {
					eigenCodeToIdMapping.put(ref2, partnerIndividual.getId());
				}

				// Add to mapping to later on add parent-child relationships.
				if (kind1 != null) {
					childToParentMapping.put(kind1, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind2 != null) {
					childToParentMapping.put(kind2, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind3 != null) {
					childToParentMapping.put(kind3, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind4 != null) {
					childToParentMapping.put(kind4, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind5 != null) {
					childToParentMapping.put(kind5, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind6 != null) {
					childToParentMapping.put(kind6, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind7 != null) {
					childToParentMapping.put(kind7, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind8 != null) {
					childToParentMapping.put(kind8, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind9 != null) {
					childToParentMapping.put(kind9, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind10 != null) {
					childToParentMapping.put(kind10, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind11 != null) {
					childToParentMapping.put(kind11, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind12 != null) {
					childToParentMapping.put(kind12, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind13 != null) {
					childToParentMapping.put(kind13, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind14 != null) {
					childToParentMapping.put(kind14, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind15 != null) {
					childToParentMapping.put(kind15, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind16 != null) {
					childToParentMapping.put(kind16, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind17 != null) {
					childToParentMapping.put(kind17, individual.getId() + ":" + partnerIndividual.getId());
				}
				if (kind18 != null) {
					childToParentMapping.put(kind18, individual.getId() + ":" + partnerIndividual.getId());
				}

				if (partnerBirthDate != null || (partnerBirthNote != null && !partnerBirthNote.isEmpty())) {
					EventType birthEventType = eventTypeRepository
							.findByQualifier(EventTypes.BIRTH.getQualifier());
					Event birthEvent = new Event(partnerIndividual, birthEventType, partnerBirthDate);
					birthEvent.setEventPlace(partnerBirthPlace);
					birthEvent.setEventNote(partnerBirthNote);
					birthEvent = eventRepository.save(birthEvent);
					logger.debug("birthEvent=" + birthEvent.toString());
				}

				if (partnerDeathDate != null || (partnerDeathNote != null && !partnerDeathNote.isEmpty())) {
					EventType deathEventType = eventTypeRepository
							.findByQualifier(EventTypes.DEATH.getQualifier());
					Event deathEvent = new Event(partnerIndividual, deathEventType, partnerDeathDate);
					deathEvent.setEventPlace(partnerDeathPlace);
					deathEvent.setEventNote(partnerDeathNote);
					deathEvent = eventRepository.save(deathEvent);
					logger.debug("deathEvent=" + deathEvent.toString());
				}

				if (partnersRelationDate != null || (relationshipNote != null && !relationshipNote.isEmpty())) {
					EventType partnerEventType = eventTypeRepository
							.findByQualifier(EventTypes.MARRIAGE.getQualifier());
					Event partnerEvent = new Event(partnerIndividual, partnerEventType, partnersRelationDate);
					partnerEvent.setEventPlace(partnersRelationPlace);
					partnerEvent.setEventNote(relationshipNote);
					partnerEvent = eventRepository.save(partnerEvent);
					logger.debug("partnerEvent=" + partnerEvent.toString());
				}

				if (partnersSeparationDate != null || (separationNote != null && !separationNote.isEmpty())) {
					EventType partnerEventType = eventTypeRepository
							.findByQualifier(EventTypes.DIVORCE.getQualifier());
					Event partnerEvent = new Event(partnerIndividual, partnerEventType, partnersSeparationDate);
					partnerEvent.setEventPlace(partnersSeparationPlace);
					partnerEvent.setEventNote(separationNote);
					partnerEvent = eventRepository.save(partnerEvent);
					logger.debug("partnerEvent=" + partnerEvent.toString());
				}

				// The relationship is husband-wife.
				RoleType partner1RoleType = getRoleType(individual, Roles.HUSBAND.getQualifier(),
						Roles.WIFE.getQualifier(), Roles.HUSBAND.getQualifier());
				RoleType partner2RoleType = getRoleType(partnerIndividual, Roles.HUSBAND.getQualifier(),
						Roles.WIFE.getQualifier(), Roles.HUSBAND.getQualifier());

				RelationshipType relationshipType =
						relationshipTypeService.getRelationshipTypeByQualifier(RelationshipTypes.MARRIED.getQualifier());

				if (partner1RoleType != null && partner2RoleType != null && relationshipType != null) {
					Relationship relationship = new Relationship(individual, partnerIndividual, partner1RoleType,
							partner2RoleType, relationshipType);
					relationship.setNote(relationshipNote);
					relationship = relationshipRepository.save(relationship);
					logger.debug("relationship=" + relationship.toString());
				}
			}
		}

		return Boolean.TRUE;
	}

	private String mergeAddress(String town, String province, String country) {
		String address = (!town.isEmpty() ? town + SystemConstants.COMMA + SystemConstants.SPACE
				: SystemConstants.EMPTY_STRING)
				+ (!province.isEmpty() ? province + SystemConstants.COMMA + SystemConstants.SPACE
						: SystemConstants.EMPTY_STRING)
				+ (!country.isEmpty() ? country + SystemConstants.COMMA + SystemConstants.SPACE
						: SystemConstants.EMPTY_STRING);
		return address;
	}

	private void createParentChildRelationships(Map<String, String> eigenCodeToIdMapping,
			Map<String, String> childToParentMapping) {

		for (Map.Entry<String, String> entry : childToParentMapping.entrySet()) {

			// The eigenCode is a 5 digit string, which should be left-padded with zeros.
			String childEigenCode = entry.getKey();
			String childId = null;
			if (childEigenCode != null && !childEigenCode.isEmpty()) {
				childEigenCode = padZeros(childEigenCode, 5) + "Z";
				childId = eigenCodeToIdMapping.get(childEigenCode);
			}

			String parentIds = entry.getValue();
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
					roleTypeParent1 = getRoleType(parent1, Roles.FATHER.getQualifier(),
							Roles.MOTHER.getQualifier(), Roles.FATHER.getQualifier());
				}
			}
			if (parent2Id != null && !parent2Id.isEmpty()) {
				Optional<Individual> optionalParent2 = individualRepository.findById(parent2Id);
				if (optionalParent2.isPresent()) {
					parent2 = optionalParent2.get();
					roleTypeParent2 = getRoleType(parent2, Roles.FATHER.getQualifier(),
							Roles.MOTHER.getQualifier(), Roles.FATHER.getQualifier());
				}
			}
			if (childId != null && !childId.isEmpty()) {
				Optional<Individual> optionalChild = individualRepository.findById(childId);
				if (optionalChild.isPresent()) {
					child = optionalChild.get();
					// Get the role of the child (son or daughter)
					roleTypeChild = getRoleType(child, Roles.SON.getQualifier(), Roles.DAUGHTER.getQualifier(),
							Roles.SON.getQualifier());
				}
			}

			// The relationship is parent-child.
			RelationshipType relationshipType =
					relationshipTypeService.getRelationshipTypeByQualifier(RelationshipTypes.PARENT_CHILD.getQualifier());

			// Finally save the relationship of the child and its first parent.
			if (child != null && roleTypeChild != null && parent1 != null && roleTypeParent1 != null
					&& relationshipType != null) {
				Relationship relationship = new Relationship(child, parent1, roleTypeChild, roleTypeParent1,
						relationshipType);
				relationship = relationshipRepository.save(relationship);
				logger.warn("relationship=" + relationship.toString());
			}

			// And save the relationship for the same child and its second parent.
			if (child != null && roleTypeChild != null && parent2 != null && roleTypeParent2 != null
					&& relationshipType != null) {
				Relationship relationship = new Relationship(child, parent2, roleTypeChild, roleTypeParent2,
						relationshipType);
				relationship = relationshipRepository.save(relationship);
				logger.info("relationship=" + relationship.toString());
			}
		}

	}

	/**
	 * Determine role type on individual based on gender.
	 *
	 * @param individual          the individual
	 * @param roleQualifierMale   role qualifier if individual is male
	 * @param roleQualifierFemale role qualifier if individual is female
	 * @param roleQualifierOther  role qualifier if individual is otherwise
	 * @return RoleType, null if no role was found for the role qualifier passed
	 */
	private RoleType getRoleType(Individual individual, String roleQualifierMale, String roleQualifierFemale,
			String roleQualifierOther) {

		Optional<RoleType> optionalRoleType = Optional.empty();

		String genderType = individual.getGender().getQualifier();
		if (genderType.equals(AppConstants.GenderTypes.MALE.getQualifier())) {
			optionalRoleType = roleTypeRepository.findByQualifier(roleQualifierMale);
		} else {
			if (genderType.equals(AppConstants.GenderTypes.FEMALE.getQualifier())) {
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
	private Gender setGender(String field) {

		Gender gender = null;

		if (field.equals(AppConstants.MALE_NL)) {
			gender = genderService.getMale();
		} else {
			if (field.equals(AppConstants.FEMALE_NL)) {
				gender = genderService.getFemale();
			} else {
				gender = genderService.getUndefined();
			}
		}

		return gender;
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
			logger.debug(e.getLocalizedMessage());
		}
		return date;
	}

	private String nullToEmpty(String input) {
		if (input == null) {
			return SystemConstants.EMPTY_STRING;
		} else {
			return input;
		}
	}
}
