package com.jquerymobile.demo.contact;

import org.codehaus.jackson.map.ObjectMapper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;

public class ContactsActivity extends Activity {
	WebView webView;
	private String accountType = null;
	private String accountName = null;
	private Handler handler = null;
	private static final String JAVASCRIPT = "javascript:";
	private static final String BRC = "()";
	private static final String BRC_OPEN = "('";
	private static final String BRC_CLOSE = "')";
	private static final String Q = "?";
	private static final String HTML_ROOT = "file:///android_asset/www/";		

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
       
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);      
        accountType = "com.jquerymobile.demo.contact";
        Account[] accounts = AccountManager.get(this).getAccountsByType (accountType);
        if(accounts.length != 0){
        	accountName = accounts[0].name;
        }
        handler = new Handler();
        webView.addJavascriptInterface(this, "contactSupport");        
        loadPage("index.html");
    }
    
    public void loadPage(String in){
    	final String url = HTML_ROOT + in;
    	loadURL(url);
    }
    
    private void loadURL(final String in){
    	handler.post(new Runnable() {
            public void run() {
            	webView.loadUrl(in);
            }
        });
    }
    
    public void createAccount(String accountN, String displayPage){
    	if(accountN != null && !ContactUtility.EMPTY.equals(accountN)){
    		accountName = accountN;
    		AccountManager.get(this).addAccountExplicitly(new Account(accountName,accountType), "dummyPassword", null);   
    	}
    	loadPage(displayPage);
    }

    public void deleteContact(String contactId, String displayPage){   
    	ContactUtility.deleteContact(contactId,getContentResolver(),accountType);    	
    	loadPage(displayPage);
    }
    
    public void showAllContacts(String displayPage){    	
    	loadPage(displayPage);   	
    }
    
    public void showContact(String contactId, String displayPage){    	
    	loadPage(displayPage + Q + contactId);	
    }
    
    public void getContact(String contactId,String contactCallback){
    	String json = ContactUtility.getContactJSON(contactId, getContentResolver()); 
    	final String callbackFunction = JAVASCRIPT + contactCallback + BRC_OPEN + json 
    		+ BRC_CLOSE;
    	loadURL(callbackFunction); 	  	
    }

    public void addContact(String displayPage){    	
    	showContact(ContactUtility.EMPTY, displayPage);
    }
    
    public void getAllContacts(String callback, String accountCallback){    	
    	final String accountCallbackFunction = JAVASCRIPT + accountCallback + BRC;
    	
    	if(accountName == null){
    		loadURL(accountCallbackFunction);
    		return;
    	}
    	final String json = ContactUtility.getAllContactDisplaysJSON(getContentResolver());
    	final String callbackFunction = JAVASCRIPT + callback + BRC_OPEN + json + BRC_CLOSE;
    	loadURL(callbackFunction); 	
    }

    public void saveContact(String json, String displayPage){
    	ObjectMapper mapper = new ObjectMapper();
    	try{
    		Contact c = mapper.readValue(json,Contact.class);
    		ContactUtility.saveOrUpdateContact(c, getContentResolver(), accountName, accountType);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	loadPage(displayPage);
    }
}