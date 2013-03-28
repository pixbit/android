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
  
public class BFragment extends SherlockFragment {
	public ProgressBar progressBar;
	public ProgressBar progressSpinner;
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.activity_main, container, false);

	        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
	        progressBar.setProgress(0);
	        progressBar.setVisibility(View.VISIBLE);
	        
	        progressSpinner = (ProgressBar) v.findViewById(R.id.progressBar1);
	        progressSpinner.setProgress(0);
	        progressSpinner.setVisibility(View.VISIBLE);
	        
	        String url = "http:///www.empsi.com/nepalinks.html";
	        
	        WebView webview = (WebView) v.findViewById(R.id.web_engine);
    			webview.setWebViewClient(new MyWebViewClient());
            webview.getSettings().setBuiltInZoomControls(false); 
            webview.getSettings().setSupportZoom(false);
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
            webview.getSettings().setAllowFileAccess(true); 
            webview.getSettings().setDomStorageEnabled(true);
    			webview.setVisibility(View.VISIBLE);
    			webview.loadUrl(url);

	        return v;
	    }
	}