package eu.trentorise.smartcampus.unidataservice.model;

import smartcampus.service.esse3.data.message.Esse3.CalendarCds;


public class CalendarCdsData {

	private String id;
	private String title;
	private String teacher;
	private String room;
	private long from;
	private long to;
	private String type;
	
	public CalendarCdsData(CalendarCds cc) {
		this.id = cc.getId();
		this.title = cc.getTitle();
		this.teacher = cc.getTeacher();
		this.room = cc.getRoom();
		this.from = cc.getFrom();
		this.to = cc.getTo();
		this.type = cc.getType();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public long getFrom() {
		return from;
	}
	public void setFrom(long from) {
		this.from = from;
	}
	public long getTo() {
		return to;
	}
	public void setTo(long to) {
		this.to = to;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
