package org.bbekker.genealogy.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class BaseNamePrefix {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private String prefix;
	private String oldId;

	protected BaseNamePrefix() {
	}

	public BaseNamePrefix(String prefix, String oldId) {
		this.prefix = prefix;
		this.oldId = oldId;
	}

	@Override
	public String toString() {
		return String.format("BaseNamePrefix[id=%s prefix='%s' oldId='%s']", id, prefix, oldId);
	}

	public String getId() {
		return id;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getOldId() {
		return oldId;
	}

}
