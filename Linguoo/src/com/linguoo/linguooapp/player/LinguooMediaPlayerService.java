package com.linguoo.linguooapp.player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.linguoo.linguooapp.LinguooNewsActivity;
import com.linguoo.linguooapp.R;
import com.linguoo.linguooapp.util.ImageLoader;

public class LinguooMediaPlayerService extends Service{
	protected static final String TAG = "Linguoo MediaPlayer Service";
	
	private static final int NOTIFICATION_ID = 42;
	private MediaPlayer mediaPlayer;
	private Boolean mediaPlayerStarted = false;
	private ArrayList<String> playList;
	private ArrayList<String> titleList;
	private ArrayList<String> imageList;
	private String currentURL;
	private String currentTitle;
	private String currentImage;
	private Intent playbackintent;
	private int playbackStatus;
	private int index;
	private Boolean autoPlay = false;
	private Boolean callInCource = false;
	private WifiLock wifiLock;
	private IntentFilter mpReceiverFilter;
	private ProgressbarObserver progressBarObserver;
	private Thread playingThread;
	private TelephonyManager telManager;
	private PhoneStateListener phoneStateListener;
	
	@Override
	public void onCreate() {
		wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "LinguooLock");
		registerMediaReceiver();
	}
	
	@Override
	public void onDestroy() {
	    // TODO Auto-generated method stub
	    super.onDestroy();
	    releaseMediaPlayer();
	    unregisterMediaReceiver();
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
		setExternListeners();
		return Service.START_NOT_STICKY;
    }  
	
	/********************************************************************************************************/
	
	private void prepareMediaPlayer(String url, String title, String image){		
		releaseMediaPlayer();		
				
		if(url.equals("")){
			playbackStatus = LinguooMediaPlayerInterface.ON_INVALID_URL;
			//sendPlaybackStatus(LinguooMediaPlayerInterface.ON_INVALID_URL);
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
						
			sendPlaybackTitle(title,image);
			initNotification(title, image);
			wifiLock.acquire();
			playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_LOADING;
		}
		sendPlaybackStatus(playbackStatus);
		sendProgressStatus(0);		
	}
	
	private void moveForward(){
		if(playList != null){
			index++;
			if(playList.size() == 1)index = 0;
			else if(index >= playList.size())index = 0;
			
			currentURL = playList.get(index);
			currentTitle = titleList.get(index);
			currentImage = imageList.get(index);
		}
		playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_MOVEFORWARD;
		sendPlaybackStatus(playbackStatus);
		prepareMediaPlayer(currentURL, currentTitle, currentImage);
	}
	
	private void releaseMediaPlayer(){
		if (mediaPlayer != null) {
			if(mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			stopProgressbarUpdater();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if(wifiLock.isHeld())wifiLock.release();
		mediaPlayerStarted = false;
		playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_RELEASE;
		cancelNotification();
		sendProgressStatus(0);
		sendPlaybackStatus(playbackStatus);
	}
	
	private void resetMediaPlayer(){
		releaseMediaPlayer();
		playList = null;
		titleList = null;
		imageList = null;
		currentURL = "";
		currentTitle = "";
		currentImage = "";
		index = 0;
		playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_RESET;
		sendPlaybackStatus(playbackStatus);
	}
	
	private void pauseMediaPlayer(){
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			//cancelNotification();
			if(wifiLock.isHeld())wifiLock.release();
		}
		playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_PAUSE;
		sendPlaybackStatus(playbackStatus);
	}
	
	private void pauseOnCallMediaPlayer(){
		if (mediaPlayer != null) {
			mediaPlayer.pause();
			//cancelNotification();
			if(wifiLock.isHeld())wifiLock.release();
		}
		playbackStatus = LinguooMediaPlayerInterface.ON_CALL_ATTACHED;
		sendPlaybackStatus(playbackStatus);
	}
	
	private void resumeMediaPlayer(){
		if (mediaPlayer != null) {
			if(!mediaPlayer.isPlaying())startProgressbarUpdater();
			mediaPlayer.start();
			initNotification(currentTitle, currentImage);
			wifiLock.acquire();
		}
		playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_RESUME;
		sendPlaybackStatus(playbackStatus);
	}
	
	private void resumeOnCallMediaPlayer(){
		if (mediaPlayer != null) {
			if(!mediaPlayer.isPlaying())startProgressbarUpdater();
			mediaPlayer.start();
			initNotification(currentTitle, currentImage);
			wifiLock.acquire();
		}
		playbackStatus = LinguooMediaPlayerInterface.ON_CALL_RELEASED;
		sendPlaybackStatus(playbackStatus);
	}
	
	private void setAutoPlay(Boolean value){
		autoPlay = value;
	}
	
	private int getPosition(){
		if(mediaPlayer != null)return (mediaPlayer.getCurrentPosition() * 100) / mediaPlayer.getDuration();
		else return 0;
	}
	
	private void updatePlayerView(){
		switch(playbackStatus){
			case LinguooMediaPlayerInterface.ON_PLAYER_RESUME: 
			case LinguooMediaPlayerInterface.ON_PLAYER_PLAYING:
			case LinguooMediaPlayerInterface.ON_PLAYER_LOADING:
				sendPlaybackStatus(playbackStatus);
				sendPlaybackTitle(currentTitle,currentImage);
				break;
			case LinguooMediaPlayerInterface.ON_CALL_ATTACHED:
			case LinguooMediaPlayerInterface.ON_CALL_RELEASED:
			case LinguooMediaPlayerInterface.ON_PLAYER_PAUSE:
				sendPlaybackStatus(playbackStatus);
				sendPlaybackTitle(currentTitle,currentImage);
				sendProgressStatus(getPosition());
				break;
			case LinguooMediaPlayerInterface.ON_PLAYER_COMPLETE:
				sendPlaybackStatus(playbackStatus);
				sendPlaybackTitle(currentTitle,currentImage);
				sendProgressStatus(0);
				break;
			case LinguooMediaPlayerInterface.ON_PLAYER_ERROR:
				sendPlaybackStatus(playbackStatus);
				break;
		}
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
			mpReceiverFilter.addAction("updatePlayerView");
			mpReceiverFilter.addAction("resetPlayer");
			mpReceiverFilter.addAction("moveForward");
				
			registerReceiver(mpReceiver, mpReceiverFilter);
		}
	}
	
	private void unregisterMediaReceiver(){
		unregisterReceiver(mpReceiver);
	}
	
	private void sendPlaybackStatus(int status){
		playbackintent.setAction("playbackStatus");         
		playbackintent.putExtra("playback", status);
		//playbackStatus = status;
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
		progressBarObserver = new ProgressbarObserver();
		playingThread = new Thread(progressBarObserver);
		mediaPlayerStarted = true;
		playingThread.start();
	}
	
	private void stopProgressbarUpdater(){
		if(progressBarObserver != null){
			progressBarObserver.stop();
			progressBarObserver = null;
			playingThread = null;
			mediaPlayerStarted = false;
		}
	}
	
	private void broadcastHandler(Intent intent){
		String action = intent.getAction();
		//Object[] keys = intent.getExtras().keySet().toArray();
		if(action.equals("updatePlayList")){
			playList = intent.getStringArrayListExtra("playList");
		}else if(action.equals("updateTitleList")){
			titleList = intent.getStringArrayListExtra("titleList");
		}else if(action.equals("updateImageList")){
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
		}else if(action.equals("updatePlayerView")){
			updatePlayerView();
		}else if(action.equals("resetPlayer")){
			resetMediaPlayer();
		}else if(action.equals("moveForward")){
			moveForward();
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
				playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_PLAYING;
				sendPlaybackStatus(playbackStatus);
			}			
		});
		
		mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				wifiLock.release();
				mediaPlayerStarted = false;
				playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_COMPLETE;
				sendPlaybackStatus(playbackStatus);
				sendProgressStatus(0);
				cancelNotification();
				if(autoPlay)moveForward();
			}
			
		});
		
		mediaPlayer.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int arg1, int arg2) {
				// TODO Auto-generated method stub
				mediaPlayerStarted = false;
				playbackStatus = LinguooMediaPlayerInterface.ON_PLAYER_ERROR;
				sendPlaybackStatus(playbackStatus);
				return false;
			}
			
		});
	
	}
	
	private void setExternListeners(){		
		telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
				case TelephonyManager.CALL_STATE_OFFHOOK:
				case TelephonyManager.CALL_STATE_RINGING:
					if(!callInCource){
						callInCource = true;
						if(mediaPlayerStarted)pauseOnCallMediaPlayer();
					}
					
					break;
				case TelephonyManager.CALL_STATE_IDLE:
					// Phone idle. Start playing.
					if(callInCource){
						callInCource = false;
						if(mediaPlayerStarted)resumeOnCallMediaPlayer();
					}					
					break;
				}
			}
		};
		
		telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	/******************************************************************************************************/
	
	private void initNotification(String title, String image){
		Context context = getApplicationContext();
		ImageLoader il = new ImageLoader(context);
		Bitmap icon = il.getImageAsBitmap(image);
				
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			.setAutoCancel(true)
			.setSmallIcon(R.drawable.logo)
			.setLargeIcon(icon)
		    .setContentTitle("Linguoo Noticias")
		    .setContentText(title);
		
		Intent resultIntent = new Intent(this, LinguooNewsActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(LinguooNewsActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		//NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		startForeground(NOTIFICATION_ID,mBuilder.build());
	}
	
	private void cancelNotification() {
		//NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//mNotificationManager.cancel(NOTIFICATION_ID);
		stopForeground(true);
	}
		
	
	/******************************************************************************************************/
	
	class ProgressbarObserver implements Runnable{
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
					counter = getPosition();
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
