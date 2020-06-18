package org.bbekker.genealogy.repository;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BASE_NAME_PREFIX")
public class BaseNamePrefix implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
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
