package org.bbekker.genealogy.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportController {

	Logger logger = LoggerFactory.getLogger(ImportController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	private IndividualRepository individualRepository;

	@Autowired
	private GenderRepository genderRepository;

	@Value("${bekker.csv.blacklist}")
	private String EigenCodeBlackList;

	@RequestMapping(path = "/import", method = RequestMethod.GET)
	public String doImports() {

		Boolean result = Boolean.FALSE;

		try {
			File uploadedFile = new File(AppConstants.UPLOAD_FOLDER + AppConstants.BEKKER_CSV_NAME);
			InputStream inputStream = new FileInputStream(uploadedFile);

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			long lineNum = 0;

			Map<Integer, String> headerMappings = null;

			final List<String> blackList = loadBlacklistedLines(EigenCodeBlackList);

			while ((line = bufferedReader.readLine()) != null) {

				logger.info("line=" + line);

				if (lineNum == 0) {
					// First line of the csv has the column names.
					headerMappings = importCSVHeaderLine(line);
				} else {
					// Other lines are data lines.
					result = importCSVDataLine(line, headerMappings, blackList);
				}

				lineNum++;

			} // end while

			bufferedReader.close();
			inputStream.close();

		} catch (Exception e) {
			// do nothing yet
		}

		return "doImport=" + result.toString();
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

	private Boolean importCSVDataLine(String line, Map<Integer, String> headerMappings, List<String> blackListedLines) {

		final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));

		int position = 0;
		boolean parseOk = true;

		String eigenCode = SystemConstants.EMPTY_STRING;

		// Principal individual.
		String lastName = SystemConstants.EMPTY_STRING;
		String firstName = SystemConstants.EMPTY_STRING;
		String middleName = SystemConstants.EMPTY_STRING;
		String maidenName = SystemConstants.EMPTY_STRING;
		String familiarName = SystemConstants.EMPTY_STRING;
		Gender gender = null;

		// Partner individual.
		String partnerLastName = SystemConstants.EMPTY_STRING;
		String partnerFirstName = SystemConstants.EMPTY_STRING;
		String partnerMiddleName = SystemConstants.EMPTY_STRING;
		String partnerMaidenName = SystemConstants.EMPTY_STRING;
		String partnerFamiliarName = SystemConstants.EMPTY_STRING;
		Gender partnerGender = null;

		for (String field : lineList) {

			final String fieldName = headerMappings.get(position);

			if (fieldName.equals(AppConstants.EIGENCODE_NL)) {
				eigenCode = stripQuotes(field);
				// Some lines are to be skipped for being junk, irrelevant etc.
				if (blackListedLines.contains(eigenCode)) {
					parseOk = false;
				}
			}

			// Principal individual parsing.
			if (fieldName.equals(AppConstants.ANAAM_NL)) {
				lastName = stripQuotes(field);
				logger.info("lastName=" + lastName);
			}

			if (fieldName.equals(AppConstants.VNAMEN_NL)) {
				firstName = stripQuotes(field);
			}

			if (fieldName.equals(AppConstants.VVOEG_NL)) {
				middleName = stripQuotes(field);
			}

			if (fieldName.equals(AppConstants.GESLACHT_NL)) {
				final String genderType = stripQuotes(field);
				gender = setGender(genderType);
			}

			// Partner individual parsing.
			if (fieldName.equals(AppConstants.PANAAM_NL)) {
				partnerLastName = stripQuotes(field);
			}

			if (fieldName.equals(AppConstants.PVNAMEN_NL)) {
				partnerFirstName = stripQuotes(field);
			}

			if (fieldName.equals(AppConstants.PVVOEG_NL)) {
				partnerMiddleName = stripQuotes(field);
			}

			if (fieldName.equals(AppConstants.PGESLACHT_NL)) {
				final String genderType = stripQuotes(field);
				partnerGender = setGender(genderType);
			}

			position++;
		}

		if (parseOk) {
			Individual individual = new Individual(lastName, firstName, middleName, maidenName, familiarName, gender);
			individual = individualRepository.save(individual);

			Individual partnerIndividual = new Individual(partnerLastName, partnerFirstName, partnerMiddleName,
					partnerMaidenName, partnerFamiliarName, partnerGender);
			partnerIndividual = individualRepository.save(partnerIndividual);
		}

		return Boolean.TRUE;
	}

	private Gender setGender(String field) {
		Gender gender = null;

		List<Gender> foundGenders = genderRepository.findByTypeAndLanguage(field.toUpperCase(), AppConstants.ISO639_1_NL);
		if (foundGenders.size() > 0) {
			Gender foundGender = foundGenders.get(0);
			String debug = foundGender.toString();
		}

		Iterable<Gender> allGenders = genderRepository.findAll();
		for (Gender oneGender : allGenders) {
			if (field.equals(AppConstants.MALE_NL) && oneGender.getGender().equals(Gender_Type.MALE.name())
					&& oneGender.getlanguageCode().equals(AppConstants.ISO639_1_NL)) {
				gender = oneGender;
				break;
			} else {
				if (field.equals(AppConstants.FEMALE_NL)
						&& oneGender.getGender().equals(Gender_Type.FEMALE.name())
						&& oneGender.getlanguageCode().equals(AppConstants.ISO639_1_NL)) {
					gender = oneGender;
					break;
				} else {
					if (field.isEmpty() && oneGender.getGender().equals(Gender_Type.UNDEFINED.name())
							&& oneGender.getlanguageCode().equals(AppConstants.ISO639_1_NL)) {
						gender = oneGender;
						break;
					}
				}
			}
		}

		return gender;
	}

}
