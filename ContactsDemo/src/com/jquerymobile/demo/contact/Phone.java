package com.jquerymobile.demo.contact;

public class Phone {
	private String type = null;
	private String rowId = null;
	private String no = null;

	public Phone() {
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
	
	public String getNo() {
		return ContactUtility.replaceNull(no);
	}
	public void setNo(String no) {
		this.no = no;
	}

	
}
