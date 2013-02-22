package com.jquerymobile.demo.contact;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactUtility {	
	public static final String EMPTY = "";
	public static final String EMPTY_CONTACT_LIST = "{\"contacts\":[]}";
	private static final String EMPTY_JSON = "{\"contactId\":\"\",\"firstName\":\"\",\"lastName\":\"\",\"note\":{\"rowId\":\"\",\"text\":\"\"},\"ims\":[],\"phones\":[],\"emails\":[],\"organizations\":[],\"addresses\":[]}";
	private static final String QUESTION = " = ? ";
	private static final String QUESTION_AND = " = ? AND ";
	
	// Read-only operations
	
	/**
	 * Returns a JSON of the following form:
		{
		  "contacts":[
		    {"key":"J","values":[
		        {"contactId":12,"displayName":"John Doe", "key":"J"},
		        {"contactId":19,"displayName":"Jane Doe", "key":"J"}
		      ]
		    },
		    {"key":"K","values":[
		        {"contactId":12,"displayName":"Konur", "key":"K"},
		        {"contactId":19,"displayName":"Kwan Kwazcski", "key":"K"}
		      ]
		    }
		  ]
		}
	 * @param contentResolver
	 * @return
	 */
	public static String getAllContactDisplaysJSON(ContentResolver contentResolver){
		// Obtain all ContactDisplay objects from database and sort them
		ArrayList<ContactDisplay> list = getAllContactDisplays(contentResolver);
		Collections.sort(list);	
		
		// Populate the data structure consisting of ContactList-ContactGroup-ContactDisplay objects
		// Start with initializing some variables
		ContactList contactList = new ContactList();
		String key = "";
		ArrayList<ContactDisplay> values = new ArrayList<ContactDisplay>();
		ContactGroup group = null;		
		StringWriter writer = new StringWriter();
		
		// Process the list ContactDisplay objects to construct the data structure 
		if(list != null && !list.isEmpty()){			
			for(ContactDisplay display:list){
				if(!display.getKey().equals(key)){
					if(values.size() > 0){
						group = new ContactGroup();
						group.setKey(key);
						group.setValues(values);
						contactList.getContacts().add(group);
					}
					key = display.getKey();
					values = new ArrayList<ContactDisplay>();										
				}
				values.add(display);
			}
			// Add the last group
			if(values.size() > 0){
				group = new ContactGroup();
				group.setKey(key);
				group.setValues(values);
				contactList.getContacts().add(group);
			}
		}else{
			return EMPTY_CONTACT_LIST;
		}
		
		// We have the data structure of ContactList-ContactGroup-ContactDisplay objects where 
		// contactList is the root level element. Write it to a JSON formatted string using
		// org.codehaus.jackson.map.ObjectMapper.
		try{
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(writer, contactList);
		}catch(Exception e){
			return EMPTY_CONTACT_LIST;
		}
		return writer.toString();				
	}

	
	public static String getContactJSON(String id, ContentResolver contentResolver){
		// if id is EMPTY, immediately return an empty json
		if(EMPTY.equals(id)){
			return EMPTY_JSON;
		}
		Contact contact = getContact(id,contentResolver);
		StringWriter writer = new StringWriter();
		if(contact != null){			
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(writer, contact);
			} catch (IOException e) {
				return EMPTY_JSON;
			}
		}
		String val = writer.toString();
		return val;
	}
	

	public static String replaceNull(String in){
		if(in == null){
			return EMPTY;
		}
		else{
			return in;
		}
	}
	
	private static ArrayList<ContactDisplay> getAllContactDisplays(ContentResolver contentResolver){
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		ArrayList<ContactDisplay> list = new ArrayList<ContactDisplay>();
		if (cursor.getCount() > 0) {			
			while (cursor.moveToNext()) {
				String id = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				String displayName = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				ContactDisplay display = new ContactDisplay();
				display.setContactId(id);
				display.setDisplayName(displayName);
				list.add(display);
			}
			cursor.close();
		}
		return list;
	}
	
	private static Contact getContact(String id, ContentResolver contentResolver){
		Contact contact = null;
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
				ContactsContract.Contacts._ID + QUESTION, 
 				new String[]{id}, null); 
		if (cursor.getCount() > 0) {
			contact = new Contact();
			while (cursor.moveToNext()) {
				String contactId = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				contact.setContactId(contactId);
				String[] names = getFirstNameLastName(contactId, contentResolver);
				contact.setFirstName(names[0]);
				contact.setLastName(names[1]);
				contact.setPhones(getPhones(id, contentResolver));
				contact.setEmails(getEmails(contactId, contentResolver));
				contact.setNote(getNote(contactId,contentResolver));
				contact.setAddresses(getAddresses(contactId,contentResolver));
				contact.setIms(getIms(contactId, contentResolver));
				contact.setOrganizations(getOrganizations(contactId, contentResolver));				
			}
		}
		cursor.close();
		return contact;
	}
	
	private static ArrayList<Organization> getOrganizations(String id, ContentResolver contentResolver){
		ArrayList<Organization> organizations = new ArrayList<Organization>();
 		Cursor organizationCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, 
 				ContactsContract.Data.CONTACT_ID + QUESTION_AND + ContactsContract.Data.MIMETYPE + QUESTION, 
 				new String[]{id, 
 				ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE}, null);
 
 		while (organizationCursor.moveToNext()) { 
 			String name = organizationCursor.getString(organizationCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
 			String title = organizationCursor.getString(organizationCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
 			String rowId = organizationCursor.getString(organizationCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization._ID));
 			int type = organizationCursor.getInt(organizationCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE));
 			if (ContactsContract.CommonDataKinds.Organization.TYPE_WORK == type ||
 					ContactsContract.CommonDataKinds.Organization.TYPE_OTHER == type) {
 				Organization organization = new Organization();
 				organization.setName(name);
 				organization.setTitle(title);
 				organization.setRowId(rowId);
 				organization.setType(String.valueOf(type));
 				organizations.add(organization);
 			}
 		} 
 		organizationCursor.close();
		return organizations;
	}

	private static String[] getFirstNameLastName(String id, ContentResolver contentResolver){
		String[] retValue = new String[2];
		retValue[0] = "";
		retValue[1] = "";
	    
	    Cursor nameCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, 
	    		null, ContactsContract.Data.MIMETYPE + QUESTION_AND + ContactsContract.RawContactsEntity.CONTACT_ID + QUESTION, 
	    		new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE ,id},null);
	    while (nameCursor.moveToNext()) {
	        String given = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
	        String family = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
	        if(given != null){
	        	retValue[0] = given;
	        }
	        if(family != null){
	        	retValue[1] = family;
	        }
	    }
	    nameCursor.close();
	    return retValue;
	}
	
	private static ArrayList<Email> getEmails(String id, ContentResolver contentResolver){
		ArrayList<Email> emails = new ArrayList<Email>();
		Cursor emailCursor = contentResolver.query( 
 				ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
 				null,
 				ContactsContract.CommonDataKinds.Email.CONTACT_ID + QUESTION, 
 				new String[]{id}, null); 
 		while (emailCursor.moveToNext()) { 
 			String value = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
 			int type = emailCursor.getInt(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
 			String rowId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email._ID));
 			if(ContactsContract.CommonDataKinds.Email.TYPE_HOME == type ||
 					ContactsContract.CommonDataKinds.Email.TYPE_WORK == type ||
 					ContactsContract.CommonDataKinds.Email.TYPE_OTHER == type){
 	 			Email email = new Email();
 	 			email.setValue(value);
 	 			email.setRowId(rowId);
 	 			email.setType(String.valueOf(type));
 	 			emails.add(email);
 			}
 		}
 		emailCursor.close();
		return emails;
	}
		
	private static ArrayList<Address> getAddresses(String id, ContentResolver contentResolver){
		ArrayList<Address> addresses = new ArrayList<Address>();
 		Cursor addressCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, 
 				ContactsContract.Data.CONTACT_ID + QUESTION_AND + ContactsContract.Data.MIMETYPE + QUESTION, 
 				new String[]{id, 
 				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE}, null); 
 		while(addressCursor.moveToNext()) {
 			int type = addressCursor.getInt(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
 			if(ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME == type ||
 					ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK == type ||
 					ContactsContract.CommonDataKinds.StructuredPostal.TYPE_OTHER == type){ 			
	 			String street = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
	 			String city = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
	 			String state = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
	 			String poBox = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
	 			String zip = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
	 			String country = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
	 			String rowId = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal._ID));
	 			
	 			Address address = new Address();
	 			address.setCity(city);
	 			address.setCountry(country);
	 			address.setPoBox(poBox);
	 			address.setState(state);
	 			address.setType(String.valueOf(type));
	 			address.setZip(zip);
	 			address.setStreet(street);
	 			address.setRowId(rowId);
	 			addresses.add(address); 	
 			}
 		} 
 		addressCursor.close();
 		return addresses;
	}
	
	private static ArrayList<Im> getIms(String id, ContentResolver contentResolver){
		ArrayList<Im> IMs = new ArrayList<Im>();
		Cursor imCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, 
 				ContactsContract.Data.CONTACT_ID + QUESTION_AND + ContactsContract.Data.MIMETYPE + QUESTION, 
 				new String[]{id, 
 				ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE}, null); 
 		while (imCursor.moveToNext()) { 
 			String value = imCursor.getString(imCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
 			String protocol = imCursor.getString(imCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
 			String rowId = imCursor.getString(imCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im._ID));
			Im im = new Im();
			im.setProtocol(protocol);
			im.setValue(value);
			im.setRowId(rowId);
			IMs.add(im); 			
 		} 
 		imCursor.close();
 		return IMs;
	}
	
	private static Note getNote(String id, ContentResolver contentResolver){
 		
 		Note note = null;
		Cursor noteCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, 
 				ContactsContract.Data.CONTACT_ID + QUESTION_AND + ContactsContract.Data.MIMETYPE + QUESTION, 
 					new String[]{id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE}, null); 
 		if (noteCursor.moveToFirst()) { 
 			String value = noteCursor.getString(noteCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
 			String rowId = noteCursor.getString(noteCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note._ID));
 			note = new Note();
 			note.setRowId(rowId);
 			note.setText(value);
 		} 
 		noteCursor.close();
 		 
 		return note;
	}

	private static ArrayList<Phone> getPhones(String id, ContentResolver contentResolver){
		ArrayList<Phone> phones = new ArrayList<Phone>();
		Cursor phoneCursor = contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID
						+ QUESTION, new String[] { id }, null);
		while (phoneCursor.moveToNext()) {
			String no = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			String rowId = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
			int type = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
			if(ContactsContract.CommonDataKinds.Phone.TYPE_WORK == type ||
					ContactsContract.CommonDataKinds.Phone.TYPE_HOME == type ||
					ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE == type ||
					ContactsContract.CommonDataKinds.Phone.TYPE_OTHER == type){
				Phone phone = new Phone();
				phone.setRowId(rowId);
				phone.setNo(no);
				phone.setType(String.valueOf(type));
				phones.add(phone);	
			}		
		}
		phoneCursor.close();
		return phones;
	}
	// Write operations
	public static void deleteContact(String id, ContentResolver contentResolver, String accountType){	
		HashMap<String,String> contacts = getUsersFromAccount(accountType, contentResolver);
		String existingContactId = contacts.get(id);
		if(existingContactId == null){
			// The contact does not belong to account
			return;
		}
		deleteContactInternal(id, contentResolver);
	}
	
	public static void saveOrUpdateContact(Contact contact,ContentResolver contentResolver, String accountName, String accountType){
		if(contact == null || accountName == null || accountType == null){
			return;
		}
		
		String id = contact.getContactId();
		if(!EMPTY.equals(replaceNull(id))){	
			// This is existing contact to update 
			HashMap<String,String> contacts = getUsersFromAccount(accountType, contentResolver);
			String existingContactId = contacts.get(id);
			if(existingContactId == null){
			// This is associated with another account - cannot process
				return;
			}
			deleteContactInternal(id, contentResolver);			
		}
		saveContact(contact,contentResolver, accountName, accountType);
	}	

	private static void saveContact(Contact contact,ContentResolver contentResolver, String accountName, String accountType){
	   ArrayList<ContentProviderOperation>operations = new ArrayList<ContentProviderOperation>();
       operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, accountType)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, accountName)
                .build());
       operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getFirstName())
                .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, contact.getLastName())                
                .build());
       if(contact.getNote() != null){
    	   operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                   .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                   .withValue(ContactsContract.Data.MIMETYPE,
                   		ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                   .withValue(ContactsContract.CommonDataKinds.Note.NOTE, contact.getNote().getText())                                
                   .build());
       }
       
		Collection<Address> addresses = contact.getAddresses();
		if(addresses != null){
			for(Address address:addresses){
				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
		                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,address.getType())
		                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET,address.getStreet())
		                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY,address.getCity())
		                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION,address.getState())
		                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POBOX,address.getPoBox())
		                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,address.getZip())
		                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,address.getCountry())
		                .build());
			}
		}
		Collection<Organization> organizations = contact.getOrganizations();
		if(organizations != null){
			for(Organization organization:organizations){
				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
		                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, organization.getType())
		                .withValue(ContactsContract.CommonDataKinds.Organization.DATA, organization.getName())
		                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, organization.getTitle())
		                .build());
			}
		}
		Collection<Email> emails = contact.getEmails();
		if(emails != null){
			for(Email email:emails){
				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
		                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,email.getType())
		                .withValue(ContactsContract.CommonDataKinds.Email.DATA,email.getValue())
		                .build());
			}
		}
		Collection<Im> ims = contact.getIms();
		if(ims != null){
			for(Im im:ims){
				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
		                .withValue(ContactsContract.CommonDataKinds.Im.PROTOCOL,im.getProtocol())
		                .withValue(ContactsContract.CommonDataKinds.Im.DATA,im.getValue())
		                .build());
			}
		}
		Collection<Phone> phones = contact.getPhones();
		if(phones != null){
			for(Phone phone:phones){
				operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
		                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
		                .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
		                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,phone.getType())
		                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phone.getNo())
		                .build());
			}
		}
        try {
        	contentResolver.applyBatch(ContactsContract.AUTHORITY,operations);
        } catch (Exception e) {
        	
        }
	}

	private static void deleteContactInternal(String id, ContentResolver contentResolver){ 		
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
				ContactsContract.Contacts._ID + QUESTION, 
 				new String[]{id}, null); 
		String lookup = null;
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				lookup = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));			
			}		
		}
		cursor.close();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookup);        
        contentResolver.delete(uri, null, null);
	}
		
	private static HashMap<String,String> getUsersFromAccount(String accountType, ContentResolver contentResolver){
		Cursor cursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, null,
				ContactsContract.RawContacts.ACCOUNT_TYPE + QUESTION, new String[] { accountType }, null);
		HashMap<String,String> map = new HashMap<String,String>();
		if (cursor.getCount() > 0) {		
			while (cursor.moveToNext()) {				
				String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
				map.put(contactId, contactId);
			}
		}
		return map;
	}
}
