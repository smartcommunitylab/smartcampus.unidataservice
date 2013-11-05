package eu.trentorise.smartcampus.unidataservice.model;

import smartcampus.service.esse3.data.message.Esse3.Facolta;

public class FacoltaData {

	private String facId;
	private String description;
	
	public FacoltaData(Facolta facolta) {
		this.facId = facolta.getFacId();
		this.description = facolta.getDescription();
	}

	public String getFacId() {
		return facId;
	}

	public void setFacId(String facId) {
		this.facId = facId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
