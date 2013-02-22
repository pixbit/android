package com.jquerymobile.demo.contact;

public class Im {
	private String protocol = null;
	private String rowId = null;
	private String value = null;
	

	public Im() {
		super();
	}
	
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getProtocol() {
		return ContactUtility.replaceNull(protocol);
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getValue() {
		return ContactUtility.replaceNull(value);
	}
	public void setValue(String value) {
		this.value = value;
	}

	
}
