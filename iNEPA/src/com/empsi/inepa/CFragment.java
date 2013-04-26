package com.empsi.inepa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CFragment extends SherlockListFragment {

	public static final String TAG = CFragment.class.getSimpleName();
	public ActionBar actionBar;
	public List<ParsedRow> entries;
	public List<ParsedRow> previousEntries;
	public List<String> titleList;
	public String initialTitle;
	public static boolean editModeOn = false;

	Context mContext;
	List<ParsedRow> bookmarkList;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		/* Sets variable so that Preferences can be loaded and saved as in JavaScriptInterface */
		mContext = getActivity();

		try {
			/* Our PlistHandler now provides the parsed data to us. */
			//                this.entries = myHandler.getListOfRows();
			bookmarkList = new ArrayList<ParsedRow>();

			/* Loads the Preference for Number of Bookmarks */
			String bmCount = LoadPreferenceString("bmCount", "-1");
			Log.d(TAG, "----------------------------");
			Log.d(TAG, "bmCount: " + bmCount);
			for(int i = 0; i <= Integer.parseInt(bmCount); i++){
				String bmTitle = LoadPreferenceString("bmTitle"+i, "bmTitle");
				String bmScroll = LoadPreferenceString("bmScroll"+i, "bmScroll");

				ParsedRow bmRow = new ParsedRow();
				bmRow.setTitle(bmTitle + "(" + bmScroll + ")" + " " + i + " of " + bmCount);
				bmRow.setView("3");
				bmRow.setScroll(bmScroll);
				//				bmRow.setIcon(icon);
				bmRow.setIndex(i);
				bookmarkList.add(bmRow);
			}

			ParsedRow row1 = new ParsedRow();
			row1.setTitle("Application Tutorial");
			row1.setView("5");
			row1.setScroll("0");
			bookmarkList.add(row1);

			ParsedRow row2 = new ParsedRow();
			row2.setTitle("Bookmark Tutorial");
			row2.setView("6");
			row2.setScroll("0");
			bookmarkList.add(row2);

			ParsedRow row3 = new ParsedRow();
			row3.setTitle("Navigation Tutorial");
			row3.setView("7");
			row3.setScroll("0");
			bookmarkList.add(row3);

			/* Set the result to be displayed in our GUI. */
			BookmarkAdapter adapter = new BookmarkAdapter(getActivity(), R.layout.bookmark_custom_row, bookmarkList, this);
			setListAdapter(adapter);

		} catch (Exception e) {
			/* Display any Error to the GUI. */
			Log.e(TAG, "XMLQueryError", e);
		}
	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {
		if(CFragment.editModeOn){
			Log.d( TAG, String.format("Position clicked = %d", position) );

			try {
				/* Our PlistHandler now provides the parsed data to us. */
				//                this.entries = myHandler.getListOfRows();
				List<ParsedRow> bookmarkList = new ArrayList<ParsedRow>();
				int icon = android.R.drawable.ic_menu_delete;
				//    			int icon = android.R.drawable.ic_menu_close_clear_cancel;
				String bmCount = LoadPreferenceString("bmCount", "-1");

				/* Saves Bookmarks without the deleted one. */
				Log.d(TAG, "---------Delete--------");
				Log.d(TAG, "bmCount before delete: " + bmCount);
				
				for(int i = 0; i <= Integer.parseInt(bmCount); i++){
					String bmTitle = LoadPreferenceString("bmTitle"+i, "bmTitle");
					String bmScroll = LoadPreferenceString("bmScroll"+i, "bmScroll");

					if(i < position){
						this.editBookmark(bmTitle, bmScroll, i);
					}else if(i == position){
						Log.d(TAG, "deleteBookmark " + bmTitle + "[" + i + "] =" + bmScroll);
						DeletePreference("bmTitle"+i);
						DeletePreference("bmScroll"+i);
					}else if(i > position){
						this.editBookmark(bmTitle, bmScroll, i-1);
					}else{

					}
				}

				int theCount = Integer.parseInt(bmCount) - 1;
				Log.d(TAG, "theCount = " + theCount);
				SavePreferenceString("bmCount", String.valueOf(theCount));

				/* Loads the Preference for Number of Bookmarks */
				Log.d(TAG, "----------Load----------");
				Log.d(TAG, "bmCount: " + bmCount);
				for(int i = 0; i <= Integer.parseInt(bmCount); i++){
					String bmTitle = LoadPreferenceString("bmTitle"+i, "bmTitle");
					String deleteTitle = bmTitle + " [tap to be deleted]";
					String bmScroll = LoadPreferenceString("bmScroll"+i, "bmScroll");

					ParsedRow bmRow = new ParsedRow();
					bmRow.setTitle(deleteTitle + "(" + bmScroll + ")" + " " + i + " of " + bmCount);
					bmRow.setView("3");
					bmRow.setScroll(bmScroll);
					bmRow.setIcon(icon);
					bmRow.setIndex(i);
					bookmarkList.add(bmRow);
				}

				/* Set the result to be displayed in our GUI. */
				BookmarkAdapter adapter = new BookmarkAdapter(getActivity(), R.layout.bookmark_custom_row, bookmarkList, this);
				setListAdapter(null);
				setListAdapter(adapter);

			} catch (Exception e) {
				/* Display any Error to the GUI. */
				Log.e(TAG, "XMLQueryError", e);
			}
		}else{
			//	        Toast.makeText(this, this.currentEntries.get(position).getView(), Toast.LENGTH_SHORT).show();
			ParsedRow clickedRow = BookmarkAdapter.currentData.get(position);
			int view = Integer.parseInt(clickedRow.getView());
			String title = clickedRow.getTitle();
			String scroll = clickedRow.getScroll();

			FileList fl = new FileList();
			String url = fl.getURL(view);

			Intent i = new Intent(getActivity(), MainActivity.class);

			i.putExtra("view", url);
			i.putExtra("title", title);
			i.putExtra("scroll", scroll);
			i.setData(Uri.parse(url));

			Log.d(TAG, "INTENT: " + title);
			Log.d(TAG, "INTENT: " + url);
			Log.d(TAG, "INTENT: " + scroll);

			//	        Toast.makeText(getActivity(), scroll + url, Toast.LENGTH_SHORT).show();

			startActivity(i);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		setListAdapter(null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Add your menu entries here
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.layout.bookmark_menu, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_bookmark:
			if(CFragment.editModeOn){
				CFragment.editModeOn = false;
			}else{
				CFragment.editModeOn = true;
			}
			
			//			Toast.makeText(getActivity(), "edit Bookmark", Toast.LENGTH_LONG).show();
			try {
				/* Our PlistHandler now provides the parsed data to us. */
				//                this.entries = myHandler.getListOfRows();
				List<ParsedRow> bookmarkList = new ArrayList<ParsedRow>();
				int icon = android.R.drawable.ic_menu_delete;

				/* Loads the Preference for Number of Bookmarks */
				String bmCount = LoadPreferenceString("bmCount", "-1");
				Log.d(TAG, "----------------------------");
				Log.d(TAG, "bmCount: " + bmCount);
				for(int i = 0; i <= Integer.parseInt(bmCount); i++){
					String bmTitle = LoadPreferenceString("bmTitle"+i, "bmTitle");
					String deleteTitle = "";
					if(CFragment.editModeOn){
						deleteTitle = bmTitle + " [tap to be deleted]";
					}
					
					String bmScroll = LoadPreferenceString("bmScroll"+i, "bmScroll");

					ParsedRow bmRow = new ParsedRow();
					bmRow.setTitle(deleteTitle + "(" + bmScroll + ")" + " " + i + " of " + bmCount);
					bmRow.setView("3");
					bmRow.setScroll(bmScroll);
					if(CFragment.editModeOn){
						bmRow.setIcon(icon);
					}
					bmRow.setIndex(i);
					bookmarkList.add(bmRow);
				}


				if(!CFragment.editModeOn){
					ParsedRow row1 = new ParsedRow();
					row1.setTitle("Application Tutorial");
					row1.setView("5");
					row1.setScroll("0");
					bookmarkList.add(row1);

					ParsedRow row2 = new ParsedRow();
					row2.setTitle("Bookmark Tutorial");
					row2.setView("6");
					row2.setScroll("0");
					bookmarkList.add(row2);

					ParsedRow row3 = new ParsedRow();
					row3.setTitle("Navigation Tutorial");
					row3.setView("7");
					row3.setScroll("0");
					bookmarkList.add(row3);
				}

				/* Set the result to be displayed in our GUI. */
				BookmarkAdapter adapter = new BookmarkAdapter(getActivity(), R.layout.bookmark_custom_row, bookmarkList, this);
				setListAdapter(null);
				setListAdapter(adapter);

			} catch (Exception e) {
				/* Display any Error to the GUI. */
				Log.e(TAG, "XMLQueryError", e);
			}
			break;
		default:
			getActivity().finish();
			Toast.makeText(getActivity(), "home pressed", Toast.LENGTH_LONG).show();
		}
		return true;
	}
	
    public void editBookmark(String title, String scroll, int index){
		Log.d(TAG, "addBookmark " + title + "[" + index + "] =" + scroll);
    	/*Sets the Number of Bookmarks in Preferences*/
    	SavePreferenceString("bmTitle"+String.valueOf(index), title);
    	SavePreferenceString("bmScroll"+String.valueOf(index), scroll);
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
    Activity activity = (Activity) mContext;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
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
    Activity activity = (Activity) mContext;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    String strSaved = sharedPreferences.getString(key, alternative);
    return strSaved;
  }
}