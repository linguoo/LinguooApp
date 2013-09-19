package com.linguoo.linguooapp;

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
import android.widget.Toast;

import com.linguoo.linguooapp.async.AsyncConnection;
import com.linguoo.linguooapp.async.ConnectionListener;
import com.linguoo.linguooapp.player.LinguooMediaPlayer;
import com.linguoo.linguooapp.player.LinguooMediaPlayerInterface;
import com.linguoo.linguooapp.player.LinguooUIManager;
import com.linguoo.linguooapp.player.LinguooUIManagerInterface;
import com.linguoo.linguooapp.util.Constants;
import com.linguoo.linguooapp.util.KeySaver;


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
		mediaPlayer.updatePlayerView();
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
				KeySaver.saveShare(LinguooNewsActivity.this, "newsdata", message);
				KeySaver.saveShare(this, "noChangesCategory", true);
				prepareData(message);							
				break;
			case AsyncConnection.ERROR:
			case AsyncConnection.NOCONNECTION:
				Toast.makeText(this,"No hay una conexión a internet activa.", Toast.LENGTH_SHORT).show();				
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
		uiManager.updateProgressBar(value);
	}
	
	@Override
	public void playerTitleHandler(String title, String image) {
		// TODO Auto-generated method stub
		uiManager.setNewsTitleAndImage(title, image);
	}
	
	@Override
	public void playerStatusHandler(int status) {
		Log.d(TAG,"Status: "  + status);
		// TODO Auto-generated method stub
		uiManager.hideAudioLoader();
		switch(status){
			case ON_PLAYER_STARTED:
				uiManager.disablePlayButton();
				uiManager.setNewsTitleAndImage(null,null);
				break;
			case ON_INVALID_URL:
			case ON_PLAYER_ERROR:
				uiManager.disablePlayButton();
				uiManager.setNewsTitleAndImage("No es posible reproducir la noticia seleccionada.",null);
				break;
			case ON_PLAYER_LOADING:
				uiManager.showAudioLoader();
				uiManager.enablePlayButton();
				break;
			case ON_PLAYER_PLAYING:
				uiManager.setPlaying(true);
				break;
			case ON_PLAYER_COMPLETE:
				uiManager.setPlaying(false);
				break;
			case ON_PLAYER_PAUSE:
				uiManager.setPlaying(false);
				break;
			case ON_PLAYER_RESUME:
				uiManager.setPlaying(true);
				break;
			case ON_PLAYER_MOVEFORWARD:
				break;
			case ON_PLAYER_RELEASE:
				break;				
			case ON_PLAYER_RESET:
				break;
			case ON_CALL_ATTACHED:
				uiManager.setPlaying(false);
				uiManager.removeClickPlayButton();
				uiManager.removeClickForwardButton();
				break;
			case ON_CALL_RELEASED:
				uiManager.setPlaying(true);
				uiManager.addClickPlayButton();
				uiManager.addClickForwardButton();
				break;
		}
	}	
	
	@Override
	public void UIStatusHandler(int status, int value) {
		// TODO Auto-generated method stub		
		switch(status){
			case UI_SHOW_MAIN_VIEW:
				if(!KeySaver.getBoolSavedShare(LinguooNewsActivity.this, "noChangesCategory")){
					sendNewsRequest();
				}else{
					prepareData(null);
				}
				if(KeySaver.getBoolSavedShare(LinguooNewsActivity.this, "autoplay")){
					uiManager.setAutoplayButtonAsON();
					mediaPlayer.setAutoPlay(true);
				}else{
					uiManager.setAutoplayButtonAsOFF();
					mediaPlayer.setAutoPlay(false);
				}
				mediaPlayer.updatePlayerView();
				break;
			case UI_ADD_TO_PLAY_LIST:
				mediaPlayer.addToPlayList(value);
				KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", mediaPlayer.getSelectedIndexes());
				uiManager.enableForwardButton();
				break;
			case UI_REMOVE_FROM_PLAY_LIST:
				mediaPlayer.removeFromPlayList(value);
				KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", mediaPlayer.getSelectedIndexes());
				if(mediaPlayer.getTotal() == 0)uiManager.disableForwardButton();
				break;
			case UI_ITEM_SELECTED:
				if(mediaPlayer.isCallAttached()){
					Toast.makeText(this,"Tienes una llamada en curso. Intenta Luego.",Toast.LENGTH_LONG).show();
				}else{
					mediaPlayer.play(value);
				}				
				break;
			case UI_CONFIG:
				break;
			case UI_AUTO_PLAY_ON:
				KeySaver.saveShare(LinguooNewsActivity.this, "autoplay", true);
				mediaPlayer.setAutoPlay(true);
				break;
			case UI_AUTO_PLAY_OFF:
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
					clearKeySavers();
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
							clearKeySavers();
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
				if(mediaPlayer.isCallAttached()){
					Toast.makeText(this,"Tienes una llamada en curso. Intenta Luego.",Toast.LENGTH_LONG).show();
				}else{
					mediaPlayer.play(-1);
				}
				break;
			case UI_PAUSE:
				if(mediaPlayer.isCallAttached()){
					Toast.makeText(this,"Tienes una llamada en curso. Intenta Luego.",Toast.LENGTH_LONG).show();
				}else{
					mediaPlayer.pause();
				}
				
				break;
			case UI_MOVE_FORWARD:
				mediaPlayer.moveForward();
				break;
		}
	}
	
	/********************************************************************************************************/	
	
	private void sendNewsRequest(){
		uiManager.showLoader();
		AsyncConnection.getInstance(Constants.WSGETNEWS + usuLog, LinguooNewsActivity.this, Constants.NEWS).execute();
	}
	
	private void prepareData(String receivedData){
		String data = (receivedData != null && !receivedData.equals("")) ? receivedData : KeySaver.getStringSavedShare(LinguooNewsActivity.this, "newsdata");
		String selectedItems = KeySaver.getStringSavedShare(LinguooNewsActivity.this, "selectedItems");
		uiManager.showList(data, selectedItems);
		
		if(selectedItems != null && !selectedItems.equals("")){
			uiManager.enableForwardButton();
			mediaPlayer.addToPlayList(selectedItems);
		}
	}
	
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
	
	private void clearKeySavers(){
		KeySaver.saveShare(LinguooNewsActivity.this, "UsuLog", "");
		KeySaver.saveShare(LinguooNewsActivity.this, "UsuCod", "");
		KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", "");
		KeySaver.saveShare(LinguooNewsActivity.this, "newsdata", "");
		KeySaver.saveShare(LinguooNewsActivity.this, "autoplay", false);
		KeySaver.saveShare(LinguooNewsActivity.this, "noChangesCategory", false);
		KeySaver.saveShare(LinguooNewsActivity.this, "state", Constants.MAIN);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.CATEG) {			
			if(resultCode == Constants.CATCHG){      
		         /*las categorías han cambiado, detener el reproductor, limpiar el playlist
		    	 * y volver a listar las noticias
		    	 */
				KeySaver.saveShare(LinguooNewsActivity.this, "noChangesCategory", false);
				KeySaver.saveShare(LinguooNewsActivity.this, "selectedItems", "");
				mediaPlayer.restartPlayer();
				uiManager.refreshLayout();				
				sendNewsRequest();
		    }
		    if (resultCode == Constants.CATUCHG) {    
		         /*
		          * No se modificó ninguna categoría, todo sigue igual
		          * Borrar si no es necesario ejecutar ninguna acción
		          */
		    	KeySaver.saveShare(LinguooNewsActivity.this, "noChangesCategory", true);
		    	prepareData(null);
		    }		    
		    mediaPlayer.updatePlayerView();
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

}