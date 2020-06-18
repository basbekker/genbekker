package org.bbekker.genealogy.repository;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Gender implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "GENDER_QUALIFIER", nullable = false, unique = true)
	private String qualifier;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "GENDER_SYMBOL", nullable = false, unique = false)
	private String symbol;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "GENDER_DESCRIPTION", nullable = false, unique = false)
	private String description;


	protected Gender() {
	}

	public Gender(String qualifier, String symbol, String description) {
		this.qualifier = qualifier;
		this.symbol = symbol;
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Gender[id=%s qualifier='%s' symbol='%s' description='%s']", id, qualifier, symbol, description);
	}

	public String getId() {
		return id;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getGender()
	{
		return qualifier;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
