package com.empsi.inepabor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsedExampleDataSet {
	private String extractedString = null;
	private int extractedInt = 0;
	private Map<String, String> extractedStrings = new HashMap<String, String>();

	public String getExtractedString() {
		return extractedString;
	}
	
	public void setExtractedString(String extractedString) {
		this.extractedString = extractedString;
//		Log.d("ParsedExampleDataSet.setExtractedString()", extractedString);
	}

	public int getExtractedInt() {
		return extractedInt;
	}
	public void setExtractedInt(int extractedInt) {
		this.extractedInt = extractedInt;
//		Log.d("ParsedExampleDataSet.setExtractedInt()", Integer.toString(this.extractedInt));
	}
	
	public String toString(){
		return "ExtractedString = " + this.extractedString
				+ "nExtractedInt = " + this.extractedInt;
	}
	
	public Map<String, String> getMap(){
		return this.extractedStrings;
	}
}