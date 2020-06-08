package org.bbekker.genealogy.repository;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "EVENT")
public class Event {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Column(name = "EVENT_NAME", nullable = false, unique = false)
	private String eventName;

	@Basic(optional = false, fetch = FetchType.EAGER)
	@Temporal(TemporalType.DATE)
	@Column(name = "EVENT_DATE", nullable = false, unique = false)
	private Date eventDate;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "EVENT_PLACE", nullable = true, unique = false)
	private String eventPlace;

	@Basic(optional = true, fetch = FetchType.EAGER)
	@Column(name = "EVENT_NOTE", nullable = true, unique = false)
	private String eventNote;


	protected Event() {
	}

	public Event(String eventName, Date eventDate) {
		this.eventName = eventName;
		this.eventDate = eventDate;
	}

	public Event(String eventName, Date eventDate, String eventPlace, String eventNote) {
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.eventPlace = eventPlace;
		this.eventNote = eventNote;
	}

	@Override
	public String toString() {
		return String.format("Event[id=%s eventName='%s' eventDate='%s' eventPlace='%s' eventNotes='%s']", id, eventName, eventDate, eventPlace, eventNote);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}

	public String getEventPlace() {
		return eventPlace;
	}

	public void setEventNote(String eventNote) {
		this.eventNote = eventNote;
	}

	public String getEventNote() {
		return eventNote;
	}

}
