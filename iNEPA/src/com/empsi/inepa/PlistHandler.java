package com.empsi.inepa;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PlistHandler extends DefaultHandler{
	public static final String TAG = PlistHandler.class.getSimpleName();
	
	// ===========================================================
	// Fields
	// ===========================================================

	private int depth = 0;
	
	private boolean in_title_key = false;
	private boolean in_scroll_key = false;
	private boolean in_view_key = false;
	
	private boolean in_string_tag = false;
	
	public List<ParsedRow> listOfRows;
	private ParsedRow rowDepth1;
	private ParsedRow rowDepth2;
	private ParsedRow rowDepth3;
	private ParsedRow rowDepth4;
	

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public List<ParsedRow> getListOfRows() {
//		Log.d(TAG, "getEntries called.");
		return this.listOfRows;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	@Override
	public void startDocument() throws SAXException {
//		Log.d(TAG, "startDocument called.");
		this.listOfRows = new ArrayList<ParsedRow>();
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
//			Log.d(TAG, "+PLIST[" + depth + "]");
			
		}else if (localName.equals("dict")) {
//			Log.d(TAG, "+DICT[" + depth + "]");
			if(this.depth == 1){
				// Allocate empty row
//				Log.d(TAG, "++ Allocate empty rowDepth1 ++");
				this.rowDepth1 = new ParsedRow();
			}else if(this.depth == 2){
				// Allocate empty row
//				Log.d(TAG, "++ Allocate empty rowDepth2 ++");
				this.rowDepth2 = new ParsedRow();
			}else if(this.depth == 3){
				// Allocate empty row
//				Log.d(TAG, "++ Allocate empty rowDepth3 ++");
				this.rowDepth3 = new ParsedRow();
			}else if(this.depth == 4){
				// Allocate empty row
//				Log.d(TAG, "++ Allocate empty rowDepth4 ++");
				this.rowDepth4 = new ParsedRow();
			}
			
		}else if (localName.equals("array")) {
//			Log.d(TAG, "+ARRAY[" + depth + "]");
			
			// Increment depth
//			Log.d(TAG, "++ Increment depth ++");
			this.depth++;
			
		}else if (localName.equals("key")) {
//			Log.d(TAG, "+KEY[" + depth + "]");
			
		}else if (localName.equals("string")) {
//			Log.d(TAG, "+STRING[" + depth + "]");
			this.in_string_tag = true;
			
		}
	}
	
	/** Gets be called on closing tags like: 
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//		Log.d(TAG, "ENDING a " + localName + " tag.");
		int icon = android.R.drawable.ic_menu_sort_by_size;
		
		if (localName.equals("plist")) {
//			Log.d(TAG, "-PLIST[" + plist_depth + "]");
			
		}else if (localName.equals("dict")) {
//			Log.d(TAG, "-DICT[" + depth + "]");
			if(this.depth == 1){
				// Add row to temp list
//				Log.d(TAG, "Add row to temp list '" + this.rowDepth1.getTitle() + "'");
				this.listOfRows.add(this.rowDepth1);
				
				// Release row
//				Log.d(TAG, "++ Release row ++");
				this.rowDepth1 = null;
				
			}else if(this.depth == 2){
				// Add row to temp list
//				Log.d(TAG, "Add rowDepth2 to rowDepth1's children '" + this.rowDepth1.getTitle() + "'");
				this.rowDepth1.addChild(this.rowDepth2);
				this.rowDepth1.setIcon(icon);
				
				// Release row
//				Log.d(TAG, "++ Release row ++");
				this.rowDepth2 = null;
				
			}else if(this.depth == 3){
				// Add row to temp list
//				Log.d(TAG, "Add rowDepth3 to rowDepth2's children '" + this.rowDepth2.getTitle() + "'");
				this.rowDepth2.addChild(this.rowDepth3);
				this.rowDepth2.setIcon(icon);
				
				// Release row
//				Log.d(TAG, "++ Release row ++");
				this.rowDepth3 = null;
				
			}else if(this.depth == 4){
				// Add row to temp list
//				Log.d(TAG, "Add rowDepth4 to rowDepth3's children '" + this.rowDepth3.getTitle() + "'");
				this.rowDepth3.addChild(this.rowDepth4);
				this.rowDepth3.setIcon(icon);
				
				// Release row
//				Log.d(TAG, "++ Release row ++");
				this.rowDepth4 = null;
				
			}
			
		}else if (localName.equals("array")) {
//			Log.d(TAG, "-ARRAY[" + depth + "]");
			// Decrement depth
//			Log.d(TAG, "++ Decrement depth ++");
			this.depth--;
				
		}else if (localName.equals("key")) {
//			Log.d(TAG, "-KEY[" + depth + "]");
			
		}else if (localName.equals("string")) {
//			Log.d(TAG, "-STRING[" + depth + "]");
			this.in_string_tag = false;
			
		}
	}
	
	/** Gets be called on the following structure: 
	 * <tag>characters</tag> */
	@Override
    public void characters(char ch[], int start, int length) {
		String charSet = new String(ch, start, length);

		if(charSet.equals("Title")){
			this.in_title_key = true;
		}else if(charSet.equals("Scroll")){
			this.in_scroll_key = true;
		}else if(charSet.equals("View")){
			this.in_view_key = true;
		}
			
		if(this.in_string_tag){
			if(this.depth == 1){
				if(this.in_title_key){
//					Log.d(TAG, "rowDepth1 title[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth1.setTitle(charSet);
					this.in_title_key = false;
				}else if(this.in_scroll_key){
//					Log.d(TAG, "rowDepth1 scroll[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth1.setScroll(charSet);
					this.in_scroll_key = false;
				}else if(this.in_view_key){
//					Log.d(TAG, "rowDepth1 view[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth1.setView(charSet);
					this.in_view_key = false;
				}
			}else if(this.depth == 2){
				if(this.in_title_key){
//					Log.d(TAG, "rowDepth2 title[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth2.setTitle(charSet);
					this.in_title_key = false;
				}else if(this.in_scroll_key){
//					Log.d(TAG, "rowDepth2 scroll[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth2.setScroll(charSet);
					this.in_scroll_key = false;
				}else if(this.in_view_key){
//					Log.d(TAG, "rowDepth2 view[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth2.setView(charSet);
					this.in_view_key = false;
				}
			}else if(this.depth == 3){
				if(this.in_title_key){
//					Log.d(TAG, "rowDepth3 title[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth3.setTitle(charSet);
					this.in_title_key = false;
				}else if(this.in_scroll_key){
//					Log.d(TAG, "rowDepth3 scroll[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth3.setScroll(charSet);
					this.in_scroll_key = false;
				}else if(this.in_view_key){
//					Log.d(TAG, "rowDepth3 view[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth3.setView(charSet);
					this.in_view_key = false;
				}
			}else if(this.depth == 4){
				if(this.in_title_key){
//					Log.d(TAG, "rowDepth4 title[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth4.setTitle(charSet);
					this.in_title_key = false;
				}else if(this.in_scroll_key){
//					Log.d(TAG, "rowDepth4 scroll[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth4.setScroll(charSet);
					this.in_scroll_key = false;
				}else if(this.in_view_key){
//					Log.d(TAG, "rowDepth4 view[" + Integer.toString(depth) + "]" + charSet);
					this.rowDepth4.setView(charSet);
					this.in_view_key = false;
				}
			}
		}
    }
}