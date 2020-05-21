package org.bbekker.genealogy.common;

import org.springframework.beans.factory.annotation.Value;

public class AppConstants {

	@Value("${upload.folder}")
	public static final String UPLOAD_FOLDER = "C://Temp//Upload//";

	public static final String BASE_NAME_CSV_PATH = "data/BaseName.csv";
	public static final String BASE_NAME_PREFIX_CSV_PATH = "data/BaseNamePrefix.csv";

	public static final String GENDER_CSV_PATH = "data/Gender.csv";

	public static final String BEKKER_CSV_NAME = "BEKKER.csv";

	public enum Gender_Type {
		MALE, FEMALE, UNDEFINED
	}

	public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		for (E e : enumClass.getEnumConstants()) {
			if (e.name().equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static final String ISO639_1_NL = "nl";
	public static final String ISO639_1_ES = "es";
	public static final String ISO639_1_EN = "en";

	public static final String MALE_NL = "M";
	public static final String FEMALE_NL = "V";

	// "PROJNR","EIGENCODE","HUWNR","HUWTYPE","XHUW","VADERCODE","MOEDER","REF1","REF2","REFH","RNAAM","TITEL","INITS5","VNAMEN","VVOEG","ANAAM","NAAMGRP","NAAMCODE","GESLACHT","BEROEP","GEBDP","GEBDATUM","GEBPLTS","GEBPROV","GEBLAND","GEBAKTDAT","GEBGEM","GEBARCH","GEBREG","GEBFOL","GEBAKTE","GEBGEZDTE","GEBBRON","OVLBG","OVLDATUM","OVLPLTS","OVLPROV","OVLLAND","OVLAKTDAT","OVLGEM","OVLARCH","OVLREG","OVLFOL","OVLAKTE","OVLBRON","PRNAAM","PTITEL","PVNAMEN","PVVOEG","PANAAM","PNAAMGRP","PGESLACHT","PBEROEP","PGEBDP","PGEBDATUM","PGEBPLTS","PGEBPROV","PGEBLAND","PGEBAKTDAT","PGEBGEM","PGEBARCH","PGEBREG","PGEBFOL","PGEBAKTE","PGEBBRON","PGEBGEZDTE","POVLBG","POVLDATUM","POVLPLTS","POVLPROV","POVLLAND","POVLAKTDAT","POVLGEM","POVLARCH","POVLREG","POVLFOL","POVLAKTE","POVLBRON","HUWTR","HUWDATUM","HUWPLTS","HUWPROV","HUWLAND","HUWAKTDAT","HUWGEM","HUWARCH","HUWREG","HUWFOL","HUWAKTE","HUWGEZDTE","HUWBRON","SCHRB","SCHDATUM","SCHPLTS","SCHPROV","SCHLAND","SCHAKTE","SCHAKTDAT","SCHGEM","SCHARCH","SCHREG","SCHFOL","SCHBRON","OPM1","OPM2","OPMH","KIND1","KIND2","KIND3","KIND4","KIND5","KIND6","KIND7","KIND8","KIND9","KIND10","KIND11","KIND12","KIND13","KIND14","KIND15","KIND16","KIND17","KIND18","PRIVACY","RELATIE"
	public static final String PROJNR_NL = "PROJNR";
	public static final String EIGENCODE_NL = "EIGENCODE";
	public static final String HUWTYPE_NL = "HUWTYPE";
	public static final String HUWNR_NL = "HUWNR";
	public static final String XHUW_NL = "XHUW";
	public static final String VADERCODE_NL = "VADERCODE";
	public static final String MOEDER_NL = "MOEDER";
	public static final String REF1_NL = "REF1";
	public static final String REF2_NL = "REF2";
	public static final String REFH_NL = "REFH";
	public static final String RNAAM_NL = "RNAAM";
	public static final String TITEL_NL = "TITEL";
	public static final String INITS5_NL = "INITS5";
	public static final String VNAMEN_NL = "VNAMEN";
	public static final String VVOEG_NL = "VVOEG";
	public static final String ANAAM_NL = "ANAAM";
	public static final String NAAMGRP_NL = "NAAMGRP";
	public static final String NAAMCODE_NL = "NAAMCODE";
	public static final String GESLACHT_NL = "GESLACHT";
	public static final String BEROEP_NL = "BEROEP";
	public static final String GEBDP_NL = "GEBDP";
	public static final String GEBDATUM_NL = "GEBDATUM";
	//...
	public static final String OVLDATUM_NL = "OVLDATUM";
	//...
	public static final String PRNAAM_NL = "PRNAAM";
	public static final String PTITEL_NL = "PTITEL";
	public static final String PVNAMEN_NL = "PVNAMEN";
	public static final String PVVOEG_NL = "PVVOEG";
	public static final String PANAAM_NL ="PANAAM";
	public static final String PNAAMGRP_NL = "PNAAMGRP";
	public static final String PGESLACHT_NL = "PGESLACHT";

}
