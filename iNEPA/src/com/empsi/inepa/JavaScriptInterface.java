package com.empsi.inepa;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class JavaScriptInterface {
	private static final String TAG = JavaScriptInterface.class.getSimpleName();
    public static String scrollJSONArray;
	public static ArrayList<String> scrollBookmarks;
	public static int scrollIndex = 0;
	
	WebView web;
	TextView searchString;
	
	Context mContext;
    public String searchScrollValues = "";
    public static String searchCount = "";

    public JavaScriptInterface(Context c, WebView webview, TextView searchString) {
        this.mContext = c;
        this.web = webview;
        this.searchString = searchString;
    }
    
    public void submitSearch(String query){
    	Log.d(TAG, "webview.loadUrl's RUN");
  	  	web.loadUrl("javascript:MyApp_HighlightAllOccurencesOfString(\""+query+"\");");
  	  	web.loadUrl("javascript:MainActivity.setSearchCount(jQuery('.MyAppHighlight').size());");

//    	  if(JavaScriptInterface.scrollBookmarks.size() > 0){
//    		  webview.loadUrl("javascript:jQuery('html, body').scrollTop("+ JavaScriptInterface.scrollBookmarks.get(0) +");");
//    		  Log.d(TAG, "JavaScriptInterface.scrollIndex+1: " + JavaScriptInterface.scrollIndex+1);
//    		  searchResultString = String.format("%d of %d", JavaScriptInterface.scrollBookmarks.size(), JavaScriptInterface.scrollBookmarks.size());
//    		  Log.d(TAG, searchResultString);
//    		  searchString.setText(resultString);
//    	  }
    	  
    	  
//    		if(resultNumber > 0){
//    			resultString = String.format("%d of %d", searchIndex+1, resultNumber);
//    			Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
//    		}
//    		webview.loadUrl("javascript:jQuery('html, body').scrollTop(jQuery('#"+ query +"').offset().top);");
          

//    	    if(resultNumber > 0){
//    	        [self setScrollPostion:webView xValue:0 yValue:[[scrollArray objectAtIndex:0] integerValue]];
//    	        searchIndex = 0;
//    	        [scrollArray retain];
//    	    }
    	  
    	  
    	  
//    		if(resultNumber > 0){
//    			resultString = String.format("%d of %d", searchIndex+1, resultNumber);
//    			Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
//    		}
//    		webview.loadUrl("javascript:jQuery('html, body').scrollTop(jQuery('#"+ query +"').offset().top);");
//  	  webview.loadUrl("javascript:getSearchScrollValues();");
//  	  Log.d(TAG, "scrollJSONArray: " + JavaScriptInterface.scrollJSONArray);
  	  
    }

    @JavascriptInterface
    public void showToast(String toast){
    	Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void setSearchCount(String message){
    	searchString.setText(message);
    }

    @JavascriptInterface
    public void pushScrollValue(String value){
    	Log.d("pushScrollValue", value);
    	scrollJSONArray = value;
    	parseJSON(scrollJSONArray);
    }
    
    public void parseJSON(String json) {
  	  scrollBookmarks = new ArrayList<String>();  
  	  JSONArray jsonArray = null;
  	  try {
  		jsonArray = new JSONArray(json);
  	  } catch (JSONException e1) {
  		// TODO Auto-generated catch block
  		e1.printStackTrace();
  	  } 
  	  
  	  if (jsonArray != null) { 
  	     int len = jsonArray.length();
  	     for (int i=0;i<len;i++){ 
  	    	 try {
  				scrollBookmarks.add(jsonArray.get(i).toString());
  			} catch (JSONException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  	     } 
  	  }
      
  	  return;
    }


    @JavascriptInterface
    public void emptyScrollValues(){
    	JavaScriptInterface.scrollJSONArray = "";
    }
}