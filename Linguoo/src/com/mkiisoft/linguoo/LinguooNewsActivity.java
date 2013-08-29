package com.mkiisoft.linguoo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;
import android.view.View;
import android.view.View.OnClickListener;
import com.mkiisoft.linguoo.CustomNewsAdapter.newsHolder;
import com.mkiisoft.linguoo.async.AsyncConnection;
import com.mkiisoft.linguoo.async.ConnectionListener;
import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.Dialogs;
import com.mkiisoft.linguoo.util.KeySaver;

public class LinguooNewsActivity extends Activity implements ConnectionListener {
	protected static final String TAG = "Linguoo Noticias";
		
	private RelativeLayout newsFooterLayout;
	private FrameLayout newsContentLayout;
	private ListView newsList;
	private ProgressBar mainLoader;
	
	private String page;	
	private CustomNewsAdapter adapter;
	private btnAddClick btnAdd;
	private itemClick itemPlay;
	private newsHolder nHolder;
	private ArrayList<HashMap<String, String>> arrayNewsList;
	private LinguooPlayList playList;
	private LinguooMediaPlayer player;
	private Button btnAddCategory; 
	private Button btnNextNews;
	private ToggleButton btnPlayPause;
	private ProgressBar progressbar;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView();
        initializeLists();
        initializePlayer();
        loadNews();        
        addListeners();
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	@Override
	public void onStart(){
		super.onStart();		
	}

