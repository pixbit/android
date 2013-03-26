package com.empsi.iesa;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
  
public class BFragment extends SherlockListFragment {
	 @Override
	    public void onViewCreated(View view, Bundle savedInstanceState) {
	        super.onViewCreated(view, savedInstanceState);
	        TextView tv = new TextView(getActivity());
	        tv.setText("Hello");
	        getListView().addHeaderView(tv);
	        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
	                new String[] { "Hello world" }));
	    }
	 
	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	        setListAdapter(null);
	    }
	}