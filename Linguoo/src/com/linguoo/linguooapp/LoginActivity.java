package com.linguoo.linguooapp;


import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.linguoo.linguooapp.async.AsyncConnection;
import com.linguoo.linguooapp.async.ConnectionListener;
import com.linguoo.linguooapp.util.Constants;
import com.linguoo.linguooapp.util.KeySaver;

public class LoginActivity extends Activity implements ConnectionListener{

	private static final String TAG = "Linguoo Login Activity";

	private String page;
	private TextView et_usu;
	private TextView et_pass;
	private TextView et_email;
	private Button btn_login;
	private Button btn_reg;
	private Button btn_recover;
	private int state=Constants.LOGIN;
	private int laststate;
	private ProgressBar pb_logreg;
	private WebView webView;
	private ImageView imgheader;
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		KeySaver.saveShare(this, "state", Constants.LOGREG);
		setLingouooView();
		setListeners();
	}

	private void setListeners() {
		btn_login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String error="";
				//Uso del botón loguin para ingresar a linguoo
				if (state==Constants.LOGIN){
					if(et_usu.getText().toString().equals("")==true){
						error=error+"Ingrese un Nombre de Usuario o E-mail. ";
					}
					if(et_pass.getText().toString().equals("")==true){
						error+="Ingrese la contraseña";
					}
					if (error.equals("")==false){
						showAlertDialog(LoginActivity.this,"Linguoo",error);
					}else{
						page=Constants.WSLOGIN+et_usu.getText().toString()+","+et_pass.getText().toString();
						pb_logreg.setVisibility(View.VISIBLE);
						Log.d("Linguoo_Login",page);
						enablebtns(false);
						AsyncConnection.getInstance(page, LoginActivity.this, Constants.LOGIN).execute();
						//Loguear al usuario y guardar token
						// AsyncConnection.getInstance(WS, this, Constants.LOGIN).execute();
					}
				}
				//uso del botón login para cancelar el registro de nuevo usuario
				else{
					if (state==Constants.RECOVER){
						btnstate(laststate);
					}else{
						btnstate(Constants.LOGIN);
					}
				}
			}

		});

		btn_reg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String error="";
				//Uso del botón registrarse para acceder al formulario de registro
				if (state==Constants.LOGIN){
					btnstate(Constants.REGUSER);
				}
				//Uso del botón registrarse para validar el formulario y registrar al usuario
				else if(state==Constants.RECOVER){
					if(et_email.getText().toString().equals("")==true){
						error+="Ingrese su cuenta de e-mail";
					}else if(validateEmail(et_email.getText().toString())==false){
						error+="No es un e-mail válido";
					}
					if (error.equals("")==false){
						showAlertDialog(LoginActivity.this,"Linguoo",error);
					}else{
						//Enviar e-mail para recuperar password
						
						// AsyncConnection.getInstance(WS, this, Constants.RECOVER).execute();
					}
				}else{
					if(et_email.getText().toString().equals("")==true){
						error+="Ingrese su cuenta de e-mail ";
					}else if(validateEmail(et_email.getText().toString())==false){
						error+="No es un e-mail válido ";
					}
					if(et_usu.getText().toString().equals("")==true){
						error=error+"Ingrese un Nombre de Usuario. ";
					}
					if(et_pass.getText().toString().equals("")==true){
						error+="Ingrese la contraseña";
					}
					if (error.equals("")==false){
						showAlertDialog(LoginActivity.this,"Linguoo",error);
					}else{
						//registrar al usuario y guardar token
						page=Constants.WSREUSR+","+et_email.getText().toString()+","+
						et_usu.getText().toString()+","+et_pass.getText().toString()+",L";
						legalDialog();
					}

				}
			}


		});

		btn_recover.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				btnstate(Constants.RECOVER);
			}


		});


	}

	protected void btnstate(int i) {
		switch(i){
		case Constants.LOGIN:
			btn_reg.setText("Unirse");
			btn_login.setText("Loguearse");
			et_usu.setHint("Nombre de Usuario / E-mail");
			et_usu.setText("");
			et_pass.setText("");
			et_email.setText("");
			btn_recover.setVisibility(View.VISIBLE);
			et_usu.setVisibility(View.VISIBLE);
			et_pass.setVisibility(View.VISIBLE);
			et_email.setVisibility(View.GONE);
			state=Constants.LOGIN;
			break;
		case Constants.REGUSER:
			btn_reg.setText("Registrarse");
			et_usu.setHint("Nombre de Usuario");
			et_email.setText("");
			et_usu.setText("");
			et_pass.setText("");
			btn_login.setText("Cancelar");
			btn_recover.setVisibility(View.VISIBLE);
			et_email.setVisibility(View.VISIBLE);
			et_usu.setVisibility(View.VISIBLE);
			et_pass.setVisibility(View.VISIBLE);
			state=Constants.REGUSER;
			break;
		case Constants.RECOVER:
			btn_reg.setText("Recuperar");
			btn_login.setText("Cancelar");
			et_email.setText("");
			btn_recover.setVisibility(View.GONE);
			et_usu.setVisibility(View.GONE);
			et_email.setVisibility(View.VISIBLE);
			et_pass.setVisibility(View.GONE);
			laststate=state;
			state=Constants.RECOVER;
			break;
		default:
		}
	}

	private void setLingouooView() {
		
		setContentView(R.layout.login_layout);
		imgheader = (ImageView)findViewById(R.id.imgHeader);
		et_usu = (TextView)findViewById(R.id.et_usu);
		et_pass = (TextView)findViewById(R.id.et_pass);
		et_email = (TextView)findViewById(R.id.et_email);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_reg = (Button)findViewById(R.id.btn_reg);
		btn_recover = (Button)findViewById(R.id.btn_recover);
		btnstate(Constants.LOGIN);
		pb_logreg = (ProgressBar)findViewById(R.id.pb_logreg);
		loadImage();


	}

	private boolean validateEmail(String text) {
		boolean result = false;
		String domain;
		if ((text.equals("")==false)&&(text.contains("@")==true)){
			domain = text.substring(text.indexOf("@")+1, text.length());
			//Log.d(TAG,domain);
			if ((domain.contains("@")==false)&&(domain.contains(".")==true)&&
					(domain.substring(domain.indexOf(".")).length()>1)){
				result = true;
			}
		}

		return result;
	}


	public void showAlertDialog(Context context, String title, String message) {
		final SpannableString s = new SpannableString(message);
	    Linkify.addLinks(s, Linkify.ALL);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
		.setIcon(R.drawable.linguoo)
		.setMessage(s)
		.setCancelable(false)
		.setNegativeButton("Close",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}


	public void enablebtns(boolean i) {
		btn_login.setClickable(i);
		btn_reg.setClickable(i);
		btn_recover.setClickable(i);
		et_email.setEnabled(i);
		et_pass.setEnabled(i);
		et_usu.setEnabled(i);
	}

	@Override
	public void ready(int msg, String message) {
		pb_logreg.setVisibility(View.GONE);
		enablebtns(true);
		switch(msg){
		case Constants.REGUSER:
		case Constants.LOGIN:
			try {
				JSONObject respuesta =new JSONObject(message);
				if (respuesta.getInt("code")==1){
					String temp = respuesta.getJSONObject("usu").getString("UsuLog");
					KeySaver.saveShare(this,"UsuLog" ,temp);
					temp = respuesta.getJSONObject("usu").getString("UsuCod");
					KeySaver.saveShare(this,"UsuCod", temp);
					if (msg==Constants.REGUSER)
						KeySaver.saveShare(this, "FirstTime",Constants.FT_YES);
					else KeySaver.saveShare(this, "FirstTime",Constants.FT_NO);
					
					launch(KeySaver.getIntSavedShare(this, "FirstTime"));
				}else{
					if(msg==Constants.REGUSER)
						Toast.makeText(this,"Error durante el registro, intentelo nuevamente", Toast.LENGTH_SHORT).show();
					else if (msg==Constants.LOGIN)
						Toast.makeText(this,"Error durante el ingreso, intentelo nuevamente", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Constants.RECOVER:
			Log.d("Login_Linguoo","Respuesta; "+message);			
			break;
		case AsyncConnection.NOCONNECTION:
		case AsyncConnection.ERROR:
			showAlertDialog(LoginActivity.this, "Ha ocurrido un Error", "Por favor, intentelo mas tarde");
			break;
		}
	}

	@Override
	public void cacheReady(int msg, String message) {
	}

	protected void launch(int act) {
		Intent i = null;
		switch(act){
		case Constants.FT_NO: //lanzar la configuracion de categorías una vez configurado
			//setear la key firstime en 1
			i= new Intent(this,LinguooNewsActivity.class);
			break;
		case Constants.FT_YES:// Si se loguea por primera vez se da la opcion de configurar si no se pasa
			//directamente a las noticias
			i= new Intent(this,GridActivity.class);
			break;
		}
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}
	
	private void legalDialog() {
		LinearLayout container = new LinearLayout(this);
		container.setMinimumWidth(200);
		container.setMinimumHeight(320);
		webView = new WebView(this);
		webView.setMinimumWidth(200);
		webView.setMinimumHeight(380);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setLightTouchEnabled(true);
		webView.setFocusable(true);
		webView.setFocusableInTouchMode(true);
		webView.setClickable(true);
		webView.requestFocus(View.FOCUS_DOWN);
		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_UP:
					if (!v.hasFocus()) {
						v.requestFocus();
					}
					break;
				}
				return false;
			}
		});
		container.addView(webView);
		webView.loadUrl("file:///android_asset/legal.html");
		Builder webDialog = new AlertDialog.Builder(this);
		webDialog.setView(container).setTitle("Términos y Condiciones")
				.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
							//aceptar y registrar Usuario
							pb_logreg.setVisibility(View.VISIBLE);
							Log.d("Linguoo_Login",page);
							enablebtns(false);
							AsyncConnection.getInstance(page, LoginActivity.this, Constants.REGUSER).execute();
							dialog.dismiss();
							
						
					}
				}).setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						dialog.dismiss();
					}
				})
				.show();
	}
	
	private void loadImage(){
		Random rand = new Random();
		int rndInt = rand.nextInt(4) + 1; // n = the number of images, that start at idx 1
		String imgName = "img_" + rndInt;
		int id = getResources().getIdentifier(imgName, "drawable", getPackageName());  
		imgheader.setImageResource(id); 
	}
}
