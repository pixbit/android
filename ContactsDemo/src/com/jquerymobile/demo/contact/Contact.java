package com.jquerymobile.demo.contact;

import java.util.Collection;

public class Contact {
	private String contactId;
	private String firstName;
	private String lastName;
	private Collection<Phone> phones;
	private Collection<Email> emails;
	private Collection<Address> addresses;
	private Collection<Organization> organizations;
	private Note note;
	private Collection<Im> ims;
	
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	
	public String getFirstName() {
		return ContactUtility.replaceNull(firstName);
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return ContactUtility.replaceNull(lastName);
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Collection<Im> getIms() {
		return ims;
	}
	public void setIms(Collection<Im> ims) {
		this.ims = ims;
	}

	public Collection<Phone> getPhones() {
		return phones;
	}
	public void setPhones(Collection<Phone> phones) {
		this.phones = phones;
	}
	public Collection<Email> getEmails() {
		return emails;
	}
	public void setEmails(Collection<Email> emails) {
		this.emails = emails;
	}
	public Collection<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(Collection<Address> addresses) {
		this.addresses = addresses;
	}
	public Note getNote() {
		return note;
	}
	public void setNote(Note note) {
		this.note = note;
	}
	public Collection<Organization> getOrganizations() {
		return organizations;
	}
	public void setOrganizations(Collection<Organization> organizations) {
		this.organizations = organizations;
	}
}
