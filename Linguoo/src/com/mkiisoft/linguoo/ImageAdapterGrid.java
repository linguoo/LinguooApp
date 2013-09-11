package com.mkiisoft.linguoo;

import java.util.ArrayList;
import java.util.Arrays;

import com.mkiisoft.linguoo.CustomCatAdapter.ViewHolder;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.rtp.RtpStream;
import android.renderscript.Sampler.Value;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import android.widget.ImageView;


public class ImageAdapterGrid extends BaseAdapter {

	private Activity act;
	private ArrayList<ItemImage> arrayCat;
	private int layout;
	
	
	
	
	public ImageAdapterGrid(Activity c, ArrayList<ItemImage> array, int layoutItem)
	{
		act=c;
		this.layout=layoutItem;
		this.arrayCat=array;
	}
	
	
	
	@Override
	public int getCount() {
		
		
		return arrayCat.size();
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		//
		ViewHolder holder=null;
		
		if(arg1==null)
		{
			holder=new ViewHolder();
			LayoutInflater li=act.getLayoutInflater();
			
			arg1= li.inflate(layout, null); 
			holder.iv=(ImageView) arg1.findViewById(R.id.img_categoria_grid);
			holder.chk= (ImageView) arg1.findViewById(R.id.img_selected);
			holder.t= (TextView) arg1.findViewById(R.id.txt_categoria_grid);
			arg1.setTag(holder);
			
		}else{
			holder= (ViewHolder) arg1.getTag();
			
		}
		
		holder.t.setText(arrayCat.get(position).getTextCategoria());
		holder.iv.setBackgroundResource(arrayCat.get(position).getImageView());
		
		if(arrayCat.get(position).getImageSelected()==1)
		{
			holder.chk.setBackgroundResource(R.drawable.cat_select);
		}
		else{
			holder.chk.setBackgroundResource(R.drawable.cat_unselect);
		}
		
		
		return arg1;
	}
	
	
	
	class ViewHolder{
		ImageView iv;
		ImageView chk;
		TextView t;
		int id;
		
	}
	
	
	
	
	
	

	
	
	
	
}
