package com.mkiisoft.linguoo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.Button;
import android.widget.ImageView;

import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.HumanIt;

public class LinguooActivity extends Activity implements AccessibilityEventSource{
	private int overBtn=Constants.BTN_NONE;
	private int actualpos=-1;
	private long dblclktime = 0;

	private final String TAG = "Linguoo casa";

	private HumanIt humanit;
	private Button btn_alarm;
	private Button btn_lin_not;
	private Button btn_rss_not;
	private Button btn_lib;
	private Button btn_blog;
	private Button btn_off;
	private Button btn_fav;
	private Button btn_conf;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG,"Linguoo Main");
		humanit = new HumanIt(this);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_linguoo);
        setLingouooView();
		setListeners();
    }
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // .
        // Add code if needed
        // .
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (inControl(btn_rss_not,event)){
			overBtn=Constants.BTN_RSSNOTI;
		}
		else if (inControl(btn_lin_not,event)){
			overBtn=Constants.BTN_LNOTI;
		}
		else if (inControl(btn_lib,event)){
			overBtn=Constants.BTN_LIBROS;
		}
		else if(inControl(btn_blog,event)){
			overBtn=Constants.BTN_BLOGS;
		}
		else if (inControl(btn_conf,event)){
			overBtn=Constants.BTN_CONF;
		}
		else if (inControl(btn_off,event)){
			overBtn=Constants.BTN_SALIR;
		} else{
			overBtn=Constants.BTN_NONE;
		}
		switch(event.getAction()){
		case MotionEvent.ACTION_MOVE:
			if (overBtn!=Constants.BTN_NONE) doActionMove(overBtn);
			else actualpos=Constants.BTN_NONE;
			break;
		case MotionEvent.ACTION_DOWN:
			if (overBtn!=Constants.BTN_NONE) doActionDown(overBtn);
			else actualpos=Constants.BTN_NONE;
			break;
		}

		return true;
	}

	private void doActionDown(int overBtn2) {
		long nowclick = System.currentTimeMillis();
		if (actualpos==overBtn2){
			if ((nowclick-dblclktime)>10000){
				humanit.sayIt(actualpos);
			} else if((nowclick-dblclktime)<500){
				lanzar(actualpos);	
			}
		}else{
			actualpos=overBtn2;
			humanit.sayIt(actualpos);
		}
		dblclktime=System.currentTimeMillis();
	}

	private void doActionMove(int overBtn2) {
		if (actualpos!=overBtn2){
			actualpos=overBtn2;
			humanit.sayIt(actualpos);
		}
			
	}

	private boolean inControl(Button btn,MotionEvent evt) {
		boolean result=false;
		int loc[]={0,0};
		btn.getLocationOnScreen(loc);
		int xtop=(int)(loc[0]+btn.getWidth()*0.1);
		int ytop=(int)(loc[1]+btn.getHeight()*0.1);
		int xbot=(int)(xtop+btn.getWidth()*0.9);
		int ybot=(int)(ytop+btn.getHeight()*0.9);
		if ((evt.getX()>xtop && evt.getX()<xbot)&&
				(evt.getY()>ytop && evt.getY()<ybot)){
			result = true;
		}
		return result;
	}

	@Override
	protected void onPostCreate (Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
	}

	protected void lanzar(int actividad) {
		Intent i = new Intent();
		Bundle b = new Bundle();
		boolean def=false;
		switch(actividad){
		case Constants.BTN_RSSNOTI:
			b.putInt("notiType", Constants.BTN_RSSNOTI);
			i.setClass(this, NoticiasActivity.class); break;
		case Constants.BTN_LNOTI:
			b.putInt("notiType", Constants.BTN_LNOTI);
			i.setClass(this, LinguooNoticiasActivity.class); break;
		case Constants.BTN_SALIR:
			def=true; break;
		default:
			i.setClass(this, LinguooActivity.class);def=true;break;
		}
		
		if (def==true){
			finish();
		}else{
			i.putExtras(b);
			startActivity(i);
		}
	}

/**
 * Aquí se declaran todos los listeners de esta Activity y se implementan las interfaces necesarias.
 */
	private void setListeners() {
	}

/**
 * Aquí se instancian todos los objetos del layout que se requieran como parte de la UI.
 */
	private void setLingouooView() {
		setContentView(R.layout.main_linguoo);
		btn_alarm = (Button) findViewById(R.id.btn_alarm);
		btn_lin_not = (Button) findViewById(R.id.btn_lin_not);
		btn_rss_not = (Button) findViewById(R.id.btn_rss_not);
		btn_lib = (Button) findViewById(R.id.btn_lib);
		btn_blog = (Button) findViewById(R.id.btn_blog);
		btn_off = (Button) findViewById(R.id.btn_off);
		btn_fav = (Button) findViewById(R.id.btn_fav);
		btn_conf = (Button) findViewById(R.id.btn_conf);
        humanit.sayIt(Constants.WELCOME);
	}

	
	/*
	 * Se implementa AccesibilityEventSource por si el usuario tiene activadas
	 * las funciones de accesibilidad no interfieran con el funcionamiento de la
	 * aplicación.
	 *  (non-Javadoc)
	 * @see android.view.accessibility.AccessibilityEventSource#sendAccessibilityEvent(int)
	 */
	@SuppressLint("NewApi") @Override
	public void sendAccessibilityEvent(int arg0) {
	}

	@Override
	public void sendAccessibilityEventUnchecked(AccessibilityEvent arg0) {
	}
	
	@Override
	public void onStop(){
		humanit.destroyMp();
		super.onStop();
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
}
