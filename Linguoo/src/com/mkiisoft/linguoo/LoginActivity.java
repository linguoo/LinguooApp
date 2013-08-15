package com.mkiisoft.linguoo;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.mkiisoft.linguoo.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private static final String TAG = "Linguoo Login Activity";
	protected static final int REGISTERUSER = 90210;
	private TextView et_usu;
	private TextView et_pass;
	private Button btn_login;
	private Button btn_off;
	private Button btn_reg;
	private Button btn_conf;
	private LoginAsync loginAsync;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		//Log.d(TAG,"Linguoo Main");
		
        super.onCreate(savedInstanceState);
        setLingouooView();
		setListeners();
    }

	private void setListeners() {
		//Boton de Registro
		btn_reg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(LoginActivity.this,RegisterUserActivity.class);
				LoginActivity.this.startActivityForResult(i, REGISTERUSER);
			}
			
		});
		
		//Boton de Login
		btn_login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				if ((et_usu.getText().toString().equals("")==false)&&
						(et_pass.getText().toString().equals("")==false)){
					String httpws = Constants.WSLOGIN;
					httpws+=et_usu.getText().toString()+","+et_pass.getText().toString();
					Log.d(TAG,"httpws");
					btnstate(0);
					loginAsync = new LoginAsync();
					loginAsync.execute(httpws);
				}
			}
			
		});
		btn_off.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
			}
			
		});
		
		
	}

	protected void btnstate(int i) {
		if (i==0){
			btn_login.setClickable(false);
			btn_reg.setClickable(false);
			btn_conf.setClickable(false);
		}else{
			btn_login.setClickable(true);
			btn_reg.setClickable(true);
			btn_conf.setClickable(true);
		}
	}

	private void setLingouooView() {
		loginAsync = new LoginAsync();
		setContentView(R.layout.login_layout);
		et_usu = (TextView)findViewById(R.id.et_usu);
		et_pass = (TextView)findViewById(R.id.et_pass);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_off = (Button)findViewById(R.id.btn_off);
		btn_reg = (Button)findViewById(R.id.btn_reg);
		btn_conf = (Button)findViewById(R.id.btn_conf);
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==REGISTERUSER){
			if (resultCode==RESULT_OK){
				/*se guarda el UsuLog en el keystore y se da por iniciada
				 * La sesion del usuario,
				 * Se inicia la clase LinguooActivity.class
				 * y luego se finaliza esta Activity
				 */
				Intent i = new Intent(this,LinguooActivity.class);
				startActivity(i);
				finish();
			}else{
				Log.d(TAG,"Se canceló el registro de usuario");
			}
		}
	}

	public class LoginAsync extends AsyncTask<String,String,String>{

		@Override
		protected String doInBackground(String... params) {
			String result=null;
			try{
				HttpClient httpClient = new DefaultHttpClient();
			    HttpGet httpGet = new HttpGet(params[0]);

			    HttpResponse httpResponse = httpClient.execute(httpGet);
			    HttpEntity httpEntity = httpResponse.getEntity();
			    result = EntityUtils.toString(httpEntity);
			    Log.d(TAG,result);
			    
				JSONArray jsonArray = new JSONArray(result);
				Log.d(TAG,"objetos: "+jsonArray.length());
				/*for(int i=0;i<jsonArray.length();i++){
					Log.d(TAG,jsonArray.getJSONObject(i).toString());
				}*/

			}catch(Exception e){
				Log.e(TAG,e.toString());
			}
			return result;
		}
		
		@Override
		protected void onPostExecute (String result){
				btnstate(1);
		}
		
		
	}
}
