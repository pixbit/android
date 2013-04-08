package com.empsi.inepa;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class JavaScriptInterface {
	private static final String TAG = JavaScriptInterface.class.getSimpleName();
    public static String scrollJSONArray;
	public static ArrayList<String> scrollBookmarks;
	
	WebView webview;
	TextView searchString;
	RelativeLayout searchOverlay;
	Button prevButton;
	Button nextButton;
	Button doneButton;
	
	Context mContext;
    public String searchScrollValues = "";
    
    public static int searchCount = 0;
	public static int searchIndex = 1;
	
	public String currentScrollValue = "-1";
	public EditText input;

    public JavaScriptInterface(Context c, WebView webview, RelativeLayout searchOverlay, TextView searchString, Button prevButton, Button nextButton, Button doneButton) {
        this.mContext = c;
        this.webview = webview;
        this.searchString = searchString;
        this.searchOverlay = searchOverlay;
        this.prevButton = prevButton;
        this.nextButton = nextButton;
        this.doneButton = doneButton;
    }
    
    public void onBookmarkClick(){
//      	webview.loadUrl("javascript:MainActivity.setCurrentScroll('hello');");
      	webview.loadUrl("javascript:MainActivity.setCurrentScroll(window.pageYOffset);");
    }

    @JavascriptInterface
    public void setCurrentScroll(String scroll){
    	currentScrollValue = scroll;
    	
		input = new EditText(this.mContext);
    	input.setText(currentScrollValue);

		new AlertDialog.Builder(this.mContext)
	    .setTitle("Add Bookmark")
	    .setMessage("Name Bookmark")
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            Editable inputText = input.getText();
	            webview.loadUrl(inputText.toString());
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            // Do nothing.
	        }
	    }).show();
    }
    
    public void submitSearch(String query){
    	Log.d(TAG, "webview.loadUrl's RUN");
  	  	webview.loadUrl("javascript:MyApp_HighlightAllOccurencesOfString(\""+query+"\");");
  	  	webview.loadUrl("javascript:MainActivity.setSearchCount(jQuery('.MyAppHighlight').size());");
  	  	webview.loadUrl("javascript:getSearchScrollValues();");
    }
    
    public void clickPrevButton(){
    	Log.d(TAG, "clickPrevButton");
    	
    	if(searchIndex == 0){
    		searchIndex = searchCount - 1;
    	}else{
    		searchIndex--;
    	}
    	searchString.setText((searchIndex+1) + " of " + searchCount);
        webview.loadUrl("javascript:jQuery('html, body').scrollTop("+ JavaScriptInterface.scrollBookmarks.get(searchIndex) +");");
    }
    
    public void clickNextButton(){
    	Log.d(TAG, "clickNextButton");
    	
    	if(searchIndex == (searchCount - 1)){
    		searchIndex = 0;
    	}else{
    		searchIndex++;
    	}
    	searchString.setText((searchIndex+1) + " of " + searchCount);
        webview.loadUrl("javascript:jQuery('html, body').scrollTop("+ JavaScriptInterface.scrollBookmarks.get(searchIndex) +");");
    }
    
    public void clickDoneButton(){
    	Log.d(TAG, "clickDoneButton");
        searchOverlay.setVisibility(View.INVISIBLE);
        webview.loadUrl("javascript:MyApp_RemoveAllHighlights();");
    }

    @JavascriptInterface
    public void showToast(String toast){
    	Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void setSearchCount(String message){
    	searchCount = Integer.parseInt(message);
    	searchIndex = 0;
    	searchString.setText((searchIndex+1) + " of " + searchCount);

        webview.loadUrl("javascript:jQuery('html, body').scrollTop("+ JavaScriptInterface.scrollBookmarks.get(searchIndex) +");");
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