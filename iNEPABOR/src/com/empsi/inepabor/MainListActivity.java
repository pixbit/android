package com.empsi.inepabor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainListActivity extends ListActivity {

	public static final String TAG = MainListActivity.class.getSimpleName();
	public List<ParsedRow> entries;
	public List<ParsedRow> currentEntries;
	public List<ParsedRow> previousEntries;
	public List<String> titleList;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        
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
//            		Log.d(TAG, "Number of entries on this list: " + Integer.toString(entries.size()));
            		
            		this.currentEntries = this.entries;
            		/* Gets list of all the extracted strings */
            		this.titleList = new ArrayList<String>();
            		for(ParsedRow entry : currentEntries){
            			this.titleList.add(entry.getTitle());
            		}
                
                /* Set the result to be displayed in our GUI. */
//              Toast.makeText(this, parsedExampleDataSet.toString(), Toast.LENGTH_LONG).show();

                /* Set the result to be displayed in our GUI. */
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);
                setListAdapter(adapter);
               
        } catch (Exception e) {
                /* Display any Error to the GUI. */
                Log.e(TAG, "XMLQueryError", e);
        }
        /* Display the TextView. */
//        this.setContentView(tv);
//        Toast.makeText(this, "parsedExample", Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onListItemClick(ListView parent, View v, int position, long id) {
        Toast.makeText(this, this.currentEntries.get(position).getView(), Toast.LENGTH_SHORT).show();
        int view = Integer.parseInt(this.currentEntries.get(position).getView());
        
        String url = "http://www.google.com";
        switch(view){
        		case 1:
        			url = "file:///android_asset/handbook.htm";
        			break;
        		default:
        			url = "http://www.cnn.com";
        }
        
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setData(Uri.parse(url));
        startActivity(i);
    	
//    		this.previousEntries = this.currentEntries;
//    		this.currentEntries = this.currentEntries.get(position).getChildren();
//    		/* Gets list of all the extracted strings */
//    		this.titleList = new ArrayList<String>();
//    		for(ParsedRow entry : currentEntries){
//    			this.titleList.add(entry.getTitle());
//    		}
//        /* Set the result to be displayed in our GUI. */
//    		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);
//    		setListAdapter(adapter);
    	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_list, menu);
        return true;
    }
}