package eu.trentorise.smartcampus.unidataservice.model;

import smartcampus.service.esse3.data.message.Esse3.Student;

public class StudentInfoData {

	private String fiscalCode;
	private String name;
	private String surname;
	private String enrollmentYear;
	private String nation;
	private String academicYear;
	private String supplementaryYears;
	private String cfu;
	private String cfuTotal;
	private String marksNumber;
	private String marksAverage;
	private String gender;
	private String dateOfBirth;
	private String phone;
	private String mobile;
	private String address;
	private String cds;

	public StudentInfoData(Student student) {
		fiscalCode = student.getFiscalCode();
		name = student.getName();
		surname = student.getSurname();
		enrollmentYear = student.getEnrollmentYear();
		nation = student.getNation();
		academicYear = student.getAcademicYear();
		supplementaryYears = student.getSupplementaryYears();
		cfu = student.getCfu();
		cfuTotal = student.getCfuTotal();
		marksNumber = student.getMarksNumber();
		marksAverage = student.getMarksAverage();
		gender = student.getGender();
		dateOfBirth = student.getDateOfBirth();
		phone = student.getPhone();
		mobile = student.getMobile();
		address = student.getAddress();
		cds = student.getCds();
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public String getSupplementaryYears() {
		return supplementaryYears;
	}

	public void setSupplementaryYears(String supplementaryYears) {
		this.supplementaryYears = supplementaryYears;
	}

	public String getCfu() {
		return cfu;
	}

	public void setCfu(String cfu) {
		this.cfu = cfu;
	}

	public String getCfuTotal() {
		return cfuTotal;
	}

	public void setCfuTotal(String cfuTotal) {
		this.cfuTotal = cfuTotal;
	}

	public String getMarksNumber() {
		return marksNumber;
	}

	public void setMarksNumber(String marksNumber) {
		this.marksNumber = marksNumber;
	}

	public String getMarksAverage() {
		return marksAverage;
	}

	public void setMarksAverage(String marksAverage) {
		this.marksAverage = marksAverage;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCds() {
		return cds;
	}

	public void setCds(String cds) {
		this.cds = cds;
	}

//	public String getIdAda() {
//		return idAda;
//	}
//
//	public void setIdAda(String idAda) {
//		this.idAda = idAda;
//	}
	
}
