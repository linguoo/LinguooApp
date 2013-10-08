package com.linguoo.linguooapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.linguoo.linguooapp.async.AsyncConnection;
import com.linguoo.linguooapp.async.Commons;
import com.linguoo.linguooapp.async.ConnectionListener;
import com.linguoo.linguooapp.util.Constants;
import com.linguoo.linguooapp.util.KeySaver;
import com.mkiisoft.linguoo.R;
import com.mkiisoft.linguoo.R.array;
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
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GridActivity extends Activity implements ConnectionListener{

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	
		super.onConfigurationChanged(newConfig);
		gv.setAdapter(ia);
		refreshGrid(0);
	}
	private final String TAG="Linguoo Categories";
	private GridView gv;
	private String usuLog="";
	private int firstTime=0;
	private ImageView img_back;
	private ArrayList<ItemImage> arrayCategoria;
	private int lastPosition=0;
	private String seleccionadas;
	private ProgressBar pb;
	ImageAdapterGrid ia;
	private boolean connected=true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catchioce_layout);
		pb= (ProgressBar)findViewById(R.id.progress_bar);
		pb.setVisibility(View.VISIBLE);
		usuLog= KeySaver.getStringSavedShare(this, "UsuLog");
				
		firstTime=KeySaver.getIntSavedShare(this, "FirstTime");

		
		gv= (GridView) findViewById(R.id.gird_cat);
		img_back=(ImageView) this.findViewById(R.id.btn_back_grid);
		String page = Constants.WSGETCAT+usuLog+",Q";
		arrayCategoria=new ArrayList<ItemImage>();
		AsyncConnection.getInstance(page, this, Constants.CATEGQ).execute();
		
		setListeners();
	}
	
	private void setListeners() {
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long arg3) {


				lastPosition=gv.getPositionForView(v);
				if(arrayCategoria.get(position).getImageSelected()==0){
					arrayCategoria.get(position).setImageSelected(1);
				}else{
					arrayCategoria.get(position).setImageSelected(0);
				}
				
				if(checkearSeleccion()==true){
					img_back.setVisibility(View.VISIBLE);
				}else{
					img_back.setVisibility(View.INVISIBLE);
				}
				refreshGrid(lastPosition);
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
			
			if(!seleccionadas.equals(categorias)){
				
				String page=Constants.WSGETCAT+usuLog+",I,"+categorias;
				pb.setVisibility(View.VISIBLE);
				AsyncConnection.getInstance(page, GridActivity.this, Constants.CATEGI).execute();
			}else{
				
				//Intent in=new Intent(GridActivity.this, LinguooNewsActivity.class);
				//startActivity(in);
			}
				
			}
		});
	}


	private void dibujar_grilla(){
		if(firstTime==1){
			showAlertDialog(GridActivity.this, "Bienvenido!!", "Elija al menos una categoria que sea de su interes."); 
			img_back.setBackgroundResource(R.drawable.icon_next);
			KeySaver.saveShare(this, "FirstTime", 0);
		}else{
			img_back.setBackgroundResource(R.drawable.icon_back);
		}
		if(checkearSeleccion()==true){
			img_back.setVisibility(View.VISIBLE);
		}else{
			img_back.setVisibility(View.INVISIBLE);
		}
		ia= new ImageAdapterGrid(this, arrayCategoria, R.layout.cat_item_grid);
		gv.setAdapter(ia);
	}
	
	private void refreshGrid(int pos){
		
		//int firstVPosition = gv.getFirstVisiblePosition();
//		int lastVPosition = gv.getLastVisiblePosition();
		gv.setSelection(pos);
		ia.notifyDataSetChanged();
		//gv.setAdapter(ia);
		gv.invalidate();
	//	if (lastPosition<(lastVPosition-1))gv.setSelection(firstVPosition);
		//else gv.setSelection(firstVPosition+2);
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
				if(connected==false){
					finish();
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
		
	

	
	
	public boolean checkearSeleccion()
	{
		int selectedCat=0;
		for( ItemImage i: arrayCategoria){
			selectedCat= selectedCat + i.getImageSelected();
		}
		if(selectedCat==0){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public void ready(int msg, String message) {
		pb.setVisibility(View.GONE);
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
					JSONObject object=categorias.getJSONObject(i);
					String cat=object.getString("CatdesLN");
					Log.v("object", cat);
					
					if(seleccionadas.length()>0 && String.valueOf(seleccionadas.charAt(j)).equals(categorias.getJSONObject(i).getString("CatLN"))){
						selected=1;
						j++;
						if (j==seleccionadas.length()) j=seleccionadas.length()-1;
					}
					ItemImage im=new ItemImage(object.getString("CatfotLN_GXI"), selected, 
							cat, 
							Integer.valueOf(object.getString("CatLN")));
					Log.v("item", "imagen: "+im.getImageView()+"selected: "+ im.getImageSelected() 
							+"categoria: "+im.getTextCategoria() 
							+"id: "+im.getId());
					arrayCategoria.add(im);
					selected=0;
				}
				
			}catch (Exception e) {
				
				e.getMessage();
			}
				
				dibujar_grilla();
				connected=true;
			break;
		case Constants.CATEGI:
			
			/*
			 * Llamar al intent para pasar a noticias
			 */
			
			//Intent in=new Intent(GridActivity.this, LinguooNewsActivity.class);
			//startActivity(in);
		break;
		case AsyncConnection.ERROR:
		case AsyncConnection.NOCONNECTION:
			/*
			 * Dialogo error de conexion volver a intentar
			 */
			connected=false;
			showAlertDialog(GridActivity.this, "Ha ocurrido un Error", "Por favor, intentelo mas tarde");
			
			break;
		}
	}

	@Override
	public void cacheReady(int msg, String message) {
	}
}