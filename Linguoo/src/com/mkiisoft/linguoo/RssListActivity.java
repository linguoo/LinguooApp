package com.mkiisoft.linguoo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mkiisoft.linguoo.util.GraphicsUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class RssListActivity extends Activity implements OnInitListener,AccessibilityEventSource{
	private static final String TAG = "RSSListActivity";
	private static final String URL = "http://www.infobae.com/rss/politica.xml";
	private static final int POSBACK = -10;
	private String url;
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech myTTS;
	private ListView lv;
	private CustomRssFeedAdapter adapter;
	private ImageView btnBack;
	private long backclicktime;
	private long itemclicktime;
	private int actualpos=-1;
	private ArrayList<RssFeed> list;
	private Activity activity;
	private int categoryid ;
	private String [] category;
	private int ttsInited =-1;
	private int numNoti;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extra = getIntent().getExtras();
        categoryid = extra.getInt("subcat");
        category = getResources().getStringArray(R.array.CatNames);
		super.onCreate(savedInstanceState);
        activity = this;
        setTheView();
		setListener();
		Log.d(TAG,"DIP: "+GraphicsUtils.getDeviceDip(this));
		PointF sdp = GraphicsUtils.getWindowSizeDp(this);
		Log.d(TAG,"Screen W: "+sdp.x+" ; H: "+sdp.y);
		numNoti =(int) (sdp.y-110)/61;
		Log.d(TAG,numNoti+" noticias por pantalla");
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // .
        // Add code if needed
        // .
    }

	private void setListener() {
		// TODO Auto-generated method stub
		
	}

	private void setTheView() {
		list = new ArrayList<RssFeed>();
		setContentView(R.layout.catfeed_layout);
		lv = (ListView) this.findViewById(R.id.lv);
		btnBack = (ImageView) this.findViewById(R.id.imgBack);
		setRss();
		new LeerRss().execute(url);

	}


	private void setRss() {
		Log.d(TAG,category[categoryid]);
		try{
			url = getResources().getStringArray(R.array.rssList)[categoryid];
		}catch (Exception e){
			url = URL;
		}
	}

	private boolean inControl(View imgV,MotionEvent evt) {
		boolean result=false;
		int loc[]={0,0};
		imgV.getLocationOnScreen(loc);
		int xtop=(int)(loc[0]+imgV.getWidth()*0.1);
		int ytop=(int)(loc[1]+imgV.getHeight()*0.1);
		int xbot=(int)(xtop+imgV.getWidth()*0.9);
		int ybot=(int)(ytop+imgV.getHeight()*0.9);

		if ((evt.getX()>xtop && evt.getX()<xbot)&&
				(evt.getY()>ytop && evt.getY()<ybot)){
			result = true;
		}
		return result;
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
						CustomRssFeedAdapter adaptador = (CustomRssFeedAdapter)lv.getAdapter();
						speakWords((String)adaptador.getItem(position).getTitle());
						actualpos=position;
					}
				}
			}
			if (inControl(btnBack,ev)){
				if (actualpos!=POSBACK){
					speakWords("Regresar");
					actualpos=POSBACK;
				}
			}
			result = true;
			break;
		case MotionEvent.ACTION_DOWN:
			
			if (inControl(lv,ev)){
				nowclick = System.currentTimeMillis();
				if (position>=0 && position<lv.getCount()){
					Log.d(TAG,"position: "+position);
					Log.d(TAG,"actualpos: "+actualpos);
					if ((nowclick-itemclicktime)<300 && actualpos==position){
						//launch((String)lv.getAdapter().getItem(position));
					}else if ((nowclick-itemclicktime)>700 && actualpos==position){
						CustomRssFeedAdapter adaptador = (CustomRssFeedAdapter)lv.getAdapter();
						speakWords((String)adaptador.getItem(position).getTitle());
					}
				}
				itemclicktime=System.currentTimeMillis();
			}
			if (inControl(btnBack,ev)){
				nowclick = System.currentTimeMillis();
					if ((nowclick-backclicktime)<300){
						finish();
					}else if ((nowclick-backclicktime)>700){
						speakWords("Regresar");
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

    private class LeerRss extends AsyncTask<String, String, String>{
    	
		@Override
		protected String doInBackground(String... dir) {
			String result=null;
			URL url=null;
			int response=-1;
			HttpURLConnection conn;
			Document doc;
			list.clear();
			Log.d(TAG,dir[0].toString());
			
			try {
				url = new URL(dir[0].toString());
				conn = (HttpURLConnection) url.openConnection();
				response =conn.getResponseCode(); 
				 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				 DocumentBuilder db = dbf.newDocumentBuilder();
				 doc = db.parse(url.openStream());
				}catch (Exception e) {
					return null;
				}

				if (response==HttpURLConnection.HTTP_OK){
					 doc.getDocumentElement().normalize();
					 NodeList itemLst = doc.getElementsByTagName("item");
					 int items;
					 if (itemLst.getLength()>numNoti){
						 items=numNoti;
					 }else{
						 items=itemLst.getLength();
					 }
					 for (int i = 0; i < items; i++){
						 Node item = itemLst.item(i);
						 if (item.getNodeType() == Node.ELEMENT_NODE){
							 String title,link,content;
							 
							 Element ielem = (Element) item;
							 try{
								 title = ielem.getElementsByTagName("title").item(0).getChildNodes().item(0).getNodeValue();
							 }catch(Exception e){
								 title="";
							 }
							 try{
								 link = ielem.getElementsByTagName("link").item(0).getChildNodes().item(0).getNodeValue();
							 }catch(Exception e){
								 link="";
							 }
							 try{
								 content = ielem.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue();
							 }catch(Exception e){
								 content="";
							 }
							 Log.d(TAG,title);
							 Log.d(TAG,link);
							 Log.d(TAG,content);
							 //String mediaurl = ielem.getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getNodeValue();
							 RssFeed feed=new RssFeed(title,link,content);
							 list.add(feed);
						 }
                     }
					 result = "done";
				}

			return result;
		}
		
		protected void onPostExecute(String result) {
			if (result!=null){
				RssListActivity.this.ready(1, "done");
			}else{
				RssListActivity.this.ready(0, "error");
			}

        }
    	
    }
	
	public void ready(int msg, String message) {
		// TODO Auto-generated method stub
		if (msg==1){
			adapter = new CustomRssFeedAdapter(this,R.layout.catfeed_row_layout,list);
			lv.setAdapter(adapter);
			if(ttsInited>0){
				myTTS.stop();
				speakWords("Noticias Cargadas");
			}
		}
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
				speakWords("Cargando noticias de " + getResources().getStringArray(R.array.CatNames)[categoryid]);
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
				ttsInited=1;
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
