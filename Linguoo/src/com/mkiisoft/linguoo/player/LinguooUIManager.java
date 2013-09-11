package com.mkiisoft.linguoo.player;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mkiisoft.linguoo.R;
import com.mkiisoft.linguoo.player.LinguooNewsCustomAdapter.itemHolder;
import com.mkiisoft.linguoo.player.LinguooUIManagerInterface;
import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.ImageLoader;

public class LinguooUIManager implements OnClickListener{
	protected static final String TAG = "Linguoo UI Manager";
	
	private LinguooNewsManager newsManager;
	private LinguooUIManagerInterface uiInterface;
	private Activity activity; 
	
	/* Declaración de todos los controles y objetos que hacen el header de la UI */
	private int mainLayout;
	private int itemLayout;
	private ImageButton btnConfig;
	private ImageButton btnAutoPlay;
	private ImageButton btnUser;
	
	/* Declaración de todos los controles y objetos del contenedor de imagen de la noticia actual */
	private FrameLayout newsImageHeaderLayout;
	private ImageView imgNews;
	private ImageButton btnAddCategory;
	
	/* Declaración de la lista de noticias y loader */
	private ListView newsList;
	private ProgressBar mainLoader;	
	
	/* Declaración de todos los controles y objetos que hacen el footer de la UI */
	private RelativeLayout newsFooterLayout;
	private ToggleButton btnPlayPause;
	private Button btnNextNews;
	private TextView txtCurrentNewsTitle;
	private ProgressBar progressbar;
	private ProgressBar audioLoader;
	
