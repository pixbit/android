package com.empsi.inepa;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
  SharedPreferences sharedPref;
  
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
  
  public void onBookmarkClick(String viewText){
    Log.d(TAG, String.format("bmCount is: %d", this.getCount()));
    String js = "javascript:MainActivity.setCurrentScroll(window.pageYOffset,'" + viewText + "');";
    Log.d(TAG, js);
    webview.loadUrl(js);
  }

  @JavascriptInterface
  public void setCurrentScroll(String scroll, String view){
    currentScrollValue = scroll;
    
    input = new EditText(this.mContext);
    input.setText("");
    
    final String viewText = view;

    new AlertDialog.Builder(this.mContext)
      .setTitle("Add Bookmark")
      .setMessage("Name Bookmark")
      .setView(input)
      .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          Editable inputText = input.getText();
          String js = "javascript:MainActivity.setBookmark('" +
                      inputText +
                      "', window.pageYOffset, '" +
                      viewText +
                      "');";
          Log.d(TAG, js);
          webview.loadUrl(js);
        }
      })
      .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          // Do nothing.
        }
      }).show();
  }
    
  /**
   * Sets bookmark with title and scroll values using SharedPreferences
   * @param title  [name that shows up in the listView row]
   * @param scroll [Y scroll value of the bookmark]
   */
  @JavascriptInterface
  public void setBookmark(String title, String scroll, String view){
    if(this.getCount() < 0){
      this.incrementCount();
    }
    this.saveBookmarkPreference(title, scroll, view, this.getCount());
    this.incrementCount();
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
      scrollBookmarks = new ArrayList<String>();  
      parseJSON(scrollJSONArray, scrollBookmarks);
    }
    
    public void parseJSON(String json, ArrayList<String> aList) {
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
          aList.add(jsonArray.get(i).toString());
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

  ////////////////////////
  // Bookmark Functions //
  ////////////////////////

  public void removeAndRewriteBookmarkPreferences(int position){
    String bmCount = LoadPreferenceString("bmCount", "-1");
    this.resetCount();
    for(int i = 0; i <= Integer.parseInt(bmCount); i++){
      String bmTitle  = LoadPreferenceString("bmTitle"+i, "bmTitle");
      String bmScroll = LoadPreferenceString("bmScroll"+i, "bmScroll");
      String bmView   = LoadPreferenceString("bmView"+i, "bmView");
      if(i == position){
        Log.d(TAG, "skipped: " + bmTitle);
      }else{
        Log.d(TAG, "kept: " + bmTitle + " @ " + i);
        this.saveBookmarkPreference(bmTitle, bmScroll, bmView, i);
        this.incrementCount();
      }
    }

    // Pops/Deletes the last one that is leftover
    this.deleteBookmarkPreference(Integer.parseInt(bmCount));
  }

  public void saveBookmarkPreference(String title, String scroll, String view, int index){
    Log.d(TAG, "saveBookmarkPreference " + title + "[" + index + "] =" + scroll);
    SavePreferenceString("bmTitle"+String.valueOf(index), title);
    SavePreferenceString("bmScroll"+String.valueOf(index), scroll);
    SavePreferenceString("bmView"+String.valueOf(index), view);
  }

  public void deleteBookmarkPreference(int index){
    Log.d(TAG, "deleteBookmarkPreference [" + index + "]");
    DeletePreference("bmTitle"+String.valueOf(index));
    DeletePreference("bmScroll"+String.valueOf(index));
    DeletePreference("bmView"+String.valueOf(index));
  }

  public int getCount(){
    return Integer.parseInt(LoadPreferenceString("bmCount", "-1"));
  }

  public void incrementCount(){
    String bmCount = LoadPreferenceString("bmCount", "-1");
    int theCount = Integer.parseInt(bmCount) + 1;
    Log.d(TAG, "Increment theCount to: " + theCount);
    SavePreferenceString("bmCount", String.valueOf(theCount));
  }

  public void decrementCount(){
    String bmCount = LoadPreferenceString("bmCount", "-1");
    int theCount = Integer.parseInt(bmCount) - 1;
    Log.d(TAG, "Decrement theCount to: " + theCount);
    SavePreferenceString("bmCount", String.valueOf(theCount));
  }

  public void resetCount(){
    DeletePreference("bmCount");
  }

  ////////////////////////////////
  // SharedPreference Functions //
  ////////////////////////////////

  /**
   * Deletes a single SharedPreference based on the key given.
   * 
   * @param key [lookup string for SharedPreference]
   */
  private void DeletePreference(String key){
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.remove(key);
    editor.commit();
  }
    
  /**
  * Saves a single SharedPreference String value to a String key.
  * 
  * @param key   [lookup string for SharedPreference]
  * @param value [value stored for SharedPreference]
  */
  private void SavePreferenceString(String key, String value){
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, value);
    editor.commit();
  }

  /**
  * Loads a single SharedPreference String value from a String key.
  * 
  * @param key          [lookup string for SharedPreference]
  * @param  alternative [string returned if not a SharedPreference]
  * @return             [loaded preference or alternative]
  */
  private String LoadPreferenceString(String key, String alternative){
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    String strSaved = sharedPreferences.getString(key, alternative);

    return strSaved;
  }
}