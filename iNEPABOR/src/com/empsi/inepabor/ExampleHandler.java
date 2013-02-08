package com.empsi.inepabor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExampleHandler extends DefaultHandler{

	// ===========================================================
	// Fields
	// ===========================================================
	
	private boolean in_stringtag = false;
	
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
		if (localName.equals("string")) {
			this.in_stringtag = true;
		}else if (localName.equals("tagwithnumber")) {
            String attrValue = atts.getValue("thenumber");
            int i = Integer.parseInt(attrValue);
            myParsedExampleDataSet.setExtractedInt(i);
		}
	}
	
	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("string")) {
			this.in_stringtag = false;
		}
	}
	
	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
    public void characters(char ch[], int start, int length) {
		if(this.in_stringtag){
    		myParsedExampleDataSet.setExtractedString(new String(ch, start, length));
    	}
    }
}