package com.jquerymobile.demo.contact;

import java.util.ArrayList;
import java.util.Collection;

public class ContactList {
	public ContactList() {
		super();
		contacts = new ArrayList<ContactGroup>();
	}
	
	private Collection<ContactGroup> contacts;

	public Collection<ContactGroup> getContacts() {
		return contacts;
	}
}
