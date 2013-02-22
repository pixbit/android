package com.jquerymobile.demo.contact;

import java.util.Collection;

public class ContactGroup {
	private String key;
	private Collection<ContactDisplay> values;

	public Collection<ContactDisplay> getValues() {
		return values;
	}
	public void setValues(Collection<ContactDisplay> values) {
		this.values = values;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
