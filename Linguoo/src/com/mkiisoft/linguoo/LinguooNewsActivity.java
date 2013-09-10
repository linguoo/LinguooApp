package com.mkiisoft.linguoo;

import com.mkiisoft.linguoo.async.AsyncConnection;
import com.mkiisoft.linguoo.async.ConnectionListener;
import com.mkiisoft.linguoo.player.LinguooMediaPlayer;
import com.mkiisoft.linguoo.player.LinguooMediaPlayerInterface;
import com.mkiisoft.linguoo.player.LinguooMediaPlayerService;
import com.mkiisoft.linguoo.player.LinguooUIManager;
import com.mkiisoft.linguoo.player.LinguooUIManagerInterface;
import com.mkiisoft.linguoo.util.Constants;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;


public class LinguooNewsActivity extends Activity implements ConnectionListener, LinguooUIManagerInterface, LinguooMediaPlayerInterface{
	protected static final String TAG = "Linguoo Noticias";
	private LinguooUIManager uiManager;
	private LinguooMediaPlayer mediaPlayer;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUI();
        setMainView();
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	@Override
	protected void onPause() { 
		mediaPlayer.unregisterPlaybackReceiver();
	    super.onPause();
	}
	     

	@Override
	protected void onResume() {
		super.onResume();
		mediaPlayer.registerPlaybackReceiver();
	}
	
	@Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

	@Override
	public void ready(int msg, String message) {
		// TODO Auto-generated method stub
		uiManager.hideLoader();
		switch(msg){
			case Constants.NEWS:
				String d = "[{\"LnCod\":21,\"LnAud\":\"C:\\sites\\web\\PublicTempStorage\\multimedia\\primavera_5454be3039c44ebab857071ffc69f136.mp3\",\"LnAud_GXI\":\"http://ec2-54-232-205-219.sa-east-1.compute.amazonaws.com/linguoo/PublicTempStorage/multimedia/primavera_5454be3039c44ebab857071ffc69f136.mp3\",\"Lntitle\":\"Caer a 90 Km por hora desde 27 metros: la nueva locura de la natación\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://www.infobae.com/adjuntos/jpg/2013/07/291x0_687266.jpg\",\"LnLoc\":\"Toti Pasman \",\"LnFec\":\"2013-07-29T16:40:00\",\"LnContent\":\"La disciplina Saltos de Altura debutó en la competencia máxima del deporte que s...\",\"CatLN\":\"2\",\"FeedsLN\":2,\"FeedsUrlLN\":\"http://www.infobae.com\"},{\"LnCod\":22,\"LnAud\":\"C:\\sites\\web\\PublicTempStorage\\multimedia\\entretenimiento-noti_dd0648e70f0b407eb2bbc0e098dfe2ff.mp3\",\"LnAud_GXI\":\"http://ec2-54-232-205-219.sa-east-1.compute.amazonaws.com/linguoo/PublicTempStorage/multimedia/entretenimiento-noti_dd0648e70f0b407eb2bbc0e098dfe2ff.mp3\",\"Lntitle\":\"Los vendedores ambulantes volvieron al Centro de Córdoba\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticft.lavozdelinterior.com.ar/files/imagecache/lvi_nota_652_366/nota_periodistica/ambulante_0.jpg\",\"LnLoc\":\"Angel Suarez \",\"LnFec\":\"2013-07-29T19:20:00\",\"LnContent\":\"vamos linguoo\",\"CatLN\":\"2\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":25,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Mejorar en conjunto\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://www.ole.com.ar/futbol-internacional/espana/Tata-prepara-duelo-frente-Valencia_OLEIMA20130831_0061_5.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-07-29T19:20:00\",\"LnContent\":\"Martino\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"}]";
				uiManager.showList(d);			
			case 500:
				
				break;
		}
	}

	@Override
	public void cacheReady(int msg, String message) {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public void playerStatusHandler(int status) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Player Status: " + status);
		switch(status){
			case ON_PLAYER_ERROR:
				uiManager.hideAudioLoader();
				uiManager.setPlaying(false);
				uiManager.setNewsTitleAndImage("No se puede reproducir la noticia seleccionada.",null);
				break;
			case ON_PLAYER_LOADING:
				uiManager.showAudioLoader();
				break;
			case ON_PLAYER_PLAYING:
				uiManager.hideAudioLoader();
				uiManager.enablePlayerControls();
				uiManager.setPlaying(true);
				break;
			case ON_PLAYER_COMPLETE:
				uiManager.setPlaying(false);
				break;
			case ON_PLAYER_PAUSE:
				break;
			
		}
	}	

	@Override
	public void playerProgressHandler(int value) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Progress Status: " + value);
		uiManager.updateProgressBar(value);
	}
	
	@Override
	public void playerTitleHandler(String title, String image) {
		// TODO Auto-generated method stub
		uiManager.setNewsTitleAndImage(title, image);
	}
	
	@Override
	public void UIStatusHandler(int status, int value) {
		// TODO Auto-generated method stub
		switch(status){
			case UI_SHOW_MAIN_VIEW:
				AsyncConnection.getInstance(Constants.WSGETNEWS, LinguooNewsActivity.this, Constants.NEWS).execute();
				break;
			case UI_ADD_TO_PLAY_LIST:
				mediaPlayer.addToPlayList(value);
				if(mediaPlayer.getTotal() > 0)uiManager.enablePlayerControls();
				break;
			case UI_REMOVE_FROM_PLAY_LIST:
				mediaPlayer.removeFromPlayList(value);
				if(mediaPlayer.getTotal() == 0 && !mediaPlayer.isPlaying())uiManager.disablePlayerControls();
				break;
			case UI_ITEM_SELECTED:
				mediaPlayer.play(value);
				break;
			case UI_CONFIG:
				Log.d(TAG,"CONFIG");
				break;
			case UI_AUTO_PLAY_ON:
				Log.d(TAG,"AUTO PLAY ON ");
				mediaPlayer.setAutoPlay(true);
				break;
			case UI_AUTO_PLAY_OFF:
				Log.d(TAG,"AUTO PLAY OFF ");
				mediaPlayer.setAutoPlay(false);
				break;
			case UI_USER:
				Log.d(TAG,"USER");
				break;
			case UI_ADD_CATEGORY:
				Log.d(TAG,"ADD CATEGORY");
				break;
			case UI_PLAY:
				Log.d(TAG,"PLAY");
				mediaPlayer.play(-1);
				break;
			case UI_PAUSE:
				Log.d(TAG,"PAUSE");
				mediaPlayer.pause();
				break;
			case UI_MOVE_FORWARD:
				Log.d(TAG,"MOVE FORWARD");
				mediaPlayer.moveForward();
				break;
		}
	}
	
	/********************************************************************************************************/
	
	private void createUI(){
		uiManager = new LinguooUIManager(LinguooNewsActivity.this);
		mediaPlayer = new LinguooMediaPlayer(LinguooNewsActivity.this);
		mediaPlayer.registerPlaybackReceiver();
		mediaPlayer.startMediaPlayerService();
	}
	
	private void setMainView(){
		uiManager.showMainView();
	}

}