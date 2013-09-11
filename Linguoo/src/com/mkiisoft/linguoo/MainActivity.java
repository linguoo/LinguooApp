package com.mkiisoft.linguoo;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.KeySaver;

public class MainActivity extends Activity{
	

	private ImageView imgHeader;
	private Button btn_logreg;
	private Button btn_testver;
	private String usuLog;
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		//Log.d(TAG,"Linguoo Main");
		int state =KeySaver.getIntSavedShare(this, "state");
		usuLog = KeySaver.getStringSavedShare(this, "UsuLog");
		if(state>-1 && state!=Constants.MAIN){
			launch(state);
		}else{
			KeySaver.saveShare(this, "state", Constants.MAIN);
		}
        super.onCreate(savedInstanceState);
        setLingouooView();
		setListeners();
    }

	private void setListeners() {
		btn_logreg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				launch(Constants.LOGREG);
			}
			
		});
		
		btn_testver.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				launch(Constants.TESTVER);
				
				
			}
			
		});
	}

	protected void launch(int act) {
		Intent i = null;
		KeySaver.saveShare(MainActivity.this, "state", act);
		switch(act){
		case Constants.TESTVER:
			KeySaver.saveShare(MainActivity.this, "UsuLog", "2F5B3F3CB0");
			KeySaver.saveShare(MainActivity.this, "UsuCod", "prueba");
			i= new Intent(MainActivity.this,LinguooNewsActivity.class);
			break;
		case Constants.LOGREG:
			i= new Intent(MainActivity.this,LoginActivity.class);
			break;
		case Constants.NEWS:
			i= new Intent(MainActivity.this,LinguooNewsActivity.class);
			break;
		case Constants.CATEG:
			i= new Intent(MainActivity.this,GridActivity.class);
			break;
		default:
		}
		MainActivity.this.startActivity(i);
		if (act == Constants.NEWS || act == Constants.CATEG){
			finish();
		}
	}

	private void setLingouooView() {
		setContentView(R.layout.main_linguoo);
		imgHeader = (ImageView) this.findViewById(R.id.imgHeader);
		btn_logreg = (Button) this.findViewById(R.id.btn_logreg);
		btn_testver = (Button) this.findViewById(R.id.btn_testver);
		loadImage();
	}

	private void loadImage(){
		Random rand = new Random();
		int rndInt = rand.nextInt(4) + 1; // n = the number of images, that start at idx 1
		String imgName = "img_" + rndInt;
		int id = getResources().getIdentifier(imgName, "drawable", getPackageName());  
		imgHeader.setImageResource(id); 
	}
	
}
