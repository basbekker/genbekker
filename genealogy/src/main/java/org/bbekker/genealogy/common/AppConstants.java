package org.bbekker.genealogy.common;

public class AppConstants {

	public static final String BASE_NAME_CSV_PATH = "data/BaseName.csv";
	public static final String BASE_NAME_PREFIX_CSV_PATH = "data/BaseNamePrefix.csv";

	public static final String GENDER_CSV_PATH = "data/Gender.csv";

	public enum Gender_Type {
		MALE,
		FEMALE
	}

	public static <E extends Enum<E>> boolean isInEnum(String value, Class<E> enumClass) {
		for (E e : enumClass.getEnumConstants()) {
			if(e.name().equals(value)) { return true; }
		}
		return false;
	}

}
