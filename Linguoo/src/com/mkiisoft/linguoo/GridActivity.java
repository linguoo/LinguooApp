package com.mkiisoft.linguoo;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;

import com.mkiisoft.linguoo.async.Commons;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GridActivity extends Activity {

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	private String comons;
	GridView gv;
	String[] catSelected;
	int c=0;
	boolean[] arraySelection;
	ArrayList<ItemImage> arrayCategoria;
	
	ImageAdapterGrid ia;
	//boolean[] arraySelection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catchioce_layout);
		
		gv= (GridView) findViewById(R.id.gird_cat);
		ia= new ImageAdapterGrid(this, arrayCategoria, R.layout.cat_item_grid);
		gv.setAdapter(ia);
		refreshGrid();
		
		try {
			
			comons=Commons.readFileAsString("file:///android_asset/jsoncat.txt");
			JSONArray jsArrayCat= new JSONArray(comons);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		final int size= gv.getCount();
		catSelected=new String[size];
		
		/*gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				arg1.setSelected(true);
				
				int itemSize= itemChild.getChildCount();
				
				
				
				
				
				for(int j=0; j<itemSize; j++)
				{
					
					if(itemChild.getChildAt(j) instanceof TextView)
					{
						Log.v("asda", ""+itemSize);
						TextView text= (TextView)itemChild.getChildAt(j);
						String categoria= (String) text.getText();
						if(probarSeleccion(categoria))
						{
							catSelected[c]=categoria;
							c++;
						}else
						{
							for(int i=0; i<catSelected.length; i++)
							{
								if(catSelected[i].toString().equals(categoria))
								{
									
								}
							}
						}
						
						
						Log.v("array", catSelected[c]);
						
					}
				}
				
			}
		});*/
		ImageView back_immage= (ImageView) findViewById(R.id.btn_back_grid);
		back_immage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				
			}
		});
		
		
		
	}
	
	private void refreshGrid(){
		//setBotones();
		if(gv.getAdapter()!=null){
		gv.invalidateViews();}
		//reloadAdapter();
		//mAdapter.notifyDataSetChanged();
		gv.setAdapter(ia);
		
		ia.notifyDataSetChanged();
	}
	
	public boolean probarSeleccion(String cat)
	{
		for(int i=0; i<catSelected.length; i++)
		{
			if(catSelected[i].toString().equals(cat))
			{
				return false;
			}
		}
		return true;
	}
	
	
	
	
	
		
	

}
