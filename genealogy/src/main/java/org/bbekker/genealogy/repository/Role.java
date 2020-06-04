package org.bbekker.genealogy.repository;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Role {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "DESCRIPTION", nullable = false, unique = false)
	private String description;

	protected Role() {
	}

	public Role(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Role[id=%s description='%s']", id, description);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setdescription(String description) {
		this.description = description;
	}

	public String getdescription() {
		return description;
	}

}
