package com.empsi.inepa;

public class FileList {

	public String getURL(int view){
		String url;
		switch(view){
		case 1:
			url = "file:///android_asset/endangeredspeciesact.htm";
			break;
		case 2:
			url = "file:///android_asset/migratorybirdtreatyact.htm";
			break;
		case 3:
			url = "file:///android_asset/baldeagleprotectionact.htm";
			break;
		case 4:
			url = "file:///android_asset/fishandwildlifecoordinationact.htm";
			break;
		case 5:
			url = "file:///android_asset/applicationTutorial.htm";
			break;
		case 6:
			url = "file:///android_asset/bookmarkTutorial.htm";
			break;
		case 7:
			url = "file:///android_asset/navigationTutorial.htm";
			break;
		default:
			url = "http://www.empsi.com";
		}
		return url;
	}
}
