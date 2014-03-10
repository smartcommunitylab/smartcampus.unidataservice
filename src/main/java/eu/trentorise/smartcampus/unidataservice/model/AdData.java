package eu.trentorise.smartcampus.unidataservice.model;

import java.util.ArrayList;
import java.util.List;

import smartcampus.service.esse3.data.message.Esse3.Ad;

public class AdData {

	private String adId;
	private String adcod;
	private String description;
	private List<String> fatPart;
	private List<String> domPart;
	
	public AdData(Ad ad) {
		this.adId = ad.getAdId();
		this.adcod = ad.getAdcod();
		this.description = ad.getDescription();
		fatPart = new ArrayList<String>();
		domPart = new ArrayList<String>();
		for (String fp: ad.getFatPartList()) {
			fatPart.add(fp);
		}
		for (String dp: ad.getDomPartList()) {
			domPart.add(dp);
		}		
	}

	public String getAdId() {
		return adId;
	}

	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getAdcod() {
		return adcod;
	}

	public void setAdcod(String adcod) {
		this.adcod = adcod;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getFatPart() {
		return fatPart;
	}

	public void setFatPart(List<String> fatPart) {
		this.fatPart = fatPart;
	}

	public List<String> getDomPart() {
		return domPart;
	}

	public void setDomPart(List<String> domPart) {
		this.domPart = domPart;
	}
	
	
	
}
