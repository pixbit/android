package com.empsi.inepabor;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainListActivity extends SherlockListActivity {

	public static final String TAG = MainListActivity.class.getSimpleName();
	public ActionBar actionBar;
	public List<ParsedRow> entries;
	public List<ParsedRow> previousEntries;
	public List<String> titleList;
	public String initialTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        
        actionBar = getSupportActionBar();
        actionBar.setIcon(android.R.drawable.ic_menu_revert);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initialTitle = "iNEPA BOR";
        
        try {
                /* Get a SAXParser from the SAXPArserFactory. */
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();

                /* Get the XMLReader of the SAXParser we created. */
                XMLReader xr = sp.getXMLReader();
                /* Create a new ContentHandler and apply it to the XML-Reader*/
                PlistHandler myHandler = new PlistHandler();
                xr.setContentHandler(myHandler);
                
                /* Parse the xml-data from our file. */
                Resources res = getResources();
                InputStream is = res.openRawResource(R.raw.data);
                xr.parse(new InputSource(is));
                /* Parsing has finished. */

                /* Our PlistHandler now provides the parsed data to us. */
            		this.entries = myHandler.getListOfRows();
                
                /* Set the result to be displayed in our GUI. */
//              Toast.makeText(this, parsedExampleDataSet.toString(), Toast.LENGTH_LONG).show();

                /* Set the result to be displayed in our GUI. */
                EmpsiAdapter adapter = new EmpsiAdapter(this, R.layout.empsi_custom_row, this.entries);
                setListAdapter(adapter);
               
        } catch (Exception e) {
                /* Display any Error to the GUI. */
                Log.e(TAG, "XMLQueryError", e);
        }
    }
    
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
//        Toast.makeText(this, this.currentEntries.get(position).getView(), Toast.LENGTH_SHORT).show();
    		ParsedRow clickedRow = EmpsiAdapter.currentData.get(position);
        int view = Integer.parseInt(clickedRow.getView());
        String title = clickedRow.getTitle();
        String scroll = clickedRow.getScroll();
        
        String url;
        switch(view){
        		case 1:
        			url = "file:///android_asset/usbr.htm";
        			break;
        		case 2:
        			url = "file:///android_asset/applicationTutorial.htm";
        			break;
        		case 3:
        			url = "file:///android_asset/bookmarkTutorial.htm";
        			break;
        		case 4:
        			url = "file:///android_asset/navigationTutorial.htm";
        			break;
        		default:
        			url = "http://www.empsi.com";
        }
        
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        
        i.putExtra("view", url);
        i.putExtra("title", title);
        i.putExtra("scroll", scroll);
        i.setData(Uri.parse(url));

        Log.d("INTENT", title);
        Log.d("INTENT", url);
        Log.d("INTENT", scroll);

//      Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        
        startActivity(i);
    	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
    		String title = "";
  	    switch (item.getItemId()) {
  	    	  case android.R.id.home:
  	    		/* Set the result to be displayed in our GUI. */
  	  	  	int indexOfLast = EmpsiAdapter.prevData.size() - 1;
  	  	    this.previousEntries = EmpsiAdapter.prevData.get(indexOfLast);
  	  	    EmpsiAdapter.prevData.remove(indexOfLast);
            EmpsiAdapter adapter = new EmpsiAdapter(this, R.layout.empsi_custom_row, this.previousEntries);
            setListAdapter(adapter);

  	  	  	int indexOfLastTitle = EmpsiAdapter.prevData.size() - 1;
//  	  	    Toast.makeText(this, String.format("index: %d", indexOfLastTitle), Toast.LENGTH_LONG).show();
  	  	    if(indexOfLastTitle < 0){
  	  	    		title = this.initialTitle;
  	  	    }else{
  	  	  	    title = EmpsiAdapter.prevTitle.get(indexOfLastTitle);
  	  	  	    EmpsiAdapter.prevTitle.remove(indexOfLastTitle);
  	  	    }
            actionBar.setTitle(title);
            
  	        break;
  	      default:
  	    	  	finish();
  	  	    Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
  	    }

  	    return true;
  	}
}