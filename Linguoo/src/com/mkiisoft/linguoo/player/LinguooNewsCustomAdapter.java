package com.mkiisoft.linguoo.player;

import com.mkiisoft.linguoo.R;
import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LinguooNewsCustomAdapter extends BaseAdapter {
	protected static final String TAG = "CustomNewsActivity";
	private int layoutResourceid;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater=null;
    private ImageLoader imageLoader;
	private OnClickListener itemPlay;
	private OnClickListener itemAdd;
	private Boolean isDemoUser;
    
    public LinguooNewsCustomAdapter(Activity a, int layoutResourceid, ArrayList<HashMap<String, String>> data, OnClickListener itemPlay, Boolean isDemoUser) {
        this.activity = a;
        this.data = data;
        this.layoutResourceid = layoutResourceid;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoader(activity.getApplicationContext());
        this.itemPlay = itemPlay;
        this.itemAdd = itemPlay;
        this.isDemoUser = isDemoUser;
    }

	public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	View viewList = convertView;
    	HashMap<String, String> item = new HashMap<String, String>();
    	itemHolder holder = null;
       
        if(convertView == null){
        	viewList = inflater.inflate(layoutResourceid, null);
            holder = new itemHolder();
            
            holder.txtItemTitle  = (TextView)viewList.findViewById(R.id.txtItemTitle); 
            holder.txtItemContent = (TextView)viewList.findViewById(R.id.txtItemContent); 
            holder.thub_image = (ImageView)viewList.findViewById(R.id.imgItemNews);
            holder.btnAdd = (ToggleButton)viewList.findViewById(R.id.btnAddToPlaylist);
            if(isDemoUser)holder.btnAdd.setVisibility(ToggleButton.INVISIBLE);
            
            viewList.setTag(holder);
        }else{
  	      	holder = (itemHolder) viewList.getTag();
        }
        
        item = data.get(position);

        holder.onPlayList = Boolean.parseBoolean(item.get(Constants.NEWS_ONPLAYLIST));
        holder.txtItemTitle.setText(item.get(Constants.NEWS_TITLE));
        holder.txtItemContent.setText(item.get(Constants.NEWS_CONTENT));
        holder.itemPosition = position;
        imageLoader.DisplayImage(item.get(Constants.NEWS_THUMB), holder.thub_image);
                
        if(holder.isAdded()){
        	holder.btnAdd.setChecked(true);
        	holder.btnAdd.setBackgroundResource(R.drawable.btn_add_on);
        }else{
        	holder.btnAdd.setChecked(false);
        	holder.btnAdd.setBackgroundResource(R.drawable.btn_add_off);
        };
         
        holder.btnAdd.setOnClickListener(itemAdd);
        viewList.setOnClickListener (itemPlay);
        return viewList;
    }
    
        
    /*  *********************************************************************************************** */
    
    static class itemHolder {
    	protected TextView txtItemTitle;
    	protected TextView txtItemContent;
    	protected ImageView thub_image;
    	protected ToggleButton btnAdd;
    	protected int itemPosition;
    	protected Boolean onPlayList;
    	
    	public int getItemPosition(){
    		return itemPosition;
    	}
    	
    	public Boolean isAdded(){
    		return onPlayList;
    	}
    }
}