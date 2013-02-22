package com.jquerymobile.demo.contact;

public class Organization {
	private String type = null;
	private String title = null;
	private String name = null;
	private String rowId = null;

	public Organization() {
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
	public String getTitle() {
		return ContactUtility.replaceNull(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return ContactUtility.replaceNull(name);
	}
	public void setName(String name) {
		this.name = name;
	}

}
