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
import org.bbekker.genealogy.common.AppConstants.Gender_Type;
import org.bbekker.genealogy.common.SystemConstants;
import org.bbekker.genealogy.repository.Gender;
import org.bbekker.genealogy.repository.GenderRepository;
import org.bbekker.genealogy.repository.Individual;
import org.bbekker.genealogy.repository.IndividualRepository;
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
	private GenderRepository genderRepository;

	@Value("${bekker.csv.blacklist}")
	private String EigenCodeBlackList;

	@Override
	public Boolean parseBekkerCsvFile(String fileName) {

		Boolean parseResult = Boolean.FALSE;

		try {
			File uploadedFile = new File(fileName);
			InputStream inputStream = new FileInputStream(uploadedFile);

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			long lineNum = 0;

			Map<Integer, String> headerMappings = null;

			final List<String> blackList = loadBlacklistedLines(EigenCodeBlackList);

			final Map<String, Gender> genderMappings = getGenderMappings();

			while ((line = bufferedReader.readLine()) != null) {

				logger.info("line=" + line);

				if (lineNum == 0) {
					// First line of the csv has the column names.
					headerMappings = importCSVHeaderLine(line);
				} else {
					// Other lines are data lines.
					parseResult = importCSVDataLine(line, headerMappings, blackList, genderMappings);
				}

				lineNum++;

			} // end while

			bufferedReader.close();
			inputStream.close();

			Long numberOfIndividualEntries = individualRepository.count();
			logger.info("Individual entries=" + numberOfIndividualEntries.toString());

		} catch (Exception e) {
			parseResult = Boolean.FALSE;
			logger.error("Parse " + fileName + " failed: ", e.getLocalizedMessage());
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

	private Boolean importCSVDataLine(String line, Map<Integer, String> headerMappings, List<String> blackListedLines,
			Map<String, Gender> genderMappings) {

		final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));

		String eigenCode = SystemConstants.EMPTY_STRING;

		// Principal individual.
		String lastName = SystemConstants.EMPTY_STRING;
		String firstName = SystemConstants.EMPTY_STRING;
		String middleName = SystemConstants.EMPTY_STRING;
		String maidenName = SystemConstants.EMPTY_STRING;
		String familiarName = SystemConstants.EMPTY_STRING;
		Gender gender = null;
		Date birthDate = null;
		String birthPlace = SystemConstants.EMPTY_STRING;
		Date deathDate = null;
		String deathPlace = SystemConstants.EMPTY_STRING;
		String deathCause = SystemConstants.EMPTY_STRING;
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
		Date partnerDeathDate = null;
		String partnerDeathPlace = SystemConstants.EMPTY_STRING;
		String partnerDeathCause = SystemConstants.EMPTY_STRING;
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

				// Principal individual parsing.
				if (fieldName.equals(AppConstants.RNAAM_NL)) {
					familiarName = stripQuotes(field);
					if (familiarName.isEmpty()) {
						familiarName = null;
					}
					logger.debug("familiarName=" + familiarName);
				}

				if (fieldName.equals(AppConstants.VVOEG_NL)) {
					middleName = stripQuotes(field);
					if (middleName.isEmpty()) {
						middleName = null;
					}
					logger.debug("middleName=" + middleName);
				}

				if (fieldName.equals(AppConstants.ANAAM_NL)) {
					lastName = stripQuotes(field);
					if (lastName.isEmpty()) {
						parseOk = false;
					}
					logger.debug("lastName=" + lastName);
				}

				if (fieldName.equals(AppConstants.VNAMEN_NL)) {
					firstName = stripQuotes(field);
					if (firstName.isEmpty()) {
						parseOk = false;
					}
					logger.debug("firstName=" + firstName);
				}

				if (fieldName.equals(AppConstants.GESLACHT_NL)) {
					final String genderType = stripQuotes(field);
					gender = setGender(genderType, genderMappings);
					if (gender != null) {
						logger.debug("gender=" + gender.toString());
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
							// The birth date field has data, only not in proper format, maybe it has just the year
							// or something alike, so do not loose that info and save it to the notes field.
							if (notes.length() > 0) {
								notes = notes + SystemConstants.SPACE;
							}
							notes = notes + AppConstants.GEBDATUM_NL + SystemConstants.EQUALS + birthDateString + SystemConstants.SEMICOLON;
						}
					}
					logger.debug("birthDate=" + birthDate);
				}

				if (fieldName.equals(AppConstants.OVLDATUM_NL)) {
					final String deathDateString = stripQuotes(field);
					final Optional<Date> optionalDeathDate = setDate(deathDateString);
					deathDate = null;
					if (optionalDeathDate != null && optionalDeathDate.isPresent()) {
						deathDate = optionalDeathDate.get();
					}
					logger.debug("deathDate=" + deathDate);
				}

				// Partner individual parsing.
				if (fieldName.equals(AppConstants.PRNAAM_NL)) {
					partnerFamiliarName = stripQuotes(field);
					if (partnerFamiliarName.isEmpty()) {
						partnerFamiliarName = null;
					}
					logger.debug("partnerFamiliarName=" + partnerFamiliarName);
				}

				if (fieldName.equals(AppConstants.PANAAM_NL)) {
					partnerLastName = stripQuotes(field);
					if (partnerLastName.isEmpty()) {
						parsePartnerOk = false;
					}
					logger.debug("partnerLastName=" + partnerLastName);
				}

				if (fieldName.equals(AppConstants.PVNAMEN_NL)) {
					partnerFirstName = stripQuotes(field);
					if (partnerFirstName.isEmpty()) {
						parsePartnerOk = false;
					}
					logger.debug("partnerFirstName=" + partnerFirstName);
				}

				if (fieldName.equals(AppConstants.PVVOEG_NL)) {
					partnerMiddleName = stripQuotes(field);
					if (partnerMiddleName.isEmpty()) {
						partnerMiddleName = null;
					}
					logger.debug("partnerMiddleName=" + partnerMiddleName);
				}

				if (fieldName.equals(AppConstants.PGESLACHT_NL)) {
					final String genderType = stripQuotes(field);
					partnerGender = setGender(genderType, genderMappings);
					if (gender != null) {
						logger.debug("partnerGender=" + partnerGender.toString());
					} else {
						logger.debug("partnerGender=null");
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
							// The partner birth date field has data, only not in proper format, maybe it has just the year
							// or something alike, so do not loose that info and save it to the notes field.
							if (partnerNotes.length() > 0) {
								partnerNotes = partnerNotes + SystemConstants.SPACE;
							}
							partnerNotes = partnerNotes + AppConstants.PGEBDATUM_NL + SystemConstants.EQUALS + partnerBirthDateString + SystemConstants.SEMICOLON;
						}
					}
					logger.debug("partnerBirthDate=" + partnerBirthDate);
				}

				if (fieldName.equals(AppConstants.POVLDATUM_NL)) {
					final String partnerDeathDateString = stripQuotes(field);
					final Optional<Date> optionalPartnerDeathDate = setDate(partnerDeathDateString);
					partnerDeathDate = null;
					if (optionalPartnerDeathDate != null && optionalPartnerDeathDate.isPresent()) {
						partnerDeathDate = optionalPartnerDeathDate.get();
					}
					logger.debug("partnerDeathDate=" + partnerDeathDate);
				}

			}

			position++;
		}

		if (parseOk) {
			Individual individual = new Individual(lastName, firstName, middleName, maidenName, familiarName, gender);
			individual.setBirthDate(birthDate);
			individual.setBirthPlace(birthPlace);
			individual.setDeathDate(deathDate);
			individual.setBirthPlace(deathPlace);
			individual.setDeathCause(deathCause);
			individual.setNotes(notes);
			individual = individualRepository.save(individual);
			logger.info("individual=" + individual.toString());

			if (parsePartnerOk) {
				Individual partnerIndividual = new Individual(partnerLastName, partnerFirstName, partnerMiddleName,
						partnerMaidenName, partnerFamiliarName, partnerGender);
				partnerIndividual.setBirthDate(partnerBirthDate);
				partnerIndividual.setBirthPlace(partnerBirthPlace);
				partnerIndividual.setDeathDate(partnerDeathDate);
				partnerIndividual.setBirthPlace(partnerDeathPlace);
				partnerIndividual.setDeathCause(partnerDeathCause);
				partnerIndividual.setNotes(partnerNotes);
				partnerIndividual = individualRepository.save(partnerIndividual);
				logger.info("partnerIndividual=" + partnerIndividual.toString());
			}
		}

		return Boolean.TRUE;
	}

	/**
	 *
	 * @param field          imported string with Dutch gender indication, ie M, V
	 * @param genderMappings
	 * @return gender object
	 */
	private Gender setGender(String field, Map<String, Gender> genderMappings) {

		Gender gender = null;
		String key = SystemConstants.EMPTY_STRING;

		if (field.equals(AppConstants.MALE_NL)) {
			key = Gender_Type.MALE.name() + SystemConstants.COMMA + AppConstants.ISO639_1_NL;
		} else {
			if (field.equals(AppConstants.FEMALE_NL)) {
				key = Gender_Type.FEMALE.name() + SystemConstants.COMMA + AppConstants.ISO639_1_NL;
			} else {
				key = Gender_Type.UNDEFINED.name() + SystemConstants.COMMA + AppConstants.ISO639_1_NL;
			}
		}
		gender = genderMappings.get(key);

		return gender;
	}

	/**
	 * Build a mapping of gender types per language with their gender object.
	 *
	 * @return a map with as key compound string "gender type,language code", ie
	 *         "MALE,nl", "UNDEFINED,es"
	 */
	private Map<String, Gender> getGenderMappings() {
		Map<String, Gender> genders = new HashMap<String, Gender>();

		Iterable<Gender> allGenders = genderRepository.findAll();
		for (Gender oneGender : allGenders) {
			genders.put(oneGender.getGender() + SystemConstants.COMMA + oneGender.getlanguageCode(), oneGender);
		}

		return genders;
	}

	/**
	 * Convert a date string to a date object.
	 *
	 * @param dateString date in dd-mm-yyy format
	 * @return a date if string was parsed successfully, empty if the string was
	 *         null or empty, or null otherwise
	 */
	private Optional<Date> setDate(String dateString) {
		Optional<Date> date = null;
		try {
			if (dateString != null && !dateString.isEmpty()) {
				Date tempDate = new SimpleDateFormat("dd-mm-yyyy").parse(dateString);
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

}
