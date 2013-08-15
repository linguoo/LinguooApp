package com.mkiisoft.linguoo;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomCatAdapter extends ArrayAdapter<String>  {
	private static final String TAG = "Linguoo CustomAdapter";
	private Activity activity;
	private int layoutResourceid;
	private ArrayList<String> data;


	public CustomCatAdapter(Activity activity, int layoutResourceid,
			ArrayList<String> data) {
		super(activity.getApplicationContext(), layoutResourceid,data);
		this.activity = activity;
		this.layoutResourceid = layoutResourceid;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
	    ViewHolder viewHolder=null;
	    if (vi == null) {
	    	//Log.d("CustomAdapter","convertview is null");
	    	LayoutInflater li = activity.getLayoutInflater();
	    	vi = li.inflate(layoutResourceid, null);
	    	
	        viewHolder = new ViewHolder();
	        viewHolder.txtCategoria = (TextView) vi.findViewById(R.id.txtCategorias);
	        vi.setTag(viewHolder);
	    }else{
	    	viewHolder = (ViewHolder) vi.getTag();
	    }
	    String categoria = data.get(position);
	    viewHolder.txtCategoria.setText(categoria);
	    
	    return vi;
	}

	static class ViewHolder {
	    protected TextView txtCategoria;
	}



}
