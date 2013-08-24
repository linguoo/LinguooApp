package com.mkiisoft.linguoo;

import com.mkiisoft.linguoo.util.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomNewsAdapter extends BaseAdapter {
	protected static final String TAG = "CustomNewsActivity";
	private int layoutResourceid;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater=null;
    public ImageLoader imageLoader;
	private OnClickListener itemPlay;
	private OnClickListener itemAdd; 
    
    public CustomNewsAdapter(Activity a, int layoutResourceid, ArrayList<HashMap<String, String>> d, 
    						 OnClickListener itemPlay, OnClickListener itemAdd) {
        this.activity = a;
        this.data = d;
        this.layoutResourceid = layoutResourceid;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageLoader = new ImageLoader(activity.getApplicationContext());
        this.itemPlay = itemPlay;
        this.itemAdd = itemAdd;
        
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
        View vi = convertView;
        newsHolder holder = null;
        HashMap<String, String> item = new HashMap<String, String>();
        
        if(convertView==null){
            vi = inflater.inflate(layoutResourceid, null);
            holder = new newsHolder();
            
            holder.txtItemTitle  = (TextView)vi.findViewById(R.id.txtItemTitle); 
            holder.txtItemContent = (TextView)vi.findViewById(R.id.txtItemContent); 
            holder.thub_image = (ImageView)vi.findViewById(R.id.imgItemNews);
            holder.btnAdd = (Button)vi.findViewById(R.id.btnAddToPlaylist);
            
            vi.setTag(holder);
            
        }else{
        	holder = (newsHolder) vi.getTag();
        }        
                
        item = data.get(position);
        
        holder.newsId = Integer.parseInt(item.get(LinguooNewsActivity.KEY_ID));
        holder.newsCategoryId = Integer.parseInt(item.get(LinguooNewsActivity.KEY_CATEGORY));
        holder.newsAudioUrl = item.get(LinguooNewsActivity.KEY_AUDIO);
        holder.txtItemTitle.setText(item.get(LinguooNewsActivity.KEY_TITLE));
        holder.txtItemContent.setText(item.get(LinguooNewsActivity.KEY_CONTENT));
        imageLoader.DisplayImage(item.get(LinguooNewsActivity.KEY_THUMB), holder.thub_image);
        
        vi.setOnClickListener (this.itemPlay);
        holder.btnAdd.setOnClickListener(this.itemAdd);
        holder.btnAdd.setTag(position);
        
        return vi;
    }
    
    static class newsHolder {
    	protected Integer newsId;
    	protected Integer newsCategoryId;
    	protected String newsAudioUrl;
    	protected TextView txtItemTitle;
    	protected TextView txtItemContent;
    	protected ImageView thub_image;
    	protected Button btnAdd;
    	
    	public Integer getNewsId(){
    		return this.newsId;
    	}
    	
    	public Integer getCategoryId(){
    		return this.newsCategoryId;
    	}
    	
    	public String getAudioURL(){
    		return this.newsAudioUrl;
    	}
    	
    }
}