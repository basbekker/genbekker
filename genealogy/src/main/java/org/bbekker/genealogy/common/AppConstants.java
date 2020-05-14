package org.bbekker.genealogy.common;

import org.springframework.beans.factory.annotation.Value;

public class AppConstants {

	@Value("${upload.folder}")
	public static final String UPLOAD_FOLDER = "C://temp//";

	public static final String BASE_NAME_CSV_PATH = "data/BaseName.csv";
	public static final String BASE_NAME_PREFIX_CSV_PATH = "data/BaseNamePrefix.csv";

	public static final String GENDER_CSV_PATH = "data/Gender.csv";

	public enum Gender_Type {
		MALE,
		FEMALE,
		UNDEFINED
	}

	public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		for (E e : enumClass.getEnumConstants()) {
			if(e.name().equals(value)) { return true; }
		}
		return false;
	}

	public static final String EMPTY_STRING = "";

	public static final String MALE_NL = "M";
	public static final String FEMALE_NL = "V";

	// "PROJNR","EIGENCODE","HUWNR","HUWTYPE","XHUW","VADERCODE","MOEDER","REF1","REF2","REFH","RNAAM","TITEL","INITS5","VNAMEN","VVOEG","ANAAM","NAAMGRP","NAAMCODE","GESLACHT","BEROEP","GEBDP","GEBDATUM","GEBPLTS","GEBPROV","GEBLAND","GEBAKTDAT","GEBGEM","GEBARCH","GEBREG","GEBFOL","GEBAKTE","GEBGEZDTE","GEBBRON","OVLBG","OVLDATUM","OVLPLTS","OVLPROV","OVLLAND","OVLAKTDAT","OVLGEM","OVLARCH","OVLREG","OVLFOL","OVLAKTE","OVLBRON","PRNAAM","PTITEL","PVNAMEN","PVVOEG","PANAAM","PNAAMGRP","PGESLACHT","PBEROEP","PGEBDP","PGEBDATUM","PGEBPLTS","PGEBPROV","PGEBLAND","PGEBAKTDAT","PGEBGEM","PGEBARCH","PGEBREG","PGEBFOL","PGEBAKTE","PGEBBRON","PGEBGEZDTE","POVLBG","POVLDATUM","POVLPLTS","POVLPROV","POVLLAND","POVLAKTDAT","POVLGEM","POVLARCH","POVLREG","POVLFOL","POVLAKTE","POVLBRON","HUWTR","HUWDATUM","HUWPLTS","HUWPROV","HUWLAND","HUWAKTDAT","HUWGEM","HUWARCH","HUWREG","HUWFOL","HUWAKTE","HUWGEZDTE","HUWBRON","SCHRB","SCHDATUM","SCHPLTS","SCHPROV","SCHLAND","SCHAKTE","SCHAKTDAT","SCHGEM","SCHARCH","SCHREG","SCHFOL","SCHBRON","OPM1","OPM2","OPMH","KIND1","KIND2","KIND3","KIND4","KIND5","KIND6","KIND7","KIND8","KIND9","KIND10","KIND11","KIND12","KIND13","KIND14","KIND15","KIND16","KIND17","KIND18","PRIVACY","RELATIE"
	public static final String EIGENCODE_NL = "EIGENCODE";
	public static final String VNAMEN_NL = "VNAMEN";
	public static final String VVOEG_NL = "VVOEG";
	public static final String ANAAM_NL = "ANAAM";
	public static final String GESLACHT_NL = "GESLACHT";

}
