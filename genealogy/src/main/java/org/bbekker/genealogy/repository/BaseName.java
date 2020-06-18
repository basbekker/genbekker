package org.bbekker.genealogy.repository;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "BASE_NAME")
public class BaseName implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	private String id;

	private String name;
	private String oldId;

	protected BaseName() {}

	public BaseName(String name, String oldId) {
		this.name = name;
		this.oldId = oldId;
	}

	@Override
	public String toString() {
		return String.format("BaseName[id=%s name='%s' oldId='%s']", id, name, oldId);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getOldId() {
		return oldId;
	}

}
