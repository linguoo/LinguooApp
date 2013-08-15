package com.mkiisoft.linguoo;

import com.mkiisoft.linguoo.custom.LinguooTextView;
import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.HumanIt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class CategoriesActivity extends Activity{
	public static final String TAG = "Linguoo Categories";
	private int overBtn;
	private int actualpos;
	private long clicktime;
	private HumanIt humanit;
	private Button btn_back;
	private Button btn_conf;
	private Button btn_fav;
	private Button btn_prev;
	private Button btn_next;
	private ListView lv;
	private LinguooTextView lbl_titu;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catfeed_layout);
        setLingouooView();
		setListeners();
    }

	private void setListeners() {
	}

	private void setLingouooView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_conf = (Button) findViewById(R.id.btn_conf);
		btn_fav = (Button) findViewById(R.id.btn_fav);
		btn_prev = (Button) findViewById(R.id.btn_prev);
		btn_next = (Button) findViewById(R.id.btn_next);
		lv = (ListView) findViewById(R.id.lv);
		lbl_titu = (LinguooTextView) findViewById(R.id.lbl_titu);
		}
	
	private boolean inControl(View imgV,MotionEvent evt) {
		boolean result=false;
		int loc[]={0,0};
		imgV.getLocationOnScreen(loc);
		int xtop=(int)(loc[0]);
		int ytop=(int)(loc[1]);
		int xbot=(int)(xtop+imgV.getWidth());
		int ybot=(int)(ytop+imgV.getHeight());
		if ((evt.getX()>xtop && evt.getX()<xbot)&&
				(evt.getY()>ytop && evt.getY()<ybot)){
			result = true;
		}
		return result;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		

		if (inControl(btn_fav,event)){
			overBtn=Constants.BTN_FAV;
		}
		else if (inControl(btn_prev,event)){
			overBtn=Constants.BTN_PREV;
		}
		else if(inControl(btn_next,event)){
			overBtn=Constants.BTN_NEXT;
		}
		else if (inControl(btn_conf,event)){
			overBtn=Constants.BTN_CONF;
		}
		else if (inControl(btn_back,event)){
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
			if ((nowclick-clicktime)>10000){
				humanit.sayIt(actualpos);
			} else if((nowclick-clicktime)<500){
				lanzar(actualpos);	
			}
		}else{
			actualpos=overBtn2;
			humanit.sayIt(actualpos);
		}
		clicktime=System.currentTimeMillis();
	}

	private void doActionMove(int overBtn2) {
		if (actualpos!=overBtn2){
			actualpos=overBtn2;
			humanit.sayIt(actualpos);
		}
			
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


}
