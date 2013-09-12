package com.mkiisoft.linguoo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mkiisoft.linguoo.R.array;
import com.mkiisoft.linguoo.async.AsyncConnection;
import com.mkiisoft.linguoo.async.Commons;
import com.mkiisoft.linguoo.async.ConnectionListener;
import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.KeySaver;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.SpannableString;
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
	
		super.onConfigurationChanged(newConfig);
	}

	private GridView gv;
	private String usuLog="";
	private int firstTime=0;
	private ImageView img_back;
	private ArrayList<ItemImage> arrayCategoria;
	private int lastPosition=0;
	private String seleccionadas;


	ImageAdapterGrid ia;
	//boolean[] arraySelection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catchioce_layout);
		usuLog= KeySaver.getStringSavedShare(this, "UsuLog");
		
		
		firstTime=KeySaver.getIntSavedShare(this, "FirstTime");
		KeySaver.saveShare(this, "state", Constants.CATEG);
		gv= (GridView) findViewById(R.id.gird_cat);
		img_back=(ImageView) this.findViewById(R.id.btn_back_grid);
		String page = Constants.WSGETCAT+usuLog+",Q";
		arrayCategoria=new ArrayList<ItemImage>();
		AsyncConnection.getInstance(page, this, Constants.CATEGQ).execute();
		Log.d("Categ",page);

		
		
	}
	
	private void dibujar_grilla(){
		if(firstTime==1)
		{
			
			showAlertDialog(GridActivity.this, "Bienvenido!!", "Elija al menos una categoria que sea de su interes."); 
			img_back.setBackgroundResource(R.drawable.icon_next);
			firstTime=0;
			//KeySaver.saveShare(this, "FirstTime", 0);
		}
		else
		{
			
			img_back.setBackgroundResource(R.drawable.icon_back);
		}
		
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
			if(arrayCategoria.get(arg2).getImageSelected()==0)
			{
				arrayCategoria.get(arg2).setImageSelected(1);

			}
			else
			{
				arrayCategoria.get(arg2).setImageSelected(0);
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
			String categorias="";
		for(int i=0; i<arrayCategoria.size(); i++){
			if(arrayCategoria.get(i).getImageSelected()==1){
				categorias+=arrayCategoria.get(i).getId();
			}
			
		}
		Log.v("comparacion", seleccionadas+"---"+categorias);
		if(!seleccionadas.equals(categorias)){
			
			String page=Constants.WSGETCAT+usuLog+",I,"+categorias;
			
			AsyncConnection.getInstance(page, GridActivity.this, Constants.CATEGI).execute();
		}else{
			if (firstTime==Constants.FT_NO){
				Intent i = new Intent();
				setResult(Constants.CATUCHG, i);        
				finish();
			}
			else{
				
			}
		}
			
		}
	});
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
	
	public void showAlertDialog(Context context, String title, String message) {
		final SpannableString s = new SpannableString(message);
	    //Linkify.addLinks(s, Linkify.ALL);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
		.setIcon(R.drawable.linguoo)
		.setMessage(s)
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
		
	

	public int selectImaginItem(int id)
	{
		switch(id){
		case 1:
			return R.drawable.ciencia_256;
		case 2:
			return R.drawable.actualidad_256;
		case 3:
			return R.drawable.entretenimientos_128;
			
		case 4:
			return R.drawable.deportes_256;
			
		case 5:
			return R.drawable.cultura_256;
		case 6:
			return R.drawable.negocios_256;
		case 8:
			return R.drawable.vida_256;
		default:
			return R.drawable.vida_256;

		}


	}
	public boolean checkearSeleccion()
	{
		int selectedCat=0;
		for( ItemImage i: arrayCategoria)
		{
			
			selectedCat= selectedCat + i.getImageSelected();
			
		}
		if(selectedCat==0)
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}

	@Override
	public void ready(int msg, String message) {
		
		switch (msg){
		case Constants.CATEGQ:
			/*
			 * Parsear JSON (String message) y Llamar a cargar grilla
			 */
			try{
				JSONObject res=new JSONObject(message);
				seleccionadas = res.getString("seleccionados");
				
				int j=0,selected=0;
				JSONArray categorias = res.getJSONArray("categorias");
				
				for(int i=0; i<categorias.length(); i++)
				{
					
					
					
					if(seleccionadas.length()>0 && String.valueOf(seleccionadas.charAt(j)).equals(categorias.getJSONObject(i).getString("CatLN"))){
						selected=1;
						j++;
						if (j==seleccionadas.length()) j=seleccionadas.length()-1;
					
					}
					ItemImage im=new ItemImage(selectImaginItem(Integer.valueOf(categorias.getJSONObject(i).getString("CatLN"))), selected, 
							categorias.getJSONObject(i).getString("CatdesLN"), 
							Integer.valueOf(categorias.getJSONObject(i).getString("CatLN")));
					
					arrayCategoria.add(im);
					
					selected=0;
				}
				
			}catch (Exception e) {
				
				e.getMessage();
			}

				dibujar_grilla();
				refreshGrid();
			
			break;
		case Constants.CATEGI:
			/*
			 * Llamar al intent para pasar a noticias
			 */
			if (firstTime==Constants.FT_YES || LinguooNewsActivity.isNewsActivityIsOpen()==false){
				Intent in=new Intent(GridActivity.this, LinguooNewsActivity.class);
				startActivity(in);
				finish();
			}else if (firstTime==Constants.FT_NO){
				Intent i = new Intent();
				setResult(Constants.CATCHG, i);        
				finish();
			}
		break;
		case AsyncConnection.ERROR:
		case AsyncConnection.NOCONNECTION:
			/*
			 * Dialogo error de conexion volver a intentar
			 */
			
			showAlertDialog(GridActivity.this, "Ha ocurrido un Error", "Por favor, intentelo mas tarde");
			break;
		}


	}

	@Override
	public void cacheReady(int msg, String message) {
	

	}










}