	/* Declaraciones de variables, arrays, etc */
	private ArrayList<HashMap<String, String>> data;
	private LinguooNewsCustomAdapter listAdapter;
	private Boolean controlsReady = false;
	private ImageLoader imageLoader;
	private Boolean btnAutoPlayEnabled = false;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/* Si getTag es null, el source corresponde al botón "add/remove" */
		if(v.getTag() == null)addRemoveNews(v);
		else toPlayNews(v);
	}
	
	/********************************************************************************************************/
	
	public LinguooUIManager(Activity a){
		Log.d(TAG,"Creando UI...");
		activity = a;
		uiInterface = (LinguooUIManagerInterface)a;
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}	

	public void showMainView(){
		mainLayout = R.layout.news_layout;
		itemLayout = R.layout.news_item_layout;
		activity.setContentView(mainLayout);
		defineComponents();
		setListeners();
	}

	public void hideLoader(){
		mainLoader.setVisibility(ProgressBar.INVISIBLE);
	}
	
	public void showLoader(){
		mainLoader.setVisibility(ProgressBar.VISIBLE);
	}
	
	public void showAudioLoader(){
		btnPlayPause.setVisibility(ToggleButton.INVISIBLE);
		audioLoader.setVisibility(ProgressBar.VISIBLE);		
	}
	
	public void hideAudioLoader(){
		audioLoader.setVisibility(ProgressBar.INVISIBLE);
		btnPlayPause.setVisibility(ToggleButton.VISIBLE);
	}
	
	public void setPlaying(Boolean playing){
		if(playing){
			btnPlayPause.setBackgroundResource(R.drawable.btn_pause_on);
			btnPlayPause.setChecked(true);
		}else{
			btnPlayPause.setBackgroundResource(R.drawable.btn_play_on);
			btnPlayPause.setChecked(false);
		}
	}
	
	public void setNewsTitleAndImage(String title, String image){
		txtCurrentNewsTitle.setText(title);
		
		if(image != null)imageLoader.DisplayImage(image, imgNews);
		else imgNews.setImageResource(R.drawable.logo_sample);
	}
	
	public void updateProgressBar(int value){
		progressbar.setProgress(value);
	}
	
	public void disablePlayerControls(){
		btnPlayPause.setEnabled(false);
		btnPlayPause.setClickable(false);
		btnPlayPause.setBackgroundResource(R.drawable.btn_play_off);
		btnNextNews.setEnabled(false);
		btnNextNews.setClickable(false);
		btnNextNews.setBackgroundResource(R.drawable.btn_ff_off);
		progressbar.setEnabled(false);
		progressbar.setClickable(false);
		progressbar.setProgress(0);
		txtCurrentNewsTitle.setEnabled(false);
		txtCurrentNewsTitle.setText("");
		audioLoader.setVisibility(ProgressBar.INVISIBLE);
		controlsReady = false;
	}
	
	public void enablePlayerControls(){
		if(!controlsReady){
			btnPlayPause.setEnabled(true);
			btnPlayPause.setClickable(true);
			btnPlayPause.setBackgroundResource(R.drawable.btn_play_on);
			btnNextNews.setEnabled(true);
			btnNextNews.setClickable(true);
			btnNextNews.setBackgroundResource(R.drawable.btn_ff_on);
			progressbar.setEnabled(true);
			progressbar.setClickable(true);
			progressbar.setProgress(0);
			txtCurrentNewsTitle.setEnabled(true);
			controlsReady = true;
		}
	}

	private void defineComponents(){
		newsFooterLayout = (RelativeLayout) activity.findViewById(R.id.newsFooterLayout);
		newsImageHeaderLayout = (FrameLayout) activity.findViewById(R.id.newsImageHeaderLayout);
				
		btnConfig = (ImageButton) activity.findViewById(R.id.btnConfig);
		btnAutoPlay = (ImageButton) activity.findViewById(R.id.btnAutoPlay);
		btnUser = (ImageButton) activity.findViewById(R.id.btnUser);
		
		imgNews = (ImageView) activity.findViewById(R.id.imgNews);
		btnAddCategory = (ImageButton) activity.findViewById(R.id.btnAddCategory);
		
		newsList = (ListView) activity.findViewById(R.id.newsList);
		mainLoader = (ProgressBar) activity.findViewById(R.id.mainLoader);
		
		btnPlayPause = (ToggleButton) activity.findViewById(R.id.btnPlayPause);
		audioLoader = (ProgressBar) activity.findViewById(R.id.audioLoader);
		btnNextNews = (Button) activity.findViewById(R.id.btnFF);
		progressbar = (ProgressBar) activity.findViewById(R.id.progressbar);
		txtCurrentNewsTitle = (TextView) activity.findViewById(R.id.txtCurrentNewsTitle);
		
		disablePlayerControls();		
		sendToHandler(LinguooUIManagerInterface.UI_SHOW_MAIN_VIEW, 0);
	}
	
	public void showList(String dataSource){
		LinguooNewsManager.setData(dataSource);
		data = LinguooNewsManager.getDataAsArrayList();
		listAdapter = new LinguooNewsCustomAdapter(activity, itemLayout, data, this);
		newsList.setAdapter(listAdapter);
	}

	private void addRemoveNews(View v){
		View view = (View) v.getParent();
		ToggleButton btnAdd = (ToggleButton)v;
		itemHolder holder = (itemHolder)view.getTag();
		Boolean status;
		int resource;
		int event;
		
		if(btnAdd.isChecked()){
			status = true;
			resource = R.drawable.btn_add_on;
			event = LinguooUIManagerInterface.UI_ADD_TO_PLAY_LIST;
		}else{
			status = false;
			resource = R.drawable.btn_add_off;
			event = LinguooUIManagerInterface.UI_REMOVE_FROM_PLAY_LIST;
		}
		
		btnAdd.setBackgroundResource(resource);
		updateDataAndAdapter(holder.getItemPosition(), status);
		sendToHandler(event, holder.getItemPosition());
		
	}
	
	private void toPlayNews(View v){
		itemHolder holder = (itemHolder)v.getTag();
		sendToHandler(LinguooUIManagerInterface.UI_ITEM_SELECTED, holder.getItemPosition());
	}
	
	private void sendToHandler(int action, int value){
		uiInterface.UIStatusHandler(action, value);
	}
	
	private void updateDataAndAdapter(int index, boolean status){
		LinguooNewsManager.setNewsOnPlayListByIndex(index, status);
		listAdapter.notifyDataSetChanged();
	}

	private void setListeners(){
		btnConfig.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						view.setBackgroundResource(R.color.gray);
						break;
					case MotionEvent.ACTION_UP:
						sendToHandler(LinguooUIManagerInterface.UI_CONFIG, 0);
						view.setBackgroundResource(R.color.white);
						break;
				}
				return false;
			}
		
		});
		
		btnAutoPlay.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						if(!btnAutoPlayEnabled){
							btnAutoPlay.setImageResource(R.drawable.btn_auto_on);
							sendToHandler(LinguooUIManagerInterface.UI_AUTO_PLAY_ON, 0);
							btnAutoPlayEnabled = true;
						}else{
							btnAutoPlay.setImageResource(R.drawable.btn_auto_off);
							sendToHandler(LinguooUIManagerInterface.UI_AUTO_PLAY_OFF, 0);
							btnAutoPlayEnabled = false;
						}
						view.setBackgroundResource(R.color.gray);
						break;
					case MotionEvent.ACTION_UP:
						view.setBackgroundResource(R.color.white);
						break;
				}
				return false;
			}
		
		});
		
		btnUser.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						view.setBackgroundResource(R.color.gray);
						break;
					case MotionEvent.ACTION_UP:
						sendToHandler(LinguooUIManagerInterface.UI_USER, 0);
						view.setBackgroundResource(R.color.white);
						break;
				}
				return false;
			}
		
		});
		
		btnAddCategory.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						view.setBackgroundResource(R.drawable.btn_add_off);
						break;
					case MotionEvent.ACTION_UP:
						sendToHandler(LinguooUIManagerInterface.UI_ADD_CATEGORY, 0);
						view.setBackgroundResource(R.drawable.btn_add_on);
						break;
				}
				return false;
			}
		
		});
		
		btnPlayPause.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				ToggleButton tbtn = (ToggleButton)view;
				
				if(tbtn.isChecked()){
					tbtn.setBackgroundResource(R.drawable.btn_pause_on);
					sendToHandler(LinguooUIManagerInterface.UI_PLAY,0);
				}else{
					tbtn.setBackgroundResource(R.drawable.btn_play_on);
					sendToHandler(LinguooUIManagerInterface.UI_PAUSE,0);
				}
				
			}
			
		});
		
		btnNextNews.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				sendToHandler(LinguooUIManagerInterface.UI_MOVE_FORWARD,0);
			}
			
		});

	}
	
}
