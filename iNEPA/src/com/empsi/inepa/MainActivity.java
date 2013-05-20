package com.empsi.inepa;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class MainActivity extends SherlockActivity implements SearchView.OnQueryTextListener {
  private static final String TAG = MainActivity.class.getSimpleName(); 
  
  protected String view = null; 
  protected String title = null; 
  protected String scroll = null;
  
  WebView webview;
  RelativeLayout searchOverlay;
  TextView searchString;
  Button prevButton;
  Button nextButton;
  Button doneButton;
  
  protected JavaScriptInterface jsi;
  public ProgressBar progressBar;
  public ProgressBar progressSpinner;
  public ImageView overlay;
  
  private SearchView mSearchView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getSupportActionBar().setIcon(android.R.drawable.ic_menu_revert);
    final ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    
    searchString = (TextView)findViewById(R.id.textView1);
    
    progressBar = (ProgressBar)findViewById(R.id.progressbar);
    progressBar.setProgress(0);
    progressBar.setVisibility(View.VISIBLE);
    
    progressSpinner = (ProgressBar)findViewById(R.id.progressBar1);
    progressSpinner.setProgress(0);
    progressSpinner.setVisibility(View.VISIBLE);
    
    searchOverlay = (RelativeLayout)findViewById(R.id.searchBar);
    searchOverlay.setBackgroundResource(android.R.color.black);
    searchOverlay.setVisibility(View.INVISIBLE);

    prevButton = (Button)findViewById(R.id.prevButton);
    nextButton = (Button)findViewById(R.id.nextButton);
    doneButton = (Button)findViewById(R.id.doneButton);
    
    prevButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
              jsi.clickPrevButton();
            }
         });
    
    nextButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
          jsi.clickNextButton();
      }
    });
      
    doneButton.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        jsi.clickDoneButton();
      }
    });
      
    overlay = (ImageView)findViewById(R.id.imageView1);
    overlay.setBackgroundResource(android.R.color.black);
    overlay.setVisibility(View.VISIBLE);
    
    Intent receiveIntent = getIntent();
    // String content = receiveIntent.getData().toString();
    // WebView engine = (WebView) findViewById(R.id.web_engine);
    // engine.loadUrl(content);
      
    Bundle extras = receiveIntent.getExtras();
    if (extras == null) {
      return;
    }
    // Get data via the key
    view = extras.getString("view");
    title = extras.getString("title");
    scroll = extras.getString("scroll");

    // Toast.makeText(this, view, Toast.LENGTH_LONG).show();
    if (view != null) {
      // Do something with the data
      webview = (WebView) findViewById(R.id.web_engine);
      jsi = new JavaScriptInterface(this, webview, searchOverlay, searchString, prevButton, nextButton, doneButton);
      webview.addJavascriptInterface(jsi, "MainActivity");
      webview.getSettings().setJavaScriptEnabled(true);
      webview.setWebChromeClient(new WebChromeClient(){
        public void onProgressChanged(WebView view, int progress) {
         // Log.d("PROGRESS", String.format("Progress: %d", progress));
         progressBar.setProgress(progress);
         progressSpinner.setProgress(progress);
         if(progress == 100) {
           progressBar.setVisibility(View.GONE);
           progressSpinner.setVisibility(View.GONE);
           overlay.setVisibility(View.GONE);
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
      // Toast.makeText(this, scroll, Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    int itemId = item.getItemId();
    switch (itemId) {
      case android.R.id.home:
        finish();
        // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
        break;
      case R.id.menu_search:
        // Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
        break;
      case R.id.menu_bookmark:
        // Log.d("onBookmarkClick", "view: " + view);
        jsi.onBookmarkClick(view);
        break;
    }
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getSupportMenuInflater();
    
    inflater.inflate(R.layout.activity_menu, menu);

    // Get the SearchView and set the searchable configuration
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
    mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    mSearchView.setOnQueryTextListener(this);

    return true;
  }

  private void setupSearchView(MenuItem searchItem) {
    if (isAlwaysExpanded()) {
      mSearchView.setIconifiedByDefault(false);
    } else {
      searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
        | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
    }

   // SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
   // if (searchManager != null) {
   //   List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

   //   SearchableInfo info = searchManager.getSearchableInfo(this.getComponentName());
   //   for (SearchableInfo inf : searchables) {
   //     if (inf.getSuggestAuthority() != null
   //        && inf.getSuggestAuthority().startsWith("applications")) {
   //        info = inf;
   //     }
   //   }
   //   mSearchView.setSearchableInfo(info);
   // }

    mSearchView.setOnQueryTextListener(this);
  }
  
  public boolean onQueryTextChange(String newText){
    return false;
  }

  public boolean onQueryTextSubmit(String query){
    searchOverlay.setVisibility(View.VISIBLE);
    jsi.submitSearch(query);
    
    return false;
  }
  
  public void setupSearch(){
    return;
  }

  public boolean onClose() {
    return false;
  }

  protected boolean isAlwaysExpanded() {
    return false;
  }
}