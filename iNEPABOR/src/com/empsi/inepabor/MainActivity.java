package com.empsi.inepabor;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();
	public List<ParsedRow> entries;
	public List<ParsedRow> currentEntries;
	public List<ParsedRow> previousEntries;
	public List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
//        		Log.d(TAG, "Number of entries on this list: " + Integer.toString(entries.size()));
        		
        		this.currentEntries = this.entries;
            
            /* Set the result to be displayed in our GUI. */
//          Toast.makeText(this, parsedExampleDataSet.toString(), Toast.LENGTH_SHORT).show();
           
     	} catch (Exception e) {
        		/* Display any Error to the GUI. */
        		Log.e(TAG, "XMLQueryError", e);
        	}
        
        Intent receiveIntent = getIntent();
//        String content = receiveIntent.getData().toString();
//        WebView engine = (WebView) findViewById(R.id.web_engine);
//        engine.loadUrl(content);
        
        Bundle extras = receiveIntent.getExtras();
        if (extras == null) {
            return;
        }
        // Get data via the key
        String view = extras.getString("view");
        String title = extras.getString("title");
        String scroll = extras.getString("scroll");
        if (view != null) {
            // Do something with the data
        		WebView webview = (WebView) findViewById(R.id.web_engine);
        		webview.getSettings().setJavaScriptEnabled(true);
        		webview.setWebViewClient(new WebViewClient() {
        		    @Override
        		    public void onPageFinished(WebView view, String url)
        		    {
        		        view.loadUrl("javascript:(function() { " +
        		                "document.getElementsByTagName('body')[0].style.color = 'red'; " +
        		                "})()");
        		    }
        		});
        		
        		
            webview.loadUrl(view);
        }
        if (title != null) {
            // Do something with the data
            setTitle(title);
        }
        if (scroll != null) {
            // Do something with the data
            Toast.makeText(this, scroll, Toast.LENGTH_SHORT).show();
        }
        
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_list, menu);
//        return true;
//    }
    
}
