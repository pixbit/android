package com.empsi.inepa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;
  
public class CFragment extends SherlockListFragment {

	public static final String TAG = AFragment.class.getSimpleName();
	public ActionBar actionBar;
	public List<ParsedRow> entries;
	public List<ParsedRow> previousEntries;
	public List<String> titleList;
	public String initialTitle;
	
	 @Override
	    public void onViewCreated(View view, Bundle savedInstanceState) {
	        super.onViewCreated(view, savedInstanceState);
	        
	        try {
	        	/* Loads the Preference for Number of Bookmarks */
                SharedPreferences sharedPref = getActivity().getSharedPreferences("numberOfBookmarks", Context.MODE_PRIVATE);
                String numberOfBookmarks = sharedPref.getString("numberOfBookmarks", "+++");
                String bookmarkTitle = sharedPref.getString("bookmarkTitle", "---");
	        	
                /* Our PlistHandler now provides the parsed data to us. */
//                this.entries = myHandler.getListOfRows();
                List<ParsedRow> bookmarkList = new ArrayList<ParsedRow>();
                
                ParsedRow row1 = new ParsedRow();
                row1.setTitle("Application Tutorial");
                row1.setView("3");
                row1.setScroll("150");
                bookmarkList.add(row1);

                ParsedRow row2 = new ParsedRow();
                row2.setTitle("Bookmark Tutorial");
                row2.setView("4");
                row2.setScroll("0");
                bookmarkList.add(row2);
                
                ParsedRow row3 = new ParsedRow();
                row3.setTitle("Navigation Tutorial");
                row3.setView("5");
                row3.setScroll("0");
                bookmarkList.add(row3);
                
                ParsedRow row4 = new ParsedRow();
                row4.setTitle(bookmarkTitle + " " + numberOfBookmarks);
                row4.setView("5");
                row4.setScroll("0");
                bookmarkList.add(row4);

                /* Set the result to be displayed in our GUI. */
                BookmarkAdapter adapter = new BookmarkAdapter(getActivity(), R.layout.empsi_custom_row, bookmarkList, this);
                setListAdapter(adapter);
               
	        } catch (Exception e) {
                /* Display any Error to the GUI. */
                Log.e(TAG, "XMLQueryError", e);
	        }
	    }
	 
	    @Override
	    public void onListItemClick(ListView parent, View v, int position, long id) {
//	        Toast.makeText(this, this.currentEntries.get(position).getView(), Toast.LENGTH_SHORT).show();
	    		ParsedRow clickedRow = EmpsiAdapter.currentData.get(position);
	        int view = Integer.parseInt(clickedRow.getView());
	        String title = clickedRow.getTitle();
	        String scroll = clickedRow.getScroll();
	        
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
	        
	        Intent i = new Intent(getActivity(), MainActivity.class);
	        
	        i.putExtra("view", url);
	        i.putExtra("title", title);
	        i.putExtra("scroll", scroll);
	        i.setData(Uri.parse(url));

	        Log.d("INTENT", title);
	        Log.d("INTENT", url);
	        Log.d("INTENT", scroll);

//	        Toast.makeText(getActivity(), scroll + url, Toast.LENGTH_SHORT).show();
	        
//	        startActivity(i);
	    	}
	    
	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        setListAdapter(null);
	    }
	    
	    public boolean onOptionsItemSelected(MenuItem item) {
	  	    switch (item.getItemId()) {
	  	    	  case android.R.id.home:
	  	    		/* Set the result to be displayed in our GUI. */
	  	  	  	int indexOfLast = EmpsiAdapter.prevData.size() - 1;
	  	  	    this.previousEntries = EmpsiAdapter.prevData.get(indexOfLast);
	  	  	    EmpsiAdapter.prevData.remove(indexOfLast);
//	            EmpsiAdapter adapter = new EmpsiAdapter(getActivity(), R.layout.empsi_custom_row, this.previousEntries, this);
//	            setListAdapter(adapter);
	            
	  	        break;
	  	      default:
	  	    	  	getActivity().finish();
	  	  	    Toast.makeText(getActivity(), "home pressed", Toast.LENGTH_LONG).show();
	  	    }
	  	    return true;
	  	}
	}