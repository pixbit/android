package com.jquerymobile.demo.contact;

public class Address {
	private String type = null;
	private String street = null;
	private String city = null;
	private String state = null;
	private String zip = null;
	private String country = null;
	private String rowId = null;
	private String poBox;

	public Address() {
		super();
	}

	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getType() {
		return ContactUtility.replaceNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStreet() {
		return ContactUtility.replaceNull(street);
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return ContactUtility.replaceNull(city);
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return ContactUtility.replaceNull(state);
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return ContactUtility.replaceNull(zip);
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return ContactUtility.replaceNull(country);
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPoBox() {
		return ContactUtility.replaceNull(poBox);
	}
	public void setPoBox(String poBox) {
		this.poBox = poBox;
	}
}
