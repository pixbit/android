package com.empsi.inepabor;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

	public class JavaScriptHandler {
	    MainActivity parentActivity;
	    public JavaScriptHandler(MainActivity activity) {
	        parentActivity = activity;
	    }
	 
	    public void setResult(int val){
//	        this.parentActivity.javascriptCallFinished(val);
	    }
	 
	    public void calcSomething(int x, int y){
//	        this.parentActivity.changeText("Result is : " + (x * y));
	    }
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setIcon(R.drawable.mr_bit);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
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
        		webview.setWebViewClient(new WebViewClient());
            webview.loadUrl(view);
            webview.loadUrl("javascript:jQuery('html, body').scrollTop(jQuery('#bookmark60').offset().top);");
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

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    case android.R.id.home:
//	        toggle();
	    		finish();
	        Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    }

	    return true;
	}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_list, menu);
//        return true;
//    }
    
}
