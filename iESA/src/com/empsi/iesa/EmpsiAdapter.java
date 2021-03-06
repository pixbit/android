package com.empsi.iesa;

import java.util.ArrayList;
import java.util.List;

import com.empsi.iesa.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EmpsiAdapter extends ArrayAdapter<ParsedRow> {
    public static final String TAG = EmpsiAdapter.class.getSimpleName();

	Context activity;
	AFragment listfragment;
    int layoutResourceId;    
    List<ParsedRow> data = null;
    
    public static List<ParsedRow> currentData = null;
    public static ArrayList<List<ParsedRow>> prevData = new ArrayList<List<ParsedRow>>();
    public static ArrayList<String> prevTitle = new ArrayList<String>();
    
    public EmpsiAdapter(Context context, int layoutResourceId, List<ParsedRow> data, AFragment aFragment) {
        super(context, layoutResourceId, data);
        this.layoutResourceId    = layoutResourceId;
        this.activity            = context;
        this.listfragment        = aFragment;
        this.data                = data;
        EmpsiAdapter.currentData = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row         = convertView;
        RowHolder holder = null;
        
        if(row == null){
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder          = new RowHolder();
            holder.imgIcon  = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);           
            holder.imgIcon.setOnClickListener(new EmpsiListener(row, data, position, activity, this.listfragment));

            row.setTag(holder);
        }else{
            holder = (RowHolder)row.getTag();
        }
                
        ParsedRow rowItem = data.get(position);
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imgIcon.setImageResource(rowItem.getIcon());
        
        return row;
    }
    
    static class RowHolder {
        ImageView imgIcon;
        TextView txtTitle;
    }

    public class EmpsiListener implements View.OnClickListener {
    	Context context; 
    	AFragment frag;
        List<ParsedRow> data     = null;
        List<ParsedRow> prevData = null;
        List<ParsedRow> nextData = null;
    	private View mView;
    	int position;

    	public EmpsiListener(View v, List<ParsedRow> data, int position, Context context, AFragment frag) {
            this.data     = data;
            this.position = position;
            this.context  = context;
            this.frag     = frag;
    		setmView(v);
    	}

	    @Override
	    public void onClick(View v) {
            Log.d(TAG, String.format("Icon clicked = %d", this.position));
            Log.d(TAG, this.data.get(position).getTitle());

	    	EmpsiAdapter.prevData.add(this.data);
	    	this.nextData = this.data.get(position).getChildren();
	    	/* Set the result to be displayed in our GUI. */
	    	EmpsiAdapter adapter = new EmpsiAdapter(this.context, R.layout.empsi_custom_row, this.nextData, this.frag);
	    	this.frag.setListAdapter(adapter);  

//    			EmpsiAdapter.prevTitle.add(this.data.get(position).getTitle());
//    			((MainListActivity) this.context).actionBar.setTitle(this.data.get(position).getTitle()); 
	    }

		public View getmView() {
			return mView;
		}

		public void setmView(View mView) {
			this.mView = mView;
		}
    }
}