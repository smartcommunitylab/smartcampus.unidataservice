package eu.trentorise.smartcampus.unidataservice.model;

import smartcampus.services.dbconnector.opera.data.message.Operadb.DBStudentPayment;

public class OperaPayment {

	private String paymentDate;
	private String canteenCode;
	private String canteenDescription;
	private String productDescription;
	private String productType;
	private String totalPayment;
	private String productPrice;
	
	public OperaPayment(DBStudentPayment payment) {
		this.paymentDate = payment.getDatapassaggio();
		this.canteenCode = payment.getCodicemensa();
		this.canteenDescription = payment.getDescrizionemensa();
		this.productDescription = payment.getDescrizioneprodotto();
		this.productType = payment.getTipoprodotto();
		this.totalPayment = payment.getImportototalepassaggio();
		this.productPrice = payment.getImportoprodottodettaglio();
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getCanteenCode() {
		return canteenCode;
	}

	public void setCanteenCode(String canteenCode) {
		this.canteenCode = canteenCode;
	}

	public String getCanteenDescription() {
		return canteenDescription;
	}

	public void setCanteenDescription(String canteenDescription) {
		this.canteenDescription = canteenDescription;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(String totalPayment) {
		this.totalPayment = totalPayment;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

		
	
	
}
