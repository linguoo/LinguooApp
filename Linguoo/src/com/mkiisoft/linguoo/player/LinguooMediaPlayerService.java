package com.mkiisoft.linguoo.player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

import com.mkiisoft.linguoo.util.Constants;

public class LinguooMediaPlayerService extends Service{
	protected static final String TAG = "Linguoo MediaPlayer Service";
	
	private MediaPlayer mediaPlayer;
	private Boolean mediaPlayerStarted = false;
	private ArrayList<String> playList;
	private ArrayList<String> titleList;
	private ArrayList<String> imageList;
	private String currentURL;
	private String currentTitle;
	private String currentImage;
	private Intent playbackintent;
	private int index;
	private Boolean autoPlay = false;
	
	private IntentFilter mpReceiverFilter;
	
	@Override
	public void onCreate() {
		Log.d(TAG,"Service Created...");
		registerMediaReceiver();
	}
	
	@Override
	public void onDestroy() {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	    unregisterMediaReceiver();
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG,"BINDING...");
		return null;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
		Log.d(TAG,"Service Started...");
		return START_STICKY;
    }  
	
	/********************************************************************************************************/
	
	private void prepareMediaPlayer(String url, String title, String image){
		releaseMediaPlayer();
		
		if(url.equals("")){
			sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_ERROR);
		}else{		
			if(mediaPlayer == null){
				mediaPlayer = new MediaPlayer();
				setListeners();
			}		
			
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			
			try {
				mediaPlayer.setDataSource(url);
				mediaPlayer.prepareAsync();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				stopSelf();
				e.printStackTrace();
			}
			sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_LOADING);
			sendPlaybackTitle(title,image);
		}
		sendProgressStatus(0);
	}
	
	private void moveNext(){
		Log.d(TAG, "AUTO PLAY: " + autoPlay);
		if(autoPlay && (playList != null && playList.size() > 0)){
			index++;
			
			if((index >= playList.size()) || (playList.size() == 1))index = 0;
			
			currentURL = playList.get(index);
			currentTitle = titleList.get(index);
			currentImage = imageList.get(index);
			prepareMediaPlayer(currentURL, currentTitle, currentImage);
		}else if(autoPlay){
			prepareMediaPlayer(currentURL, currentTitle, currentImage);
		}
	}
	
	private void releaseMediaPlayer(){
		if (mediaPlayer != null) {
			if(mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
			mediaPlayerStarted = false;
		}
		sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_RELEASE);
	}
	
	private void pauseMediaPlayer(){
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
		sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_PAUSE);
	}
	
	private void resumeMediaPlayer(){
		if (mediaPlayer != null) {
			if(!mediaPlayer.isPlaying())startProgressbarUpdater();
			mediaPlayer.start();
		}
		sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_RESUME);
	}
	
	private void setAutoPlay(Boolean value){
		Log.d(TAG,"AutoPlay is: " + value); 
		autoPlay = value;
	}
	
	private void registerMediaReceiver(){
		playbackintent = new Intent();
		
		if(mpReceiverFilter == null){
			mpReceiverFilter = new IntentFilter();
			mpReceiverFilter.addAction("updatePlayList");
			mpReceiverFilter.addAction("updateTitleList");
			mpReceiverFilter.addAction("updateImageList");
			mpReceiverFilter.addAction("playThisAudio");
			mpReceiverFilter.addAction("pauseThisAudio");
			mpReceiverFilter.addAction("resumeThisAudio");
			mpReceiverFilter.addAction("setAutoPlay");
	
			registerReceiver(mpReceiver, mpReceiverFilter);
		}
	}
	
	private void unregisterMediaReceiver(){
		unregisterReceiver(mpReceiver);
	}
	
	private void sendPlaybackStatus(int status){
		playbackintent.setAction("playbackStatus");         
		playbackintent.putExtra("playback", status);         
        sendBroadcast(playbackintent);
	}
	
	private void sendProgressStatus(int value){
		playbackintent.setAction("progressStatus");         
		playbackintent.putExtra("progress", value);         
        sendBroadcast(playbackintent);
	}
	
	private void sendPlaybackTitle(String title, String image){
		playbackintent.setAction("playbackTitle");         
		playbackintent.putExtra("title", title);
		playbackintent.putExtra("image", image);  
        sendBroadcast(playbackintent);
	}
	
	private void startProgressbarUpdater(){
		MediaObserver observer = new MediaObserver();
		Thread playingThread = new Thread(observer);
		mediaPlayerStarted = true;
		playingThread.start();
		
	}
	
	private void broadcastHandler(Intent intent){
		String action = intent.getAction();
		//Object[] keys = intent.getExtras().keySet().toArray();
		if(action.equals("updatePlayList")){
			playList = intent.getStringArrayListExtra("playList");
		}else if(action.equals("updateTitleList")){
			titleList = intent.getStringArrayListExtra("titleList");
		}else if(action.equals("updateTitleList")){
			imageList = intent.getStringArrayListExtra("imageList");
		}else if(action.equals("playThisAudio")){
			currentURL = intent.getStringArrayListExtra("play").get(1);
			currentTitle = intent.getStringArrayListExtra("play").get(2);
			currentImage = intent.getStringArrayListExtra("play").get(3);
			prepareMediaPlayer(currentURL, currentTitle, currentImage);
		}else if(action.equals("pauseThisAudio")){
			pauseMediaPlayer();
		}else if(action.equals("resumeThisAudio")){
			resumeMediaPlayer();
		}else if(action.equals("setAutoPlay")){
			setAutoPlay(Boolean.parseBoolean(intent.getStringExtra("autoPlay")));
		}
		
		
	}
	
    private BroadcastReceiver mpReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent bufferIntent) {
			// TODO Auto-generated method stub
			broadcastHandler(bufferIntent);
		}
	};
    
	/******************************************************************************************************/
	
	private void setListeners(){
				
		mediaPlayer.setOnPreparedListener(new OnPreparedListener(){

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.start();
				mediaPlayerStarted = true;
				startProgressbarUpdater();
				sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_PLAYING);
			}
			
		});
		
		mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mediaPlayerStarted = false;
				sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_COMPLETE);
				sendProgressStatus(0);
				//moveNext();
			}
			
		});
		
		mediaPlayer.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int arg1, int arg2) {
				// TODO Auto-generated method stub
				mediaPlayerStarted = false;
				sendPlaybackStatus(LinguooMediaPlayerInterface.ON_PLAYER_ERROR);
				return false;
			}
			
		});
	}
	
	/******************************************************************************************************/
	
	class MediaObserver implements Runnable{
		private AtomicBoolean stop = new AtomicBoolean(false);
		private Integer counter = 0; 
		
		public void stop() {
		    stop.set(true);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!stop.get() && mediaPlayerStarted){
				if(mediaPlayer.isPlaying()){
					counter = (mediaPlayer.getCurrentPosition() * 100) / mediaPlayer.getDuration();
					sendProgressStatus(counter);
					try {
						Thread.sleep(200);
					}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				}else if(!mediaPlayerStarted){
					this.stop();
				}
			}
			//sendProgressStatus(100);
		}
	}
}
