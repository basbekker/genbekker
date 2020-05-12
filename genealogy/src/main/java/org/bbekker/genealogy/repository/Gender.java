package org.bbekker.genealogy.repository;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bbekker.genealogy.common.AppConstants;
import org.bbekker.genealogy.common.AppConstants.Gender_Type;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Gender {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "GENDER_TYPE", nullable = false, unique = false)
	private String gender;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "GENDER_NAME", nullable = false, unique = false)
	private String name;

	// ISO 639-1 2 character language code
	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "LANGUAGE_CODE", length = 2, nullable = false, unique = false)
	private String languageCode; // ISO 639-1 2 character language code

	protected Gender() {
	}

	public Gender(String gender, String name, String languageCode) {
		if (AppConstants.isInEnum(gender, Gender_Type.class)) {
			this.gender = gender;
		} else {
			throw new RuntimeException("invalid gender " + gender);
		}

		this.name = name;
		this.languageCode = languageCode;
	}

	@Override
	public String toString() {
		return String.format("Gender[id=%s gender='%s' name='%s' languageCode='%s']", id, gender, name, languageCode);
	}

	public String getId() {
		return id;
	}

	public String getGender()
	{
		return gender;
	}

	public String getName() {
		return name;
	}

	public String getlanguageCode() {
		return languageCode;
	}

}
