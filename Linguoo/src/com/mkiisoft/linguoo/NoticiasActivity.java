package com.mkiisoft.linguoo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.GraphicsUtils;
import com.mkiisoft.linguoo.util.HumanIt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class NoticiasActivity extends Activity implements OnInitListener,AccessibilityEventSource{
	protected static final String TAG = "Linguoo Noticias";
	private static final int MY_DATA_CHECK_CODE = 0;

	private ListView lv;
	private String[] values;
	private int[] idValues;
	private HashMap<String, Integer> catMap;
	
	private ArrayList<String> categorias;
	private CustomCatAdapter adapter;
	private long itemclicktime=0,backclicktime=0;
	private TextToSpeech myTTS;
	private int actualpos=-1;
	private ImageView btnBack;
	private Activity activity;
	private HumanIt humanit;
	private int notiType;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		activity=this;
        super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        notiType=b.getInt("notiType",-1);
        Log.d(TAG,"noticias: "+notiType);
		setTheView();
		humanit = new HumanIt(this);
		setListener();

	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // .
        // Add code if needed
        // .
    }

	private void setListener() {
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
			}
			
		});
	}

	private void setTheView() {
		values=getResources().getStringArray(R.array.CatNames);
		idValues = getResources().getIntArray(R.array.CatID);
		catMap = new HashMap<String, Integer>();

		for (int i = 0; i < values.length; i++) {
		    catMap.put(values[i], idValues[i]);
		}
		
		categorias= new ArrayList<String>();
		categorias.clear();
		for (int i = 0; i < values.length; ++i) {
		      categorias.add(values[i]);
		    }
		adapter = new CustomCatAdapter(this,R.layout.catfeed_row_layout,categorias);
		setContentView(R.layout.catfeed_layout);
		lv = (ListView) this.findViewById(R.id.lv);
		lv.setAdapter(adapter);
		btnBack = (ImageView) this.findViewById(R.id.imgBack);
	}
	
    public boolean dispatchTouchEvent(MotionEvent ev) {
		int position; long nowclick;
		boolean result=false;
		int firstposition = lv.getFirstVisiblePosition();
		int posY=(int)(ev.getY()-GraphicsUtils.dpTopx(60, this));
		position = lv.pointToPosition((int)ev.getX(),posY)-firstposition;
		switch (ev.getAction()){
		case MotionEvent.ACTION_MOVE:
			if (inControl(lv,ev)){
				lv.requestFocusFromTouch();
				lv.setSelection(position);
				if (position>=0 && position<lv.getCount()){
					if (actualpos!=position){
						humanit.sayIt(catMap.get((String)lv.getAdapter().getItem(position)));
						//speakWords((String)lv.getAdapter().getItem(position));
						actualpos=position;
					}
				}
			}
			if (inControl(btnBack,ev)){
				if (actualpos!=Constants.BTN_REG){
					humanit.sayIt(Constants.BTN_REG);
					//speakWords("Regresar");
					actualpos=Constants.BTN_REG;
				}
			}
			result = true;
			break;
		case MotionEvent.ACTION_DOWN:
			
			if (inControl(lv,ev)){
				nowclick = System.currentTimeMillis();
				if (position>=0 && position<lv.getCount()){
					if ((nowclick-itemclicktime)<400 && actualpos==position){
						launch(position);
					}else if ((nowclick-itemclicktime)>700 && actualpos==position){
						humanit.sayIt(catMap.get((String)lv.getAdapter().getItem(position)));
						//speakWords((String)lv.getAdapter().getItem(position));
					}
				}
				itemclicktime=System.currentTimeMillis();
			}
			if (inControl(btnBack,ev)){
				nowclick = System.currentTimeMillis();
					if ((nowclick-backclicktime)<400){
						finish();
					}else if ((nowclick-backclicktime)>700){
						humanit.sayIt(Constants.BTN_REG);
						//speakWords("Regresar");
					}
				backclicktime=System.currentTimeMillis();
			}
			
			result = true;
			break;
		case MotionEvent.ACTION_UP:
			lv.clearFocus();
			lv.invalidate();
            result= true;
            break;
        default:
        	result = super.dispatchTouchEvent(ev);
        }
		/*if (result==false){
			return super.dispatchTouchEvent(ev);
		} else { return result;}*/
		return result;
    } 

	private void launch(int position) {
		shutdownTTS();
		Intent i = new Intent();
		Bundle b = new Bundle();
		if (notiType==Constants.BTN_RSSNOTI){
			i.setClass(this, RssListActivity.class);
			b.putInt("subcat", position);
		}else if(notiType==Constants.BTN_LNOTI){
			
		}
		i.putExtras(b);
		startActivity(i);
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
	
	/*
	 * 	Funciones de Inicializacion uso y destrucción de TTS
	 * 
	 */
		
		@SuppressLint("NewApi") @Override
		public void onInit(int initStatus) {
			Locale loc = new Locale("es","AR");
			if (initStatus == TextToSpeech.SUCCESS) {
				if(myTTS.isLanguageAvailable(loc)==TextToSpeech.LANG_AVAILABLE) myTTS.setLanguage(loc);
				speakWords("Menu de Noticias");
			}
			else if (initStatus == TextToSpeech.ERROR) {
				checkEngine();
	            Toast.makeText(activity.getBaseContext(), "Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
			}
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO &&
					android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				Log.d(TAG,myTTS.getDefaultEngine());
			}else if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
				List<EngineInfo> i = myTTS.getEngines();
				for (Object s : i){
					Log.d(TAG,s.toString());
				}
			}
			
		}
		
		/**
		 * Función para destruir el servicio de TTS.
		 * Primero verificará que no esté sintetizando una cadena de texto, en tal caso
		 * detendrá el proceso, luego apagará el servicio de tts y por último convertirá
		 * el objeto en null.
		 */
		private void shutdownTTS() {
		    if(myTTS != null) {
		    	if(myTTS.isSpeaking()==true){
					myTTS.stop();
				}
		        myTTS.shutdown();
		        myTTS=null;
		        Log.d(TAG, "TTS Destroyed");
		    }
		}

		@SuppressLint("NewApi") @Override
		public void sendAccessibilityEvent(int arg0) {
		}

		@Override
		public void sendAccessibilityEventUnchecked(AccessibilityEvent arg0) {
		}
		
		/**
		 * Verifica que haya un motor de síntesis instalado con sus correspondientes
		 * datos. Si el resultado no es Engine.CHECK_VOICE_DATA_PASS se le pedirá
		 * al usuario que instale un motor de síntesis
		 */
		private void checkEngine() {
	        Intent checkIntent = new Intent();
	        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
	        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
		}

		@Override
		public void onStop(){
			shutdownTTS();
			super.onStop();
		}
		
		@Override
		public void onStart(){
			super.onStart();
			try{
				myTTS = new TextToSpeech(this, this);
			}catch(Exception e){
				Log.e(TAG,e.toString());
				checkEngine();
			}
			
		}
		
		@SuppressLint("NewApi") protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == MY_DATA_CHECK_CODE) {
	            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	            	myTTS = new TextToSpeech(this, this);
	            }
	            else {
	                    Intent installIntent = new Intent();
	                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	                    startActivity(installIntent);
	            }
	     }
		}
		
		
		/**
		 * Función para sintetizar la cadena de texto speech.
		 * Asegurarse de haber inicializado correctamente el motor de síntesis antes de usarla.
		 * @param speech
		 */
		private void speakWords(String speech) {
	        //speak straight away
			myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
		}
		
	
}
