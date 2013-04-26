package com.empsi.iesa;

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

	Context mContext;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		/* Sets variable so that Preferences can be loaded and saved as in JavaScriptInterface */
		mContext = getActivity();

		try {
			/* Our PlistHandler now provides the parsed data to us. */
			//                this.entries = myHandler.getListOfRows();
			List<ParsedRow> bookmarkList = new ArrayList<ParsedRow>();
			int icon = android.R.drawable.ic_menu_delete;
//			int icon = android.R.drawable.ic_menu_close_clear_cancel;
			
			/* Loads the Preference for Number of Bookmarks */
			String bmCount = LoadPreference("bmCount", "-1");
			Log.d(TAG, "----------------------------");
			Log.d(TAG, "bmCount: " + bmCount);
			for(int i = 0; i <= Integer.parseInt(bmCount); i++){
				String bmTitle = LoadPreference("bmTitle"+i, "bmTitle");
				String bmScroll = LoadPreference("bmScroll"+i, "bmScroll");

				ParsedRow bmRow = new ParsedRow();
				bmRow.setTitle(bmTitle + "(" + bmScroll + ")" + " " + i + " of " + bmCount);
				bmRow.setView("3");
				bmRow.setScroll(bmScroll);
				bmRow.setIcon(icon);
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d(TAG, "onDestroyView");
		setListAdapter(null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setHasOptionsMenu(true);
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
			Toast.makeText(getActivity(), "edit Bookmark", Toast.LENGTH_LONG).show();
	    break;
		default:
			getActivity().finish();
			Toast.makeText(getActivity(), "home pressed", Toast.LENGTH_LONG).show();
		}
		return true;
	}

	private void SavePreference(String key, String value){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private String LoadPreference(String key, String alternative){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
		String strSaved = sharedPreferences.getString(key, alternative);
		return strSaved;
	}

}