	@Override
	public void ready(int msg, String message) {
		// TODO Auto-generated method stub
		JSONArray newsData = null;
		try {
			switch(msg){
				case Constants.NEWS:
					newsData = new JSONArray(message);
					populateNewsList(newsData);
					break;
				case 500:
					Dialogs.showAlertDialog(LinguooNewsActivity.this, "Conexión", "Se produjo un error al intenet recuperar las noticias. Error: " + msg, "Cerrar");
					break;
			}
				
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		hideLoading();
	}

	@Override
	public void cacheReady(int msg, String message) {
		// TODO Auto-generated method stub
		
	}
	
		
	/*  *********************************************************************************************** */
	
	private void configureView(int visibility){
		switch(visibility){
			case Constants.NEWS_INVISIBLE:
				newsFooterLayout.setVisibility(RelativeLayout.INVISIBLE);
				newsContentLayout.setVisibility(FrameLayout.INVISIBLE);
				newsList.setVisibility(ListView.INVISIBLE);
				mainLoader.setVisibility(ProgressBar.INVISIBLE);
				break;
			case Constants.NEWS_VISIBLE:
				newsFooterLayout.setVisibility(RelativeLayout.VISIBLE);
				newsContentLayout.setVisibility(FrameLayout.VISIBLE);
				newsList.setVisibility(ListView.VISIBLE);
				mainLoader.setVisibility(ProgressBar.VISIBLE);
				break;
		}
			
	}
		
	private void setMainView(){
		setContentView(R.layout.news_layout);
		newsFooterLayout = (RelativeLayout) this.findViewById(R.id.newsFooterLayout);
		newsContentLayout = (FrameLayout) this.findViewById(R.id.newsContentLayout);
		newsList = (ListView) this.findViewById(R.id.lstNews);
		mainLoader = (ProgressBar) this.findViewById(R.id.mainLoader);
		btnAddCategory = (Button)this.findViewById(R.id.btnAddCategory);
		btnPlayPause = (ToggleButton)this.findViewById(R.id.btnPlayPause);
		btnNextNews = (Button)this.findViewById(R.id.btnFF);
		progressbar = (ProgressBar) this.findViewById(R.id.progressbar);
		configureView(Constants.NEWS_INVISIBLE); 
	}
	
	private void initializeLists(){
		arrayNewsList = new ArrayList<HashMap<String, String>>();
		playList = new LinguooPlayList();
	}
	
	private void initializePlayer(){
		PlayerCallback playerCallback = new PlayerCallback();
		player = new LinguooMediaPlayer(this, playerCallback);
	}
	
	private void loadNews(){
		Log.d(TAG,"Recuperando noticias...");
		showLoading();
		page = Constants.WSGETNEWS;
		AsyncConnection.getInstance(page, LinguooNewsActivity.this, Constants.NEWS).execute();
	}
	
	@SuppressWarnings("unused")
	private String getLocalStoredNews(){
		HashMap<String, String> localKeys; 
		ArrayList<HashMap<String, String>> localNews = new ArrayList<HashMap<String, String>>();
		Map<String,?> store = null;
		Integer total_news = 0;
		
		total_news = KeySaver.getIntSavedShare(LinguooNewsActivity.this, "total_news");
		store = KeySaver.getAll(LinguooNewsActivity.this);
		Object[] storeKeyArray;
		Object[] storeValueArray;
		
		storeKeyArray = store.keySet().toArray();
		
		for(int i = 0; i < total_news; i++){
			localKeys = new HashMap<String, String>();
			for(int b = 0; b < storeKeyArray.length; b++){
				String patternStr = KeySaver.getPrefix() + Constants.NEWS_KEY_PREFIX + "_" + i + "_";
				Pattern pattern = Pattern.compile(patternStr);
				Matcher matcher = pattern.matcher(storeKeyArray[b].toString());
				while(matcher.find()){
					storeValueArray =  store.values().toArray();
					String key = storeKeyArray[b].toString().substring(patternStr.length(), storeKeyArray[b].toString().length());
					String value = storeValueArray[b].toString();
					localKeys.put(key,value);
				}
			}
			localNews.add(localKeys);
			
		}		
		return localNews.toString();
	}	
	
	private void populateNewsList(JSONArray newsData) throws JSONException{
		Log.d(TAG,"Cargando noticias...");
		
		configureView(Constants.NEWS_VISIBLE);
		for(int i = 0; i < newsData.length(); i++){
			HashMap<String, String> curNews = new HashMap<String, String>();
			for(int b = 0; b < newsData.getJSONObject(i).length(); b++){
				String key = newsData.getJSONObject(i).names().optString(b);
				String value = newsData.getJSONObject(i).getString(newsData.getJSONObject(i).names().optString(b));
				curNews.put(key,value);
			}
			curNews.put(Constants.NEWS_ONPLAYLIST, "false");
			registerNews(curNews,i,newsData.length());
		}
		launchNews();
	}
	
	private void registerNews(HashMap<String, String> curNews, Integer index, Integer total){
		Iterator<String> iNews = curNews.keySet().iterator();

		arrayNewsList.add(curNews);
	    
        while(iNews.hasNext()){
        	String key = (String)iNews.next();
        	String value = (String)curNews.get(key);
        	
        	key = Constants.NEWS_KEY_PREFIX  + "_" + index + "_" + key;
        	KeySaver.saveShare(LinguooNewsActivity.this, key, value);
        }

        KeySaver.saveShare(LinguooNewsActivity.this, "total_news", total);
    }

	private void launchNews(){
		btnAdd = new btnAddClick();
		itemPlay = new itemClick();
		adapter = new CustomNewsAdapter(this, R.layout.news_item_layout, arrayNewsList, itemPlay, btnAdd);        
		newsList.setAdapter(adapter);
		updateAdapter();
	}

	private void updateArrayNewsList(Integer index, Boolean addRemoveFlag){
		HashMap<String, String> curNews;
		
		curNews = arrayNewsList.get(index);
		curNews.put(Constants.NEWS_ONPLAYLIST, addRemoveFlag.toString());
		
		arrayNewsList.set(index, curNews);
		
		if(addRemoveFlag){
			playList.add(curNews);
		}else{
			playList.remove(curNews);
		}
	}
	
	private void updateAdapter(){
		adapter.notifyDataSetChanged();
	}
		
	private void playNews(View v){
		nHolder = (newsHolder)v.getTag();
		player.play(nHolder.getAudioURL());	
	}
	
	private void addRemoveNews(View v){
		View vParent = (View) v.getParent();
		ToggleButton btnAdd = (ToggleButton)v;
		nHolder = (newsHolder)vParent.getTag();
				
		if(btnAdd.isChecked()){
			updateArrayNewsList(nHolder.getItemPosition(), true);
			btnAdd.setBackgroundResource(R.drawable.btn_add_off);
		}else{
			updateArrayNewsList(nHolder.getItemPosition(), false);
			btnAdd.setBackgroundResource(R.drawable.btn_add_on);
		}
		updateAdapter();
	}
	
	private void showLoading(){
		mainLoader.setVisibility(ProgressBar.VISIBLE);
	}
	
	private void hideLoading(){
		mainLoader.setVisibility(ProgressBar.INVISIBLE);
	}
	
	/*  *********************************************************************************************** */

	private void addListeners(){
		btnAddCategory.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Dialogs.showAlertDialog(LinguooNewsActivity.this, "Activity Categorías", "Activity Categorías...", "Cerrar");
			}			
		});
		
		btnPlayPause.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToggleButton btnPP = (ToggleButton)v;
				if(playList.getTotal() > 0){					
					if(btnPP.isChecked()){
						btnPP.setBackgroundResource(R.drawable.btn_pause);
						String url = playList.getCurrentTrackURL();
						playList.setRepeatModeOn();
						player.play(url);
						playList.moveForward();
					}else{
						btnPP.setBackgroundResource(R.drawable.btn_play);
						player.pause();
					}
				}
			}
		});
		
		btnNextNews.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(playList.getTotal() == 0){				
					Dialogs.showAlertDialog(LinguooNewsActivity.this, "Playlist", "Debe agregar algunas noticias a su lista de reproducción.", "Cerrar");
				}else{
					String url = playList.getCurrentTrackURL();
					
					playList.setRepeatModeOn();
					player.play(url);
					playList.moveForward();
				}
			}
		});
	}	
	
	/*  *********************************************************************************************** */
	
	class itemClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			playNews(v);
		}		
	}
	
	class btnAddClick implements OnClickListener {
		@Override
		public void onClick(View v) {			
			addRemoveNews(v);
		}		
	}

	class PlayerCallback implements PlayerCallbackInterface{

		@Override
		public void playerStatus(int status, int value) {
			// TODO Auto-generated method stub
			switch(status){
				case Constants.NEWS_AUDIO_ERROR:
					Log.d(TAG,"Audio ERROR: " + value);
					break;
					
				case Constants.NEWS_AUDIO_LOADING:
					Log.d(TAG,"Audio Loading...");
					break;
					
				case Constants.NEWS_AUDIO_READY:
					Log.d(TAG,"Audio READY");
					break;
					
				case Constants.NEWS_AUDIO_PLAYING:
					progressbar.setProgress(value);
					Log.d(TAG,"Audio PLAYING: " + value);
					break;
					
				case Constants.NEWS_AUDIO_STOP:
					Log.d(TAG,"Audio STOP");
					break;
					
				case Constants.NEWS_AUDIO_PAUSE:
					Log.d(TAG,"Audio PAUSE");
					break;
					
				case Constants.NEWS_AUDIO_RESUME:
					Log.d(TAG,"Audio RESUME");
					break;

			}
		}
		
	}
	
	/*  *********************************************************************************************** */
	
	interface PlayerCallbackInterface {
		void playerStatus(int status, int value);
	}
	
}