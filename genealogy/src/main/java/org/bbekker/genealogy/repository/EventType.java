package org.bbekker.genealogy.repository;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "EVENT_TYPE")
public class EventType {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "QUALIFIER", nullable = false, unique = true)
	private String qualifier;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "CATEGORY", nullable = false, unique = false)
	private String category;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "DESCRIPTION", nullable = false, unique = false)
	private String description;

	protected EventType() {
	}

	public EventType(String qualifier, String category, String description) {
		this.qualifier = qualifier;
		this.category = category;
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("EventType[id=%s qualifier='%s' category='%s' description='%s']", id, qualifier, category, description);
	}

	public void setId(String id) {
		this.id = id;
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
