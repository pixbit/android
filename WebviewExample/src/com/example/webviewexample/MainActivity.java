package com.example.webviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        WebView engine = (WebView) findViewById(R.id.web_engine);
        
        String data = "<html>" +
        			  "<body><h1>Yay, Pixbit!</h1></body>" +
        			  "</html>";
        
        engine.getSettings().setJavaScriptEnabled(true);
//        engine.loadData(data, "text/html", "UTF-8");
        engine.loadUrl("file:///android_asset/handbook.htm");
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
