package com.mkiisoft.linguoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mkiisoft.linguoo.async.Commons;
import com.mkiisoft.linguoo.async.ConnectionListener;
import com.mkiisoft.linguoo.util.KeySaver;

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

public class GridActivity extends Activity implements ConnectionListener{
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	private GridView gv;
	private String usuLog="";
	private int firstTime=0;
	private ImageView img_back;
	private ArrayList<ItemImage> arrayCategoria;
	private int lastPosition=0;
	
	ImageAdapterGrid ia;
	//boolean[] arraySelection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catchioce_layout);
		usuLog= KeySaver.getStringSavedShare(this, "UsuLog");
		firstTime=KeySaver.getIntSavedShare(this, "FirstTime");
		gv= (GridView) findViewById(R.id.gird_cat);
		img_back=(ImageView) this.findViewById(R.id.btn_back_grid);
		
		try {	
			InputStream is= getAssets().open("jsoncat.txt");
			InputStreamReader reader= new InputStreamReader(is, "UTF-8");
			String res=getStringFromInputStream(reader);
			//comons=Commons.readFileAsString(res);
			arrayCategoria=new ArrayList<ItemImage>();
			JSONArray jsArrayCat= new JSONArray(res);
			
			for(int i=0; i<jsArrayCat.length(); i++)
			{
				JSONObject obj= (JSONObject) jsArrayCat.get(i);
				ItemImage item=new ItemImage(selectImaginItem(obj.getInt("cat_id")), obj.getBoolean("cat_sel"), obj.getString("cat_name"), obj.getInt("cat_id"));
				Log.v("item", selectImaginItem(obj.getInt("cat_id")) +""+ obj.getBoolean("cat_sel")+""+ obj.getString("cat_name"));
				arrayCategoria.add(item);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* Hay que setear la imagen del boton back segun si es firs time o no*/
		
		if(checkearSeleccion()==true)
		{
			img_back.setVisibility(View.VISIBLE);
		}
		else{
			img_back.setVisibility(View.INVISIBLE);
		}
		
		ia= new ImageAdapterGrid(this, arrayCategoria, R.layout.cat_item_grid);
		gv.setAdapter(ia);
		refreshGrid();
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				lastPosition= arg2;
				if(arrayCategoria.get(arg2).getImageSelected()==true)
				{
					arrayCategoria.get(arg2).setImageSelected(false);
					
				}
				else
				{
					arrayCategoria.get(arg2).setImageSelected(true);
				}
				if(checkearSeleccion()==true)
				{
					img_back.setVisibility(View.VISIBLE);
				}
				else{
					img_back.setVisibility(View.INVISIBLE);
				}
				refreshGrid();
			}
		});
				
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*llamar al web service en el asynctask*/
				String categorias="";
				for(int i=0; i<arrayCategoria.size(); i++)
				{
					if(arrayCategoria.get(i).getImageSelected()==true)
					{
						categorias+=","+arrayCategoria.get(i).getId();
						
					}
				}
				Log.v("categorias", categorias);
			}
		});
		
	}
	
	public static String getStringFromInputStream(InputStreamReader is)
	{
		BufferedReader reader=new BufferedReader(is);
		StringBuilder sb=new StringBuilder();
		String Line=null;
		
		try{
			while((Line=reader.readLine()) != null)
			{
				sb.append(Line + "\n");
			}
			is.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	private void refreshGrid(){
		//setBotones();
		if(gv.getAdapter()!=null){
		//gv.invalidateViews();}
		}
		//reloadAdapter();
		//mAdapter.notifyDataSetChanged();
		gv.setAdapter(ia);
		gv.setSelection(lastPosition);
		
		ia.notifyDataSetChanged();
	}
	
	public int selectImaginItem(int id)
	{
		switch(id){
		case 1:
			return R.drawable.cultura_256;
		case 2:
			return R.drawable.actualidad_256;
		case 3:
			return R.drawable.deportes_256;
		case 4:
			return R.drawable.negocios_256;
		case 5:
			return R.drawable.entretenimientos_128;
		case 6:
			return R.drawable.ciencia_256;
		default:
			return R.drawable.vida_256;
			
		}
		
		
	}
	public boolean checkearSeleccion()
	{
		boolean selectedCat=false;
		for( ItemImage i: arrayCategoria)
		{
			selectedCat= selectedCat || i.getImageSelected();
		}
		return selectedCat;
	}

	@Override
	public void ready(int msg, String message) {
		
		
		
	}

	@Override
	public void cacheReady(int msg, String message) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
		
	

}
