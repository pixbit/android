package com.empsi.inepa;

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
	 	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.activity_main, container, false);
	        
	        String url = "http:///www.empsi.com/nepalinks.html";
	        
	        WebView webview = (WebView) v.findViewById(R.id.web_engine);
    		webview.getSettings().setJavaScriptEnabled(true);
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