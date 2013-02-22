package com.jquerymobile.demo.contact;

public class Email {
	private String type = null;
	private String value = null;
	private String rowId = null;
	public Email() {
		super();
	}
	
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	
	public String getValue() {
		return ContactUtility.replaceNull(value);
	}
	public String getType() {
		return ContactUtility.replaceNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
