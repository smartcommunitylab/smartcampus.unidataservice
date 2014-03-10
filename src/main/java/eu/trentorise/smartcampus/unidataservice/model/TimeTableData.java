package eu.trentorise.smartcampus.unidataservice.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import smartcampus.service.esse3.data.message.Esse3.Orari;

public class TimeTableData implements Comparable<TimeTableData> {

	private String type;
	private String date;
	private String from;
	private String to;
	private String room;
	private String teacher;
	
	public TimeTableData(Orari orari) {
		this.type = orari.getType();
		this.date = orari.getDate();
		this.from = orari.getFrom();
		this.to = orari.getTo();
		this.room = orari.getRoom();
		this.teacher = orari.getTeacher();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	
	protected Date toDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH.mm");
		Date d = sdf.parse(date + " " + from);		
		return d;
	}
	
	@Override
	public int compareTo(TimeTableData o) {
		try {
			return this.toDate().compareTo(o.toDate());
		} catch (ParseException e) {
			return 0;
		}

	}
	
	
	
}
