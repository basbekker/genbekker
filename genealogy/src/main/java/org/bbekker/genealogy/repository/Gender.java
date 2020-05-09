package org.bbekker.genealogy.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Gender {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@NotNull
	private String name;
	// ISO 639-1 2 character language code
	@NotNull
	private String languageCode; // ISO 639-1 2 character language code

	protected Gender() {
	}

	public Gender(String name, String languageCode) {
		this.name = name;
		this.languageCode = languageCode;
	}

	@Override
	public String toString() {
		return String.format("Gender[id=%s name='%s' languageCode='%s']", id, name, languageCode);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getlanguageCode() {
		return languageCode;
	}

}
