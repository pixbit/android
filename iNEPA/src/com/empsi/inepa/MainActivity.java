package com.empsi.inepa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.empsi.inepa.R;

public class MainActivity extends SherlockActivity {
	protected String view = null; 
	protected String title = null; 
	protected String scroll = null; 
	public ProgressBar progressBar;
	public ProgressBar progressSpinner;
	public ImageView overlay;

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

        getSupportActionBar().setIcon(android.R.drawable.ic_menu_revert);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        
        progressSpinner = (ProgressBar)findViewById(R.id.progressBar1);
        progressSpinner.setProgress(0);
        progressSpinner.setVisibility(View.VISIBLE);
        
        overlay = (ImageView)findViewById(R.id.imageView1);
        overlay.setBackgroundResource(android.R.color.black);
        overlay.setVisibility(View.VISIBLE);
        
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

//        Toast.makeText(this, view, Toast.LENGTH_LONG).show();
        if (view != null) {
            // Do something with the data
        		WebView webview = (WebView) findViewById(R.id.web_engine);
        		webview.getSettings().setJavaScriptEnabled(true);
        		webview.setWebChromeClient(new WebChromeClient(){
        		    public void onProgressChanged(WebView view, int progress) {
        		         Log.d("PROGRESS", String.format("Progress: %d", progress));
       		         progressBar.setProgress(progress);
       		         progressSpinner.setProgress(progress);
        		         if(progress == 100) {
        		             progressBar.setVisibility(View.GONE);
        		             progressSpinner.setVisibility(View.GONE);
        		             overlay.setVisibility(View.GONE);
//        		             view.loadUrl("javascript:alert('Finished Loading " + scroll + "');");
        		             view.loadUrl("javascript:jQuery('html, body').scrollTop(jQuery('#"+ scroll +"').offset().top);");
        		          }
        		       }
        		    });
        		webview.setVisibility(View.VISIBLE);
            webview.loadUrl(view);
        		Log.d("INTENT", "scroll: " + scroll);
        		Log.d("INTENT", "view: " + view);
        		Log.d("INTENT", "title: " + title);
        }
        if (title != null) {
            // Do something with the data
            setTitle(title);
        }
        if (scroll != null) {
            // Do something with the data
//            Toast.makeText(this, scroll, Toast.LENGTH_SHORT).show();
        }
        
    }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {

	    int itemId = item.getItemId();
	    switch (itemId) {
	    	  case android.R.id.home:
	    		finish();
//	        Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
	        break;
	    }
	    return true;
	}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_list, (android.view.Menu) menu);
//        return true;
//    }
    
}
