package com.empsi.inepa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
	
	protected WebView webview;
	protected JavaScriptInterface jsi;
	public ProgressBar progressBar;
	public ProgressBar progressSpinner;
	public ImageView overlay;
	
	private SearchView mSearchView;
	private int searchIndex;
	private int resultNumber;
	
	public TextView searchString;
	
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
        		webview = (WebView) findViewById(R.id.web_engine);
        		jsi = new JavaScriptInterface(this);
        		webview.addJavascriptInterface(jsi, "MainActivity");
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


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);

      MenuInflater inflater = getSupportMenuInflater();
      inflater.inflate(R.menu.searchview_in_menu, menu);
      MenuItem searchItem = menu.findItem(R.id.action_search);
      mSearchView = (SearchView) searchItem.getActionView();
      setupSearchView(searchItem);

      return true;
  }

  private void setupSearchView(MenuItem searchItem) {

      if (isAlwaysExpanded()) {
          mSearchView.setIconifiedByDefault(false);
      } else {
          searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                  | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
      }

//      SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
//      if (searchManager != null) {
//          List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
//
//          SearchableInfo info = searchManager.getSearchableInfo(this.getComponentName());
//          for (SearchableInfo inf : searchables) {
//              if (inf.getSuggestAuthority() != null
//                      && inf.getSuggestAuthority().startsWith("applications")) {
//                  info = inf;
//              }
//          }
//          mSearchView.setSearchableInfo(info);
//      }

      mSearchView.setOnQueryTextListener(this);
  }

  public boolean onQueryTextChange(String newText) {
//	  Toast.makeText(this, "Query = " + newText, Toast.LENGTH_SHORT).show();
      return false;
  }

  public boolean onQueryTextSubmit(String query) {
//	  Toast.makeText(this, "Query = " + query + " : submitted", Toast.LENGTH_SHORT).show();

	  webview.loadUrl("javascript:MyApp_HighlightAllOccurencesOfString(\""+query+"\");");
	  webview.loadUrl("javascript:getSearchScrollValues();");
//	  Log.d(TAG, "scrollJSONArray: " + JavaScriptInterface.scrollJSONArray);
	  
      return false;
  }
  
  public void setupSearch(){
	  
	  
	  return;
  }

  public boolean onClose() {
	  Toast.makeText(this, "Closed!", Toast.LENGTH_SHORT).show();
      return false;
  }

  protected boolean isAlwaysExpanded() {
      return false;
  }
  
  protected void searchPreviousButtonPressed(WebView webview) {
		if( searchIndex > 0){
			searchIndex -= 1;
//			searchResultButton.title = [NSString stringWithFormat:@"%i of %i", searchIndex+1, resultNumber];
//			[self setScrollPostion:webView xValue:0 yValue:[[scrollArray objectAtIndex:searchIndex] integerValue]];
		}
	}
  protected void searchNextButtonPressed(WebView webview) {
		if( searchIndex < resultNumber-1){
			searchIndex += 1;
//			searchResultButton.title = [NSString stringWithFormat:@"%i of %i", searchIndex+1, resultNumber];	
//			[self setScrollPostion:webView xValue:0 yValue:[[scrollArray objectAtIndex:searchIndex] integerValue]];
		}
	}
  
  protected int highlightAllOccurencesOfString(String str, WebView webview) {
		int result = -1;
	  	webview.loadUrl("javascript:MyApp_RemoveAllHighlights(" + str + ");");
		
//	    int result = [thiswebView stringByEvaluatingJavaScriptFromString:@"MyApp_SearchResultCount"];
	    result = 10;
	    return result;
	}
  
  protected void removeAllHighlights(WebView webview) {
		webview.loadUrl("javascript:MyApp_RemoveAllHighlights();");
	}
}
