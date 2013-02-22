package com.jquerymobile.demo.contact;

public class Note {
	private String rowId = null;
	private String text = null;

	public Note() {
		super();
	}
	public String getText() {
		return ContactUtility.replaceNull(text);
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

}
