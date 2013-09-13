package com.mkiisoft.linguoo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.mkiisoft.linguoo.async.AsyncConnection;
import com.mkiisoft.linguoo.async.ConnectionListener;
import com.mkiisoft.linguoo.player.LinguooMediaPlayer;
import com.mkiisoft.linguoo.player.LinguooMediaPlayerInterface;
import com.mkiisoft.linguoo.player.LinguooUIManager;
import com.mkiisoft.linguoo.player.LinguooUIManagerInterface;
import com.mkiisoft.linguoo.util.Constants;
import com.mkiisoft.linguoo.util.KeySaver;


public class LinguooNewsActivity extends Activity implements ConnectionListener, LinguooUIManagerInterface, LinguooMediaPlayerInterface{
	protected static final String TAG = "Linguoo Noticias";
	private static final String usuLogTry = "2F5B3F3CB0"; //Token del usuario de prueba
	private static boolean newsActivityIsOpen;
	private LinguooUIManager uiManager;
	private LinguooMediaPlayer mediaPlayer;
	private String usuLog; //Token del usuario
	private String usuCod; //Nombre de usuario
	private Boolean isDemoUser = false;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerKeys();
        setState();
        setAsDemo();
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
		if (usuLog.equals("")==true){
			KeySaver.saveShare(LinguooNewsActivity.this, "state", Constants.MAIN);
			Intent i= new Intent(this,MainActivity.class);
			//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}else{
			setNewsActivityIsOpen(true);
			mediaPlayer.registerPlaybackReceiver();
		}
	}
	
	@Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        setNewsActivityIsOpen(false);
        setState();
    }

	@Override
	public void ready(int msg, String message) {
		// TODO Auto-generated method stub
		uiManager.hideLoader();
		switch(msg){
			case Constants.NEWS:
				String d = "[{\"LnCod\":21,\"LnAud\":\"C:\\sites\\web\\PublicTempStorage\\multimedia\\primavera_5454be3039c44ebab857071ffc69f136.mp3\",\"LnAud_GXI\":\"http://ec2-54-232-205-219.sa-east-1.compute.amazonaws.com/linguoo/PublicTempStorage/multimedia/primavera_5454be3039c44ebab857071ffc69f136.mp3\",\"Lntitle\":\"Caer a 90 Km por hora desde 27 metros: la nueva locura de la natación\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://www.infobae.com/adjuntos/jpg/2013/07/291x0_687266.jpg\",\"LnLoc\":\"Toti Pasman \",\"LnFec\":\"2013-07-29T16:40:00\",\"LnContent\":\"La disciplina Saltos de Altura debutó en la competencia máxima del deporte que s...\",\"CatLN\":\"2\",\"FeedsLN\":2,\"FeedsUrlLN\":\"http://www.infobae.com\"},{\"LnCod\":22,\"LnAud\":\"C:\\sites\\web\\PublicTempStorage\\multimedia\\entretenimiento-noti_dd0648e70f0b407eb2bbc0e098dfe2ff.mp3\",\"LnAud_GXI\":\"http://ec2-54-232-205-219.sa-east-1.compute.amazonaws.com/linguoo/PublicTempStorage/multimedia/entretenimiento-noti_dd0648e70f0b407eb2bbc0e098dfe2ff.mp3\",\"Lntitle\":\"Los vendedores ambulantes volvieron al Centro de Córdoba\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticft.lavozdelinterior.com.ar/files/imagecache/lvi_nota_652_366/nota_periodistica/ambulante_0.jpg\",\"LnLoc\":\"Angel Suarez \",\"LnFec\":\"2013-07-29T19:20:00\",\"LnContent\":\"vamos linguoo\",\"CatLN\":\"2\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":25,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Mejorar en conjunto\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://www.ole.com.ar/futbol-internacional/espana/Tata-prepara-duelo-frente-Valencia_OLEIMA20130831_0061_5.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-07-29T19:20:00\",\"LnContent\":\"Martino\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"},{\"LnCod\":26,\"LnAud\":\"\",\"LnAud_GXI\":\"\",\"Lntitle\":\"Snowden no quiso reunirse con diplomáticos de Estados Unidos\",\"LnImage\":\"\",\"LnImage_GXI\":\"http://staticf5a.lavozdelinterior.com.ar/sites/default/files/styles/landscape_642_366/public/nota_periodistica/Snowden.jpg\",\"LnLoc\":\" \",\"LnFec\":\"2013-08-31T11:32:00\",\"LnContent\":\"El exanalista de la CIA no quiso reunirse con diplomáticos de su país que buscar...\",\"CatLN\":\"1\",\"FeedsLN\":1,\"FeedsUrlLN\":\"http://rss.feedsportal.com/c/32838/f/533815/index.rss\"}]";
				String newsData = KeySaver.getStringSavedShare(LinguooNewsActivity.this, "newsdata");
				
				if(message.equals(newsData)){
					String selectedItems = KeySaver.getStringSavedShare(LinguooNewsActivity.this, "selectedItems");
					uiManager.showList(message, selectedItems);
					if(selectedItems != null){
						uiManager.enableForwardButton();
						mediaPlayer.addToPlayList(selectedItems);
					}
				}else{
					KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", "");
					uiManager.showList(message, null);	
				}				
				KeySaver.saveShare(LinguooNewsActivity.this, "newsdata", message);		
				break;
			case AsyncConnection.ERROR:
			case AsyncConnection.NOCONNECTION:
				showAlertDialog(LinguooNewsActivity.this,TAG,"No existe una conexión a Internet activa.");
				break;
		}
	}

	@Override
	public void cacheReady(int msg, String message) {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public void playerProgressHandler(int value) {
		// TODO Auto-generated method stub
		//Log.d(TAG,"Progress Status: " + value);
		uiManager.updateProgressBar(value);
	}
	
	@Override
	public void playerTitleHandler(String title, String image) {
		// TODO Auto-generated method stub
		uiManager.setNewsTitleAndImage(title, image);
	}
	
	@Override
	public void playerStatusHandler(int status) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Player Status: " + status);
		switch(status){
			case ON_PLAYER_STARTED:
				uiManager.hideAudioLoader();
				uiManager.disablePlayButton();
				uiManager.setNewsTitleAndImage("",null);
				break;
			case ON_INVALID_URL:
			case ON_PLAYER_ERROR:
				uiManager.hideAudioLoader();
				uiManager.disablePlayButton();
				uiManager.setNewsTitleAndImage("No es posible reproducir la noticia seleccionada.",null);
				break;
			case ON_PLAYER_LOADING:
				uiManager.showAudioLoader();
				uiManager.enablePlayButton();
				break;
			case ON_PLAYER_PLAYING:
				uiManager.hideAudioLoader();
				uiManager.setPlaying(true);
				break;
			case ON_PLAYER_COMPLETE:
				uiManager.setPlaying(false);
				break;
			case ON_PLAYER_PAUSE:
				uiManager.setPlaying(false);
				break;
			case ON_PLAYER_MOVEFORWARD:
				//mediaPlayer.moveForward();
				break;
		}
	}	
	
	@Override
	public void UIStatusHandler(int status, int value) {
		// TODO Auto-generated method stub
		switch(status){
			case UI_SHOW_MAIN_VIEW:
				AsyncConnection.getInstance(Constants.WSGETNEWS + usuLog, LinguooNewsActivity.this, Constants.NEWS).execute();
				mediaPlayer.updatePlayerView();
				if(KeySaver.getBoolSavedShare(LinguooNewsActivity.this, "autoplay")){
					uiManager.setAutoplayButtonAsON();
					mediaPlayer.setAutoPlay(true);
				}else{
					uiManager.setAutoplayButtonAsOFF();
					mediaPlayer.setAutoPlay(false);
				}
				break;
			case UI_ADD_TO_PLAY_LIST:
				mediaPlayer.addToPlayList(value);
				KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", mediaPlayer.getSelectedIndexes());
				if(mediaPlayer.isPlaying()){
					uiManager.enableForwardButton();
				}else{
					uiManager.enableForwardButton();
					uiManager.enablePlayButton();
				}
				break;
			case UI_REMOVE_FROM_PLAY_LIST:
				mediaPlayer.removeFromPlayList(value);
				KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", mediaPlayer.getSelectedIndexes());
				if(mediaPlayer.isPlaying()){
					if(mediaPlayer.getTotal() == 0)uiManager.disableForwardButton();
					uiManager.setAsPaused();
				}else{
					if(mediaPlayer.getTotal() == 0){
						uiManager.disableForwardButton();
						uiManager.disablePlayButton();
					}
				}
				break;
			case UI_ITEM_SELECTED:
				mediaPlayer.play(value);
				break;
			case UI_CONFIG:
				Log.d(TAG,"CONFIG");
				break;
			case UI_AUTO_PLAY_ON:
				Log.d(TAG,"AUTO PLAY ON ");
				KeySaver.saveShare(LinguooNewsActivity.this, "autoplay", true);
				mediaPlayer.setAutoPlay(true);
				break;
			case UI_AUTO_PLAY_OFF:
				Log.d(TAG,"AUTO PLAY OFF ");
				KeySaver.saveShare(LinguooNewsActivity.this, "autoplay", false);
				mediaPlayer.setAutoPlay(false);
				break;
			case UI_USER:
				/*
				 * Si UsuLog=UsuLogTry es el usuario de prueba, 
				 * hacer borrar UsuLog y UsuCod y volver a MainActivity y darle finsh a esta activity
				 * si usuLog != usuLogTry pregunta con un dialogo si quiere cerrar la sesión
				 * si la cierra borrar UsuLog y UsuCod y volver a MainActivity y darle finish a esta
				 * detener el servicio y por ende la reproducción de cualquier audio.
				 * KeySaver.saveShare(this, "UsuLog", "");
				 * KeySaver.saveShare(this, "UsuCod", "");
				 */
				if(usuLog.equals(usuLogTry)){
					KeySaver.saveShare(LinguooNewsActivity.this, "UsuLog", "");
					KeySaver.saveShare(LinguooNewsActivity.this, "UsuCod", "");
					KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", "");
					KeySaver.saveShare(LinguooNewsActivity.this, "newsdata", "");
					KeySaver.saveShare(LinguooNewsActivity.this, "autoplay", false);
					KeySaver.saveShare(LinguooNewsActivity.this, "state", Constants.MAIN);
					mediaPlayer.stopMediaPlayerService();
					Intent i = new Intent(this, MainActivity.class);
					startActivity(i);
					finish();
				}else{
					OnClickListener negativeButton = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					};
					
					OnClickListener positiveButton = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							KeySaver.saveShare(LinguooNewsActivity.this, "UsuLog", "");
							KeySaver.saveShare(LinguooNewsActivity.this, "UsuCod", "");
							KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", "");
							KeySaver.saveShare(LinguooNewsActivity.this, "newsdata", "");
							KeySaver.saveShare(LinguooNewsActivity.this, "autoplay", false);
							KeySaver.saveShare(LinguooNewsActivity.this, "state", Constants.MAIN);
							mediaPlayer.stopMediaPlayerService();
							
							Intent i = new Intent(LinguooNewsActivity.this, MainActivity.class);
							startActivity(i);
							finish();
						}
					};
					showAlertDialogYESNO(LinguooNewsActivity.this,"Linguoo","¿Desea cerrar la sesión?",negativeButton,positiveButton);
				}				
				break;
			case UI_ADD_CATEGORY:
				if(usuLog.equals(usuLogTry)==false){
					//mediaPlayer.stopMediaPlayerService();
					Intent i = new Intent(this, GridActivity.class);
					startActivityForResult(i, Constants.CATEG);
				}
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
	
	private void registerKeys(){
		usuLog = KeySaver.getStringSavedShare(this, "UsuLog");
        usuCod = KeySaver.getStringSavedShare(this, "UsuCod");
        KeySaver.saveShare(this, "state", Constants.NEWS);
        if (KeySaver.getIntSavedShare(this, "firstTime") == Constants.FT_YES){
        	KeySaver.saveShare(this, "firstTime", Constants.FT_NO);
        }
	}
	
	private void setState() {
		if (usuLog.equals(usuLogTry)) KeySaver.saveShare(this, "state", Constants.TESTVER);
        else KeySaver.saveShare(this, "state", Constants.NEWS);
        //Seteo el estado de la aplicación para saber en que condiciones se ingresó a esta activity
	}
	
	private void setAsDemo(){
		if(usuLog.equals(usuLogTry))isDemoUser = true;
		else isDemoUser = false;
	}

	private void createUI(){
		uiManager = new LinguooUIManager(LinguooNewsActivity.this);
		mediaPlayer = new LinguooMediaPlayer(LinguooNewsActivity.this);
		mediaPlayer.registerPlaybackReceiver();
		mediaPlayer.startMediaPlayerService();
		uiManager.setAsDemo(isDemoUser);
	}
	
	private void setMainView(){
		uiManager.showMainView();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "cambiaron categorías");
		if (requestCode == Constants.CATEG) {
			
			if(resultCode == Constants.CATCHG){      
		         /*las categorías han cambiado, detener el reproductor, limpiar el playlist
		    	 * y volver a listar las noticias
		    	 */
		    	 Log.d(TAG, "cambiaron categorías");
		    }
		    if (resultCode == Constants.CATUCHG) {    
		         /*
		          * No se modificó ninguna categoría, todo sigue igual
		          * Borrar si no es necesario ejecutar ninguna acción
		          */
	
		    	Log.d(TAG, "no cambiaron categorías");
		    }
		}
	}

	public static boolean isNewsActivityIsOpen() {
		return newsActivityIsOpen;
	}

	public static void setNewsActivityIsOpen(boolean newsActivityIsOpen) {
		LinguooNewsActivity.newsActivityIsOpen = newsActivityIsOpen;
	}
	
	/********************************************************************************************************/
	
	public void showAlertDialogYESNO(Context context, String title, String message, OnClickListener negativeButton, OnClickListener positiveButton) {
		final SpannableString s = new SpannableString(message);
	    Linkify.addLinks(s, Linkify.ALL);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
		.setIcon(R.drawable.linguoo)
		.setMessage(s)
		.setCancelable(false)
		.setNegativeButton("No",negativeButton)
		.setPositiveButton("Si",positiveButton);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void showAlertDialog(Context context, String title, String message) {
		final SpannableString s = new SpannableString(message);
	    //Linkify.addLinks(s, Linkify.ALL);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
		.setIcon(R.drawable.linguoo)
		.setMessage(s)
		.setCancelable(false)
		.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	

}