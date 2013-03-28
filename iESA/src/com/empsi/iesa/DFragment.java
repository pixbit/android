package com.empsi.iesa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
  
public class DFragment extends SherlockFragment {
	 	@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View v = inflater.inflate(R.layout.activity_main, container, false);
	        
	        String url = "file:///android_asset/about.htm";
	        
	        WebView webview = (WebView) v.findViewById(R.id.web_engine);
    		webview.setWebViewClient(new WebViewClient() {
    		    @Override  
    		    public void onPageFinished(WebView view, String url) {
    		        Toast.makeText(getActivity().getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
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