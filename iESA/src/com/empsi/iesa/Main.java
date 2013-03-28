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
  
        Tab tab1 = actionbar.newTab().setText("Regulations").setIcon(android.R.drawable.ic_menu_sort_by_size);
        Tab tab2 = actionbar.newTab().setText("Links").setIcon(android.R.drawable.star_big_off);
        Tab tab3 = actionbar.newTab().setText("Bookmarks").setIcon(android.R.drawable.ic_menu_add);
        Tab tab4 = actionbar.newTab().setText("About").setIcon(android.R.drawable.ic_menu_info_details);
  
        tab1.setTabListener(new MyTabListener<AFragment>(this, "tab1", AFragment.class));
        tab2.setTabListener(new MyWebTabListener<BFragment>(this, "tab2", BFragment.class));
        tab3.setTabListener(new MyTabListener<CFragment>(this, "tab3", CFragment.class));
        tab4.setTabListener(new MyWebTabListener<DFragment>(this, "tab4", DFragment.class));

        actionbar.addTab(tab1);
        actionbar.addTab(tab2);
        actionbar.addTab(tab3);
        actionbar.addTab(tab4);
    }
}