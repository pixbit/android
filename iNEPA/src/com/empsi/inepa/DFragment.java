package com.empsi.inepa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
  
public class DFragment extends SherlockFragment {
	public ProgressBar progressBar;
	public ProgressBar progressSpinner;
	public RelativeLayout searchBar;
	
	 	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.activity_main, container, false);
	        
	        String url = "file:///android_asset/about.htm";
	        
	        progressBar = (ProgressBar) v.findViewById(R.id.progressbar);
	        progressBar.setProgress(0);
	        progressBar.setVisibility(View.VISIBLE);
	        
	        progressSpinner = (ProgressBar) v.findViewById(R.id.progressBar1);
	        progressSpinner.setProgress(0);
	        progressSpinner.setVisibility(View.VISIBLE);
	        
	        searchBar = (RelativeLayout) v.findViewById(R.id.searchBar);
	        searchBar.setVisibility(View.GONE);
	        
	        WebView webview = (WebView) v.findViewById(R.id.web_engine);
    		webview.setWebViewClient(new WebViewClient() {
    		    @Override  
    		    public void onPageFinished(WebView view, String url) {
//    		        Toast.makeText(getActivity().getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();

		             progressBar.setVisibility(View.GONE);
		             progressSpinner.setVisibility(View.GONE);
    		    }  

    		    @Override
    		    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    		        Toast.makeText(getActivity().getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
    		    }

    		    @Override
    		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		        if (url.endsWith(".mp4")) 
    		        {
    		            Intent intent = new Intent(Intent.ACTION_VIEW);
    		            intent.setDataAndType(Uri.parse(url), "video/*");

    		            view.getContext().startActivity(intent);
    		            return true;
    		        } 
    		        else {
    		            return super.shouldOverrideUrlLoading(view, url);
    		        }
    		    }
    		});
    		webview.getSettings().setJavaScriptEnabled(true);
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