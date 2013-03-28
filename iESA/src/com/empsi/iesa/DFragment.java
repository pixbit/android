package com.empsi.iesa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
  
public class DFragment extends SherlockFragment {
	public ProgressBar progressBar;
	public ProgressBar progressSpinner;
	public ImageView overlay;
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.activity_main, container, false);

	        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
	        progressBar.setProgress(0);
	        progressBar.setVisibility(View.VISIBLE);
	        
	        progressSpinner = (ProgressBar) v.findViewById(R.id.progressBar1);
	        progressSpinner.setProgress(0);
	        progressSpinner.setVisibility(View.VISIBLE);
	        
	        overlay = (ImageView) v.findViewById(R.id.imageView1);
	        overlay.setBackgroundResource(android.R.color.black);
	        overlay.setVisibility(View.VISIBLE);
	        
	        String url = "file:///android_asset/about.htm";
	        
	        WebView webview = (WebView) v.findViewById(R.id.web_engine);
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
//    		             view.loadUrl("javascript:alert('Finished Loading');");
    		          }
    		       }
    		    });
    			webview.setVisibility(View.VISIBLE);
    			webview.loadUrl(url);

	        return v;
	    }
	}