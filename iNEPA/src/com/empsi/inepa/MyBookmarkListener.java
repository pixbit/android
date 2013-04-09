package com.empsi.inepa;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockListFragment;
  
public class MyBookmarkListener<t extends SherlockListFragment> implements TabListener {
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<t> mClass;
  
    /** Constructor used each time a new tab is created. */
    public MyBookmarkListener(Activity activity, String tag, Class<t> clz) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
    }
  
    /* The following are each of the ActionBar.TabListener callbacks */
  
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ignoredFt) {
        FragmentManager fragMgr = ((FragmentActivity) mActivity)
                .getSupportFragmentManager();
        FragmentTransaction ft = fragMgr.beginTransaction();
  
        // Check if the fragment is already initialized
        if (mFragment == null) {
            // If not, instantiate and add it to the activity
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
  
            ft.add(android.R.id.content, mFragment, mTag);
        } else {
            // If it exists, simply attach it in order to show it
            ft.attach(mFragment);
        }
  
        ft.commit();
    }
  
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ignoredFt) {
        FragmentManager fragMgr = ((FragmentActivity) mActivity)
                .getSupportFragmentManager();
        FragmentTransaction ft = fragMgr.beginTransaction();
  
        // Check if the fragment is already initialized
        if (mFragment == null) {
            // If not, instantiate and add it to the activity
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
  
            ft.add(android.R.id.content, mFragment, mTag);
        } else {
            // If it exists, simply attach it in order to show it
            ft.detach(mFragment);
        }
  
        ft.commit();
    }
  
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}