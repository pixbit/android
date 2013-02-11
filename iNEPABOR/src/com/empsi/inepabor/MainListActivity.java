package com.empsi.inepabor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainListActivity extends ListActivity {

	public static final String TAG = MainListActivity.class.getSimpleName();
	
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

                /* Parse the xml-data from our URL. */
//                URL url = new URL("files:///android_asset/Data.plist");
//                xr.parse(new InputSource(url.openStream()));
                /* Parsing has finished. */
                
                /* Parse the xml-data from our file. */
                Resources res = getResources();
                InputStream is = res.openRawResource(R.raw.data);
                xr.parse(new InputSource(is));
                /* Parsing has finished. */

                /* Our PlistHandler now provides the parsed data to us. */
                //ParsedDataSet dataSet = myHandler.getParsedData();
            		List<ParsedRow> entries = myHandler.getListOfRows();
//            		Log.d(TAG, "Number of entries parsed: " + Integer.toString(entries.size()));
            		
            		/* Gets list of all the extracted strings */
            		List<String> dataList = new ArrayList<String>();
            		for(ParsedRow entry : entries){
            			dataList.add(entry.getTitle());
            		}
                
                /* Set the result to be displayed in our GUI. */
//              Toast.makeText(this, parsedExampleDataSet.toString(), Toast.LENGTH_LONG).show();

                /* Set the result to be displayed in our GUI. */
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
                setListAdapter(adapter);
//                tv.setText(parsedExampleDataSet.toString());
               
        } catch (Exception e) {
                /* Display any Error to the GUI. */
//                tv.setText("Error: " + e.getMessage());
                Log.e(TAG, "XMLQueryError", e);
        }
        /* Display the TextView. */
//        this.setContentView(tv);
//        Toast.makeText(this, "parsedExample", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_list, menu);
        return true;
    }
}