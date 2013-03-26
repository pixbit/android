package com.empsi.iesa;

import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
  
public class Main extends SherlockFragmentActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
  
        ActionBar actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
  
        Tab tab1 = actionbar.newTab().setText("Greeting 1");
        Tab tab2 = actionbar.newTab().setText("Greeting 2");
  
        tab1.setTabListener(new MyTabListener<AFragment>(this, "tab1",
                AFragment.class));
  
        tab2.setTabListener(new MyTabListener<BFragment>(this, "tab2",
                BFragment.class));
  
        actionbar.addTab(tab1);
        actionbar.addTab(tab2);
    }
}