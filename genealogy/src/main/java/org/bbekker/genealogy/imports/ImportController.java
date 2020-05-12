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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImportController {

	@Autowired
	MessageSource messageSource;

	@Autowired
	private IndividualRepository individualRepository;

	@Autowired
	private GenderRepository genderRepository;

	@RequestMapping(path = "/import/*", method = RequestMethod.GET)
	public String doImports() {

		try {
			File uploadedFile = new File(AppConstants.UPLOAD_FOLDER + "BEKKER.csv");
			InputStream inputStream = new FileInputStream(uploadedFile);

			//final Resource resource = new ClassPathResource(AppConstants.UPLOAD_FOLDER + "BEKKER.csv");
			//final InputStream inputStream = resource.getInputStream();

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = null;
			long lineNum = 0;
			Map<Integer, String> hmap = new HashMap<Integer, String>();

			while ((line = bufferedReader.readLine()) != null) {

				if (lineNum == 0) {

					// "PROJNR","EIGENCODE","HUWNR","HUWTYPE","XHUW","VADERCODE","MOEDER","REF1","REF2","REFH","RNAAM","TITEL","INITS5","VNAMEN","VVOEG","ANAAM","NAAMGRP","NAAMCODE","GESLACHT","BEROEP","GEBDP","GEBDATUM","GEBPLTS","GEBPROV","GEBLAND","GEBAKTDAT","GEBGEM","GEBARCH","GEBREG","GEBFOL","GEBAKTE","GEBGEZDTE","GEBBRON","OVLBG","OVLDATUM","OVLPLTS","OVLPROV","OVLLAND","OVLAKTDAT","OVLGEM","OVLARCH","OVLREG","OVLFOL","OVLAKTE","OVLBRON","PRNAAM","PTITEL","PVNAMEN","PVVOEG","PANAAM","PNAAMGRP","PGESLACHT","PBEROEP","PGEBDP","PGEBDATUM","PGEBPLTS","PGEBPROV","PGEBLAND","PGEBAKTDAT","PGEBGEM","PGEBARCH","PGEBREG","PGEBFOL","PGEBAKTE","PGEBBRON","PGEBGEZDTE","POVLBG","POVLDATUM","POVLPLTS","POVLPROV","POVLLAND","POVLAKTDAT","POVLGEM","POVLARCH","POVLREG","POVLFOL","POVLAKTE","POVLBRON","HUWTR","HUWDATUM","HUWPLTS","HUWPROV","HUWLAND","HUWAKTDAT","HUWGEM","HUWARCH","HUWREG","HUWFOL","HUWAKTE","HUWGEZDTE","HUWBRON","SCHRB","SCHDATUM","SCHPLTS","SCHPROV","SCHLAND","SCHAKTE","SCHAKTDAT","SCHGEM","SCHARCH","SCHREG","SCHFOL","SCHBRON","OPM1","OPM2","OPMH","KIND1","KIND2","KIND3","KIND4","KIND5","KIND6","KIND7","KIND8","KIND9","KIND10","KIND11","KIND12","KIND13","KIND14","KIND15","KIND16","KIND17","KIND18","PRIVACY","RELATIE"
					final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));
					int position = 0;

					for (String fieldName : lineList) {
						hmap.put(position, stripQuotes(fieldName));
						position++;
					}

				} else {

					final List<String> lineList = Arrays.asList(line.split(SystemConstants.COMMA));

					int position = 0;
					boolean parseOk = true;

					String lastName = AppConstants.EMPTY_STRING;
					String firstName = AppConstants.EMPTY_STRING;
					String middleName = AppConstants.EMPTY_STRING;
					String maidenName = AppConstants.EMPTY_STRING;
					String familiarName = AppConstants.EMPTY_STRING;
					Gender gender = null;

					for (String field : lineList) {

						final String fieldName = hmap.get(position);

						if (fieldName.equals(AppConstants.ANAAM_NL)) {
							lastName = stripQuotes(field);
						}

						if (fieldName.equals(AppConstants.VNAMEN_NL)) {
							firstName = stripQuotes(field);
						}

						if (fieldName.equals(AppConstants.VVOEG_NL)) {
							middleName = stripQuotes(field);
						}

						if (fieldName.equals(AppConstants.GESLACHT_NL)) {
							final String genderType = stripQuotes(field);
							Iterable<Gender> allGenders = genderRepository.findAll();
							for (Gender oneGender : allGenders) {
								if (genderType.equals(AppConstants.MALE_NL) && oneGender.getGender().equals(Gender_Type.MALE.name()) && oneGender.getlanguageCode().equals("nl")) {
									gender = oneGender;
									break;
								} else {
									if (genderType.equals(AppConstants.FEMALE_NL) && oneGender.getGender().equals(Gender_Type.FEMALE.name()) && oneGender.getlanguageCode().equals("nl")) {
										gender = oneGender;
										break;
									} else {
										if (genderType.isEmpty() && oneGender.getGender().equals(Gender_Type.UNDEFINED.name()) && oneGender.getlanguageCode().equals("nl")) {
											gender = oneGender;
											break;
										}
									}
								}
							}
						}

						position++;
					}

					if (parseOk) {
						Individual individual = new Individual(lastName, firstName, middleName, maidenName, familiarName, gender);
						individual = individualRepository.save(individual);
					}

				}

				lineNum++;

			} // end while

			bufferedReader.close();
			inputStream.close();

		} catch (Exception e) {
			// do nothing yet
		}

		return "doImport";
	}

	private String stripQuotes(String field) {
		return field.replaceAll("\"", "").trim();
	}

}
