package org.bbekker.genealogy.repository;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RELATIONSHIP_TYPE")
public class RelationshipType implements Serializable {

	private static final long serialVersionUID = 1L;

	// The relationship type id is not generated; unique values need to be provided when these relationship type entries are loaded.
	// This shouldn't be a problem, as the entries should be a fixed limited set.
	@Id
	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "ID", nullable = false, unique = true)
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "QUALIFIER", nullable = false, unique = true)
	private String qualifier;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "DESCRIPTION", nullable = false, unique = false)
	private String description;

	protected RelationshipType() {
	}

	public RelationshipType(String id, String qualifier, String description) {
		this.id = id;
		this.qualifier = qualifier;
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("RelationshipType[id=%s qualifier='%s' description='%s']", id, qualifier, description);
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

	public void setdDscription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
