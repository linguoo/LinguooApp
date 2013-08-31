package com.mkiisoft.linguoo;

import java.util.Random;

import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.GraphicsUtils;
import com.mkiisoft.linguoo.util.KeySaver;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity{
	
	private int ws,hs,wi,hi;
	private ImageView imgHeader;
	private Button btn_logreg;
	private Button btn_testver;
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		//Log.d(TAG,"Linguoo Main");
		if(KeySaver.getStringSavedShare(this, "UsuLog")!=null){
			launch(Constants.NEWS);
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
		switch(act){
		case Constants.TESTVER:
			i= new Intent(MainActivity.this,LoginActivity.class);
			break;
		case Constants.LOGREG:
			i= new Intent(MainActivity.this,LoginActivity.class);
			break;
		case Constants.NEWS:
			i= new Intent(MainActivity.this,LinguooNewsActivity.class);
		}
		MainActivity.this.startActivity(i);
		finish();
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
