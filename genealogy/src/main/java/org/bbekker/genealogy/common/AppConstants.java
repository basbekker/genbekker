package org.bbekker.genealogy.common;

import java.util.stream.Stream;

public class AppConstants {

	public static final String BASE_NAME_CSV_PATH = "data/BaseName.csv";
	public static final String BASE_NAME_PREFIX_CSV_PATH = "data/BaseNamePrefix.csv";
	public static final String GENDER_CSV_PATH = "data/Gender.csv";
	public static final String ROLE_CSV_PATH = "data/Role.csv";
	public static final String RELATIONSHIP_TYPE_CSV_PATH = "data/RelationshipType.csv";

	public static final String BEKKER_CSV_NAME = "BEKKER.csv";
	public static final String BEKKER_TEST_CSV_NAME = "BEKKER.csv";

	private final static String GENDER_MESSAGE_PREFIX = "GenderId";
	private final static String RELATIONSHIPTYPE_MESSAGE_PREFIX = "RelationshipTypeId";
	private final static String ROLE_MESSAGE_PREFIX = "RoleId";

	/**
	 * The gender type defines the biological sexuality of an individual.
	 * Used fields:
	 * - the character qualifier used by the application,
	 * - the Unicode symbol for the gender,
	 * - the textual description the gender in English.
	 */
	public enum GenderTypes {
		MALE ("M", "♂", "male"), // For male sexuality.
		FEMALE ("F", "♀", "female"), // For female sexuality.
		INTERSEXUAL ("I", "⚥", "intersexual"), // For hermaphrodites, androgynous, transgendered sexualities (and probably others).
		UNDEFINED ("U", "✗", "undefined"); // For use when sexuality is (still) unknown, or somehow doesn't fit in the previous 3.

		private final String qualifier;
		private final String symbol;
		private final String description;

		GenderTypes(String qualifier, String symbol, String description) {
			this.qualifier = qualifier;
			this.symbol = symbol;
			this.description = description;
		}

		public String getName() {
			return this.getName();
		}

		public String getGenderQualifier() {
			return qualifier;
		}

		public String getGenderSymbol() {
			return symbol;
		}

		public String getGenderDescription() {
			return description;
		}

		public String getMessageKey() {
			return GENDER_MESSAGE_PREFIX + SystemConstants.DOT + qualifier;
		}

		public static Stream<RelationshipTypes> stream() {
			return Stream.of(RelationshipTypes.values());
		}
	}

	/**
	 * The relationship types define the relationships that exists between two individuals.
	 * Used fields:
	 * - the qualifier that uniquely identifies the relationship,
	 * - the textual description of the relationship in English.
	 */
	public enum RelationshipTypes {
		PARENT_CHILD ("PC", "parent child"),
		MARRIED ("M", "marriage"),
		PARTNERS ("P", "partners"),
		COUSINS ("C", "cousins"),
		FRIEND ("FR", "friends");

		private final String qualifier;
		private final String description;

		RelationshipTypes(String qualifier, String description) {
			this.qualifier = qualifier;
			this.description = description;
		}

		public String getName() {
			return this.getName();
		}

		public String getRelationshipTypeQualifier() {
			return qualifier;
		}

		public String getRelationshipTypeDescription() {
			return description;
		}

		public String getMessageKey() {
			return RELATIONSHIPTYPE_MESSAGE_PREFIX + SystemConstants.DOT + qualifier;
		}

		public static Stream<RelationshipTypes> stream() {
			return Stream.of(RelationshipTypes.values());
		}
	}

	public enum Roles {
		FATHER ("F", "father"),
		MOTHER ("M", "mother"),
		SON ("S", "son"),
		DAUGHTER ("D", "daughter"),
		HUSBAND ("H", "husband"),
		WIFE ("W", "wife"),
		PARTNER ("P", "partner"),
		COUSIN ("C", "cousin"),
		NIECE ("N", "niece"),
		UNCLE ("U", "uncle"),
		AUNT ("A", "aunt"),
		NEPHEW ("NW", "nephew"),
		NIECE_UA ("NE", "niece (from uncle/aunt"),
		PATERNAL_GRANDFATHER ("PG", "paternal grandfather"),
		PATERNAL_GRANDMOTHER ("PM", "paternal grandmother"),
		MATERNAL_GRANDFATHER ("MG", "maternal grandfather"),
		MATERNAL_GRANDMOTHER ("MM", "maternal grandmother"),
		FRIEND ("FR", "friend");

		private final String qualifier;
		private final String description;

		Roles(String qualifier, String description) {
			this.qualifier = qualifier;
			this.description = description;
		}

		public String getName() {
			return this.getName();
		}

		public String getRoleQualifier() {
			return qualifier;
		}

		public String getRoleDescription() {
			return description;
		}

		public String getMessageKey() {
			return ROLE_MESSAGE_PREFIX + SystemConstants.DOT + qualifier;
		}

		public static Stream<Roles> stream() {
			return Stream.of(Roles.values());
		}
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
	public static final String GEBPLTS_NL = "GEBPLTS";
	public static final String GEBPROV_NL = "GEBPROV";
	public static final String GEBLAND_NL = "GEBLAND";
	//...
	public static final String OVLDATUM_NL = "OVLDATUM";
	public static final String OVLPLTS_NL = "OVLPLTS";
	public static final String OVLPROV_NL = "OVLPROV";
	public static final String OVLLAND_NL = "OVLLAND";
	//...
	public static final String PRNAAM_NL = "PRNAAM";
	public static final String PTITEL_NL = "PTITEL";
	public static final String PVNAMEN_NL = "PVNAMEN";
	public static final String PVVOEG_NL = "PVVOEG";
	public static final String PANAAM_NL ="PANAAM";
	public static final String PNAAMGRP_NL = "PNAAMGRP";
	public static final String PGESLACHT_NL = "PGESLACHT";
	public static final String PBEROEP_NL = "PBEROEP";
	public static final String PGEBDP_NL = "PGEBDP";
	public static final String PGEBDATUM_NL = "PGEBDATUM";
	public static final String PGEBPLTS_NL = "PGEBPLTS";
	public static final String PGEBPROV_NL = "PGEBPROV";
	public static final String PGEBLAND_NL = "PGEBLAND";
	//...
	public static final String POVLDATUM_NL = "POVLDATUM";
	public static final String POVLPLTS_NL = "POVLPLTS";
	public static final String POVLPROV_NL = "POVLPROV";
	public static final String POVLLAND_NL = "POVLLAND";
	//...

}
