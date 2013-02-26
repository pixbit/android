package com.empsi.inepabor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {
	protected String view = null; 
	protected String title = null; 
	protected String scroll = null; 
	public ProgressBar progressBar;
	public ProgressBar progressSpinner;

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
        
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        
        progressSpinner = (ProgressBar)findViewById(R.id.progressBar1);
        progressSpinner.setProgress(0);
        progressSpinner.setVisibility(View.VISIBLE);
        
        Intent receiveIntent = getIntent();
//        String content = receiveIntent.getData().toString();
//        WebView engine = (WebView) findViewById(R.id.web_engine);
//        engine.loadUrl(content);
        
        Bundle extras = receiveIntent.getExtras();
        if (extras == null) {
            return;
        }
        // Get data via the key
        view = extras.getString("view");
        title = extras.getString("title");
        scroll = extras.getString("scroll");
        if (view != null) {
            // Do something with the data
        		WebView webview = (WebView) findViewById(R.id.web_engine);
        		webview.getSettings().setJavaScriptEnabled(true);
        		webview.setWebChromeClient(new WebChromeClient(){
        		    public void onProgressChanged(WebView view, int progress) {
       		         progressBar.setProgress(progress);
       		         progressSpinner.setProgress(progress);
        		         if(progress == 100) {
        		             progressBar.setVisibility(View.GONE);
        		             progressSpinner.setVisibility(View.GONE);
        		             view.loadUrl("javascript:alert('Finished Loading " + scroll + "');");
        		             view.loadUrl("javascript:jQuery('html, body').scrollTop(jQuery('#"+ scroll +"').offset().top);");
        		          }
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

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    	  case android.R.id.home:
	    		finish();

//    			WebView webview = (WebView) findViewById(R.id.web_engine);
//    			webview.getSettings().setJavaScriptEnabled(true);
//    			webview.setWebChromeClient(new WebChromeClient());
//    			webview.loadUrl(view);
//	        webview.loadUrl("javascript:jQuery('html, body').scrollTop(jQuery('#"+ scroll +"').offset().top);");

//	        Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    }

	    return true;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_list, (android.view.Menu) menu);
        return true;
    }
    
}
