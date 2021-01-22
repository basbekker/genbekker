package org.bbekker.genealogy.repository;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EVENT_TYPE")
//@Table(name = "EVENT_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = {"CATEGORY", "QUALIFIER"}))
public class EventType implements Serializable {

	private static final long serialVersionUID = 1L;

	// The event type id is not generated; unique values need to be provided when these event type entries are loaded.
	// This shouldn't be a problem, as the entries should be a fixed limited set.
	@Id
	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "ID", nullable = false, unique = true)
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "CATEGORY", nullable = false, unique = false)
	private String category;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "QUALIFIER", nullable = false, unique = false)
	private String qualifier;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "DESCRIPTION", nullable = false, unique = false)
	private String description;

	protected EventType() {
	}

	public EventType(String id, String category, String qualifier,  String description) {
		this.id = id;
		this.category = category;
		this.qualifier = qualifier;
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("EventType[id=%s qualifier='%s' category='%s' description='%s']", id, qualifier, category, description);
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

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setdDscription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
