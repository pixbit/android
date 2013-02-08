package com.empsi.inepabor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class ExampleHandler extends DefaultHandler{

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean in_dict_tag = false;
	private boolean in_array_tag = false;
	private boolean in_key_tag = false;
	private boolean is_string_tag = false;
	private int position = 0;
	
	private ParsedExampleDataSet myParsedExampleDataSet = new ParsedExampleDataSet();

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ParsedExampleDataSet getParsedData() {
		return this.myParsedExampleDataSet;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void startDocument() throws SAXException {
		this.myParsedExampleDataSet = new ParsedExampleDataSet();
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	/** Gets be called on opening tags like: 
	 * <tag> 
	 * Can provide attribute(s), when xml was like:
	 * <tag attribute="attributeValue">*/
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("dict")) {
			this.in_dict_tag = true;
			Log.d("startElement", "<dict>");
			
		}else if (localName.equals("array")) {
			this.in_array_tag = true;
			Log.d("startElement", "<array>");
			
		}else if (localName.equals("key")) {
			this.in_key_tag = true;
			Log.d("startElement", "<key>");
			
		}else if (localName.equals("string")) {
			this.is_string_tag = true;
			Log.d("startElement", "<string>");
//          myParsedExampleDataSet.setExtractedInt(i);
		}
	}
	
	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		if (localName.equals("dict")) {
			this.in_dict_tag = false;
			Log.d("endElement", "</dict>");
			
		}else if (localName.equals("array")) {
			this.in_array_tag = false;
			Log.d("endElement", "</array>");
			
		}else if (localName.equals("key")) {
			this.in_key_tag = false;
			Log.d("endElement", "</key>");
			
		}else if (localName.equals("string")) {
			this.is_string_tag = false;
			Log.d("endElement", "</string>");
//          myParsedExampleDataSet.setExtractedInt(i);
		}
	}
	
	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
    public void characters(char ch[], int start, int length) {
		String charSet = new String(ch, start, length);
		
		if(this.in_key_tag){
			Log.d("ch[]", charSet);
    		myParsedExampleDataSet.setExtractedString(new String(ch, start, length));
    	}else if(this.is_string_tag){
			Log.d("ch[]", charSet);
    		myParsedExampleDataSet.setExtractedString(new String(ch, start, length));
    	}
    }
}