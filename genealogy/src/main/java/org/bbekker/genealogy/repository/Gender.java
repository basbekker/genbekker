package org.bbekker.genealogy.repository;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "GENDER")
@Indexed
public class Gender implements Serializable {

	private static final long serialVersionUID = 1L;

	// The gender id is not generated; unique values need to be provided when these gender entries are loaded.
	// This shouldn't be a problem, as the entries should be a fixed limited set.
	@Id
	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "ID", nullable = false, unique = true)
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "QUALIFIER", nullable = false, unique = true)
	private String qualifier;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "SYMBOL", nullable = false, unique = false)
	private String symbol;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "DESCRIPTION", nullable = false, unique = false)
	private String description;


	protected Gender() {
	}

	public Gender(String id, String qualifier, String description, String symbol) {
		this.id = id;
		this.qualifier = qualifier;
		this.description = description;
		this.symbol = symbol;
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

	public String getQualifier() {
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
