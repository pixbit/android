package com.empsi.inepa;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookmarkAdapter extends ArrayAdapter<ParsedRow> {
	public static final String TAG = BookmarkAdapter.class.getSimpleName();

	Context activity;
	CFragment listfragment;
	int layoutResourceId;    
	List<ParsedRow> data = null;

	public static List<ParsedRow> currentData = null;
	public static ArrayList<List<ParsedRow>> prevData = new ArrayList<List<ParsedRow>>();
	public static ArrayList<String> prevTitle = new ArrayList<String>();

	public BookmarkAdapter(Context context, int layoutResourceId, List<ParsedRow> data, CFragment cFragment) {
		super(context, layoutResourceId, data);
		this.layoutResourceId       = layoutResourceId;
		this.activity               = context;
		this.listfragment           = cFragment;
		this.data                   = data;
		BookmarkAdapter.currentData = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row         = convertView;
		RowHolder holder = null;

		if(row == null){
			LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder          = new RowHolder();
			holder.imgIcon  = (ImageView)row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);           
			holder.imgIcon.setOnClickListener(new BookmarkListener(row, data, position, activity, this.listfragment));

			row.setTag(holder);
		}else{
			holder = (RowHolder)row.getTag();
		}

		ParsedRow rowItem = data.get(position);
		holder.txtTitle.setText(rowItem.getTitle());
		holder.imgIcon.setImageResource(rowItem.getIcon());

		return row;
	}

	static class RowHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}

	public class BookmarkListener implements View.OnClickListener {
		Context context; 
		CFragment frag;
		List<ParsedRow> data     = null;
		List<ParsedRow> prevData = null;
		List<ParsedRow> nextData = null;
		private View mView;
		int position;

		public BookmarkListener(View v, List<ParsedRow> data, int position, Context context, CFragment frag) {
			this.data     = data;
			this.position = position;
			this.context  = context;
			this.frag     = frag;
			setmView(v);
		}

		@Override
		public void onClick(View v) {
			Log.d( TAG, String.format("Position clicked = %d", this.position) );
			Log.d( TAG, String.format("bmIndex clicked = %d", this.data.get(position).getIndex()) );

			try {
				/* Our PlistHandler now provides the parsed data to us. */
				//                this.entries = myHandler.getListOfRows();
				List<ParsedRow> bookmarkList = new ArrayList<ParsedRow>();
				int icon = android.R.drawable.ic_menu_delete;
				//    			int icon = android.R.drawable.ic_menu_close_clear_cancel;
				String bmCount = LoadPreference("bmCount", "-1");

				/* Saves Bookmarks without the deleted one. */
				Log.d(TAG, "---------Delete--------");
				Log.d(TAG, "bmCount: " + bmCount);
				for(int i = 0; i <= Integer.parseInt(bmCount); i++){
					String bmTitle = LoadPreference("bmTitle"+i, "bmTitle");
					String bmScroll = LoadPreference("bmScroll"+i, "bmScroll");

					if(i < this.position){
						this.editBookmark(bmTitle, bmScroll, i);
					}else if(i == this.position){
						Log.d(TAG, "deleteBookmark " + bmTitle + "[" + i + "] =" + bmScroll);
					}else if(i > this.position){
						this.editBookmark(bmTitle, bmScroll, i-1);
					}else{
						
					}
				}
				
				int theCount = Integer.parseInt(bmCount) - 1;
				Log.d(TAG, "theCount = " + theCount);
		    	SavePreference("bmCount", String.valueOf(theCount));

				/* Loads the Preference for Number of Bookmarks */
				Log.d(TAG, "----------Load----------");
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
				BookmarkAdapter adapter = new BookmarkAdapter(this.context, R.layout.bookmark_custom_row, bookmarkList, this.frag);
				this.frag.setListAdapter(null);
				this.frag.setListAdapter(adapter);

			} catch (Exception e) {
				/* Display any Error to the GUI. */
				Log.e(TAG, "XMLQueryError", e);
			}

			//    		BookmarkAdapter.prevData.add(this.data);
			//    		this.nextData = this.data.get(position).getChildren();
			/* Set the result to be displayed in our GUI. */
			//    		BookmarkAdapter adapter = new BookmarkAdapter(this.context, R.layout.empsi_custom_row, this.nextData, this.frag);
			//    		this.frag.setListAdapter(adapter);  

			//    			EmpsiAdapter.prevTitle.add(this.data.get(position).getTitle());
			//    			((MainListActivity) this.context).actionBar.setTitle(this.data.get(position).getTitle()); 
		}

		public View getmView() {
			return mView;
		}

		public void setmView(View mView) {
			this.mView = mView;
		}
		
	    public void editBookmark(String title, String scroll, int index){
			Log.d(TAG, "addBookmark " + title + "[" + index + "] =" + scroll);
	    	/*Sets the Number of Bookmarks in Preferences*/
	    	SavePreference("bmTitle"+String.valueOf(index), title);
	    	SavePreference("bmScroll"+String.valueOf(index), scroll);
	    }

		private void SavePreference(String key, String value){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}

		private String LoadPreference(String key, String alternative){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
			String strSaved = sharedPreferences.getString(key, alternative);
			return strSaved;
		}
	}
}