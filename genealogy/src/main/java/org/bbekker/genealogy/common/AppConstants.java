package org.bbekker.genealogy.common;

import java.util.stream.Stream;

public class AppConstants {

	public static final String BASE_NAME_CSV_PATH = "data/import/BaseName.csv";
	public static final String BASE_NAME_PREFIX_CSV_PATH = "data/import/BaseNamePrefix.csv";
	public static final String GENDER_CSV_PATH = "data/import/Gender.csv";
	public static final String ROLE_CSV_PATH = "data/import/Role.csv";
	public static final String RELATIONSHIP_TYPE_CSV_PATH = "data/import/RelationshipType.csv";
	public static final String EVENT_TYPE_CSV_PATH = "data/import/EventType.csv";

	public static final String BEKKER_CSV_NAME = "BEKKER.csv";
	public static final String BEKKER_TEST_CSV_NAME = "BEKKERTest.csv";

	private final static String GENDER_MESSAGE_PREFIX = "GenderId";
	private final static String RELATIONSHIPTYPE_MESSAGE_PREFIX = "RelationshipTypeId";
	private final static String ROLE_MESSAGE_PREFIX = "RoleId";
	private final static String EVENTTYPE_MESSAGE_PREFIX = "EventTypeId";


	/**
	 * The gender type defines the biological sexuality of an individual.
	 * Used fields:
	 * - the character qualifier used by the application,
	 * - the Unicode symbol for the gender,
	 * - the textual description the gender in English.
	 */
	public enum GenderTypes {
		MALE ("MALE", "♂", "male"), // For male sexuality.
		FEMALE ("FEMALE", "♀", "female"), // For female sexuality.
		INTERSEXUAL ("INTERSEXUAL", "⚥", "intersexual"), // For hermaphrodites, androgynous, transgendered sexualities (and probably others).
		UNDEFINED ("UNDEFINED", "✗", "undefined"); // For use when sexuality is (still) unknown, or somehow doesn't fit in the previous 3.

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
		PARENT_CHILD ("PARENT_CHILD", "parent child"),
		MARRIED ("MARRIAGE", "marriage"),
		PARTNERS ("PARTNERSHIP", "partners"),
		COUSINS ("COUSINS", "cousins"),
		FRIEND ("FRIENDS", "friends");

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
		FATHER ("FATHER", "father"),
		MOTHER ("MOTHER", "mother"),
		SON ("SON", "son"),
		DAUGHTER ("DAUGHTER", "daughter"),
		HUSBAND ("HUSBAND", "husband"),
		WIFE ("WIFE", "wife"),
		PARTNER ("PARTNER", "partner"),
		COUSIN ("COUSIN", "cousin"),
		NIECE ("NIECE", "niece"),
		UNCLE ("UNCLE", "uncle"),
		AUNT ("AUNT", "aunt"),
		NEPHEW ("NEPHEW", "nephew"),
		NIECE_UA ("NIECE_UA", "niece (from uncle/aunt"),
		PATERNAL_GRANDFATHER ("PATERNAL_GRANDFATHER", "paternal grandfather"),
		PATERNAL_GRANDMOTHER ("PATERNAL_GRANDMOTHER", "paternal grandmother"),
		MATERNAL_GRANDFATHER ("MATERNAL_GRANDFATHER", "maternal grandfather"),
		MATERNAL_GRANDMOTHER ("MATERNAL_GRANDMOTHER", "maternal grandmother"),
		FRIEND ("FRIEND", "friend");

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

	/**
	 * The relationship types define the relationships that exists between two individuals.
	 * Used fields:
	 * - the qualifier that uniquely identifies the relationship,
	 * - the textual description of the relationship in English.
	 */
	public enum EventTypeCategories {
		INDIVIDUAL ("INDIVIDUAL", "events for individuals"),
		RELATIONSHIP ("RELATIONSHIP", "events for relationships");

		private final String category;
		private final String description;

		EventTypeCategories(String category, String description) {
			this.category = category;
			this.description = description;
		}

		public String getName() {
			return this.getName();
		}

		public String getEventTypeCategory() {
			return category;
		}

		public String getEventTypeDescription() {
			return description;
		}
	}

	/**
	 * The relationship types define the relationships that exists between two individuals.
	 * Used fields:
	 * - the qualifier that uniquely identifies the relationship,
	 * - the textual description of the relationship in English.
	 */
	public enum EventTypes {
		BIRTH ("BIRTH", "INDIVIDUAL", "birth"),
		DEATH ("DEATH", "INDIVIDUAL", "death"),
		MARRIAGE ("MARRIAGE", "RELATIONSHIP", "marriage"),
		DIVORCE ("DIVORCE", "RELATIONSHIP", "divorce"),
		PARNTERS ("PARTNERS", "RELATIONSHIP", "parthership");

		private final String qualifier;
		private final String category;
		private final String description;

		EventTypes(String qualifier, String category, String description) {
			this.qualifier = qualifier;
			this.category = category;
			this.description = description;
		}

		public String getName() {
			return this.getName();
		}

		public String getEventTypeQualifier() {
			return qualifier;
		}

		public String getEventTypeCategory() {
			return category;
		}

		public String getEventTypeDescription() {
			return description;
		}

		public String getMessageKey() {
			return EVENTTYPE_MESSAGE_PREFIX + SystemConstants.DOT + qualifier;
		}

		public static Stream<RelationshipTypes> stream() {
			return Stream.of(RelationshipTypes.values());
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
	public static final String HUWTR_NL = "HUWTR";
	public static final String HUWDATUM_NL = "HUWDATUM";
	public static final String HUWPLTS_NL = "HUWPLTS";
	public static final String HUWPROV_NL = "HUWPROV";
	public static final String HUWLAND_NL = "HUWLAND";
	//...
	public static final String SCHRB_NL = "SCHRB";
	public static final String SCHDATUM_NL = "SCHDATUM";
	public static final String SCHPLTS_NL = "SCHPLTS";
	public static final String SCHPROV_NL = "SCHPROV";
	public static final String SCHLAND_NL = "SCHLAND";
	//..
	public static final String OPM1_NL = "OPM1";
	public static final String OPM2_NL = "OPM2";
	public static final String OPMH_NL = "OPMH";
	public static final String KIND1_NL = "KIND1";
	public static final String KIND2_NL = "KIND2";
	public static final String KIND3_NL = "KIND3";
	public static final String KIND4_NL = "KIND4";
	public static final String KIND5_NL = "KIND5";
	public static final String KIND6_NL = "KIND6";
	public static final String KIND7_NL = "KIND7";
	public static final String KIND8_NL = "KIND8";
	public static final String KIND9_NL = "KIND9";
	public static final String KIND10_NL = "KIND10";
	public static final String KIND11_NL = "KIND11";
	public static final String KIND12_NL = "KIND12";
	public static final String KIND13_NL = "KIND13";
	public static final String KIND14_NL = "KIND14";
	public static final String KIND15_NL = "KIND15";
	public static final String KIND16_NL = "KIND16";
	public static final String KIND17_NL = "KIND17";
	public static final String KIND18_NL = "KIND18";

}
