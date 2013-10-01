package com.linguoo.linguooapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.linguoo.linguooapp.async.AsyncConnection;
import com.linguoo.linguooapp.async.ConnectionListener;
import com.linguoo.linguooapp.util.Constants;
import com.linguoo.linguooapp.util.GraphicsUtils;
import com.linguoo.linguooapp.util.KeySaver;

public class GridActivity extends Activity implements ConnectionListener{

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	
		super.onConfigurationChanged(newConfig);
		gv.setAdapter(ia);
		refreshGrid();
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
		
		img_back.setClickable(false);
	}
	
	private void setListeners() {
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int position,
					long arg3) {


				lastPosition= position;
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
				ia.notifyDataSetChanged();
				gv.refreshDrawableState();
			    //refreshGrid();
				
			}
		});
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG,"Es: " + img_back.isClickable());
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
				if (firstTime==0)
					callNextActivity(Constants.CATUCHG);
					GridActivity.this.finish();
			}
				
			}
		});
	}

	@Override
    public void onBackPressed() {
        //start activity here
        callNextActivity(Constants.CATFIN);
        super.onBackPressed();
        GridActivity.this.finish();
    }
	
	private void dibujar_grilla(){
		if(firstTime==1){
			showAlertDialog(GridActivity.this, "Bienvenido!!", "Elija al menos una categoria que sea de su interes."); 
			img_back.setBackgroundResource(R.drawable.icon_next);
			
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
	
	private void refreshGrid(){

		int firstVPosition = gv.getFirstVisiblePosition();
		int lastVPosition = gv.getLastVisiblePosition();
		ia.notifyDataSetChanged();
		/*gv.setAdapter(ia);
		gv.invalidate();
		gv.smoothScrollToPosition(lastPosition);**/
		
		/*
		if (lastPosition<(lastVPosition-1))gv.setSelection(firstVPosition);
		else gv.setSelection(firstVPosition+2);*/
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
		
	

	public String selectImaginItem(int id)
	{
		switch(id){
		case 1:
			return "http://2.bp.blogspot.com/-DGhEucdOWNQ/UVdlc67ID9I/AAAAAAAAAAY/YsHzAiSCy9o/s1600/Amor-tecnol%25C3%25B3gico.jpg";
		case 2:
			return "http://2.bp.blogspot.com/-DGhEucdOWNQ/UVdlc67ID9I/AAAAAAAAAAY/YsHzAiSCy9o/s1600/Amor-tecnol%25C3%25B3gico.jpg";
		case 3:
			return "http://2.bp.blogspot.com/-DGhEucdOWNQ/UVdlc67ID9I/AAAAAAAAAAY/YsHzAiSCy9o/s1600/Amor-tecnol%25C3%25B3gico.jpg";
			
		case 4:
			return "http://2.bp.blogspot.com/-DGhEucdOWNQ/UVdlc67ID9I/AAAAAAAAAAY/YsHzAiSCy9o/s1600/Amor-tecnol%25C3%25B3gico.jpg";
			
		case 5:
			return "http://2.bp.blogspot.com/-DGhEucdOWNQ/UVdlc67ID9I/AAAAAAAAAAY/YsHzAiSCy9o/s1600/Amor-tecnol%25C3%25B3gico.jpg";
		case 6:
			return "http://2.bp.blogspot.com/-DGhEucdOWNQ/UVdlc67ID9I/AAAAAAAAAAY/YsHzAiSCy9o/s1600/Amor-tecnol%25C3%25B3gico.jpg";
		case 8:
			return "http://ec2-54-232-205-219.sa-east-1.compute.amazonaws.com/linguoo/PublicTempStorage/multimedia/men_yoga_80d84a4035dc446cb2b0f647a5bf0e34.jpg";
		default:
			return "http://ec2-54-232-205-219.sa-east-1.compute.amazonaws.com/linguoo/PublicTempStorage/multimedia/CLP-brain-vector-shutter-stock_bac71bc38d784a0d9e0336dbb5fce1f6.jpg";
		}
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
		img_back.setClickable(true);

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
					Log.v("ITEM", " IMAGEN: "+im.getImageView()+" SELECTED: "+ im.getImageSelected() 
							+" CATEGORIA: "+im.getTextCategoria() 
							+" ID: "+im.getId());
					arrayCategoria.add(im);
					selected=0;
				}
				
			}catch (Exception e) {
				
				e.getMessage();
			}
				
				dibujar_grilla();
			
			break;
		case Constants.CATEGI:
			callNextActivity(Constants.CATCHG);
			finish();
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

	private void callNextActivity(int status) {
		if (firstTime==1){			
			KeySaver.saveShare(this, "FirstTime", 0);
			Intent in=new Intent(GridActivity.this, LinguooNewsActivity.class);
			startActivity(in);			
		}else{
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result",status);
			setResult(status,returnIntent);     
		}	
	}

	@Override
	public void cacheReady(int msg, String message) {
		
	}
	
}
