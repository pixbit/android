package com.empsi.inepabor;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class PlistHandler extends DefaultHandler{
	public static final String TAG = PlistHandler.class.getSimpleName();
	
	// ===========================================================
	// Fields
	// ===========================================================

	private int plist_tag = 0;
	private int dict_tag = 0;
	private int array_tag = 0;
	private int key_tag = 0;
	private int string_tag = 0;
	
	private boolean in_title_key = false;
	private boolean in_scroll_key = false;
	private boolean in_view_key = false;
	
	private ParsedDataSet dataSet;
	public List<ParsedDataSet> entries = new ArrayList<ParsedDataSet>();
	

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ParsedDataSet getParsedData() {
		return this.dataSet;
	}
	
	public List<ParsedDataSet> getEntries() {
//		Log.d(TAG, "getEntries called.");
		return this.entries;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void startDocument() throws SAXException {
		Log.d(TAG, "startDocument called.");
		this.dataSet = new ParsedDataSet();
	}

	@Override
	public void endDocument() throws SAXException {
//		Log.d(TAG, "endDocument called.");
		// Nothing to do
	}

	/** Gets be called on opening tags like: 
	 * <tag> 
	 * Can provide attribute(s), when xml was like:
	 * <tag attribute="attributeValue">*/
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//		Log.d(TAG, "STARTING a " + localName + " tag.");
		
		if (localName.equals("plist")) {
			this.plist_tag++;
			Log.d(TAG, "+PLIST[" + plist_tag + "]");
		}else if (localName.equals("dict")) {
			if(this.dict_tag++ > 0){
				this.dataSet = new ParsedDataSet();
			}
			Log.d(TAG, "+DICT[" + dict_tag + "]");
		}else if (localName.equals("array")) {
			this.array_tag++;
			Log.d(TAG, "+ARRAY[" + array_tag + "]");
		}else if (localName.equals("key")) {
			this.key_tag++;
			Log.d(TAG, "+KEY[" + key_tag + "]");
		}else if (localName.equals("string")) {
			this.string_tag++;
			Log.d(TAG, "+STRING[" + string_tag + "]");
		}
	}
	
	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//		Log.d(TAG, "ENDING a " + localName + " tag.");
		
		if (localName.equals("plist")) {
			Log.d(TAG, "-PLIST[" + plist_tag + "]");
			this.array_tag--;
		}else if (localName.equals("dict")) {
			Log.d(TAG, "-DICT[" + dict_tag + "]");
			if(this.dict_tag-- > 1){
				this.entries.add(this.dataSet);
				Log.d(TAG, "Added entry for '" + this.dataSet.getTitle() + "'");
				this.dataSet = null;
			}
		}else if (localName.equals("array")) {
			Log.d(TAG, "-ARRAY[" + array_tag + "]");
			this.array_tag--;
		}else if (localName.equals("key")) {
			Log.d(TAG, "-KEY[" + key_tag + "]");
			this.key_tag--;
		}else if (localName.equals("string")) {
			Log.d(TAG, "-STRING[" + string_tag + "]");
			this.string_tag--;
		}
	}
	
	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
    public void characters(char ch[], int start, int length) {
		String charSet;
		
		if(this.key_tag > 0){
			charSet = new String(ch, start, length);
//			Log.d(TAG, "READ A KEY ["+ Integer.toString(array_tag) + "]: " + charSet);
			
			if(charSet.equals("Title")){
				this.in_title_key = true;
			}else if(charSet.equals("Scroll")){
				this.in_scroll_key = true;
			}else if(charSet.equals("View")){
				this.in_view_key = true;
			}
    			
			
		}else if(this.string_tag > 0){
			charSet = new String(ch, start, length);
//			Log.d(TAG, "READ A STRING [" + Integer.toString(array_tag) + "]: " + charSet);
			if(this.in_title_key){
				this.dataSet.setTitle(charSet);
				this.in_title_key = false;
			}else if(this.in_scroll_key){
				this.dataSet.setScroll(charSet);
				this.in_scroll_key = false;
			}else if(this.in_view_key){
				this.dataSet.setView(charSet);
				this.in_view_key = false;
			}
		}
    }
}