package com.jquerymobile.demo.contact;

public class ContactDisplay implements Comparable<Object>{
	private String contactId;
	private String displayName;
	
	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getKey(){
		if(displayName == null || "".equals(displayName)){
			return "";
		}
		else{
			return (String.valueOf(displayName.charAt(0))).toUpperCase();
		}
	}
	
	public int compareTo(Object arg0) {
		if(arg0 == null || !this.getClass().equals(arg0.getClass())){
			return 1;
		}
		String otherDisplayName = ((ContactDisplay)arg0).getDisplayName();
		if(displayName == null){
			if(otherDisplayName == null){
				return 0;
			}else{
				return -1;
			}
		}else{
			if(otherDisplayName == null){
				return 1;
			}else{
				return displayName.compareToIgnoreCase(otherDisplayName);
			}
		}
	}	
}
