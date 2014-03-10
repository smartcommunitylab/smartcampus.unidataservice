package eu.trentorise.smartcampus.unidataservice.model;

import smartcampus.service.esse3.data.message.Esse3.Pds;

public class PdsData {

	private String pdsId;
	private String pdsCod;
	
	public PdsData(Pds pds) {
		this.pdsId = pds.getPdsId();
		this.pdsCod = pds.getPdsCod();
	}

	public String getPdsId() {
		return pdsId;
	}

	public void setPdsId(String pdsId) {
		this.pdsId = pdsId;
	}

	public String getPdsCod() {
		return pdsCod;
	}

	public void setPdsCod(String pdsCod) {
		this.pdsCod = pdsCod;
	}
	
}
