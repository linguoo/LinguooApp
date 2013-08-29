package com.mkiisoft.linguoo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mkiisoft.linguoo.LinguooNewsActivity.PlayerCallbackInterface;
import com.mkiisoft.linguoo.util.Constants;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.util.Log;

public class LinguooMediaPlayer {
	protected static final String TAG = "Linguoo MediaPlayer";
	private Activity activity;
	private MediaPlayer mediaPlayer;
	private Boolean intialStage = true;
	private Boolean fromPause = false;
	private String currentURL;
	private MediaObserver observer;
	private PlayerCallbackInterface playerCallback;
	private Thread playingThread;
	
	public LinguooMediaPlayer(Activity a, PlayerCallbackInterface callback){
		activity = a;
		playerCallback = callback;
	}
	
	public void play(String url){
		currentURL = url;
		
		if(url == null)return;
		
		if(mediaPlayer != null && mediaPlayer.isPlaying()){
			stop();
		}else if(mediaPlayer != null && fromPause){
			pause();
		}else if(intialStage){
			createMedia();
			new Player().execute(currentURL);
		}else{
			if(mediaPlayer != null && !mediaPlayer.isPlaying()){
				observer = new MediaObserver();
				playingThread = new Thread(observer);
		        mediaPlayer.start();
		        playingThread.start();
		    }
		}
	}
	
	private void createMedia(){
		destroyMedia();
		mediaPlayer = new MediaPlayer();
	}
	
	public void pause(){
		if(mediaPlayer.isPlaying()){
			playerCallback.playerStatus(Constants.NEWS_AUDIO_PAUSE, 0);
			mediaPlayer.pause();
			fromPause = true;
		}
		else {
			playerCallback.playerStatus(Constants.NEWS_AUDIO_RESUME, 0);
			fromPause = false;
			mediaPlayer.start();
		}
	}
	
	public void stop(){
		playerCallback.playerStatus(Constants.NEWS_AUDIO_STOP, 0);
		intialStage = true;
		observer = null;
		destroyMedia();		
	}
	
	private void destroyMedia(){
		
		if (mediaPlayer != null){
			if (mediaPlayer.isPlaying()){
				mediaPlayer.stop();
            }
			mediaPlayer.release();
			mediaPlayer=null;
		}
	}
		
	
	/*  *********************************************************************************************** */
	
	class Player extends AsyncTask<String, Void, Boolean> {
		public Player(){
			
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean prepared;
			playerCallback.playerStatus(Constants.NEWS_AUDIO_LOADING, 0);
			
	        try {
	            mediaPlayer.setDataSource(params[0]);
	            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	                @Override
	                public void onCompletion(MediaPlayer mp) {
	                    // TODO Auto-generated method stub
	                    intialStage = true;
	                    //playPause=false;
	                    mediaPlayer.stop();
	                    mediaPlayer.reset();
	                }
	            });
	            mediaPlayer.prepare();
	            prepared = true;
	        } catch (IllegalArgumentException e) {
	            // TODO Auto-generated catch block
	            Log.d("IllegarArgument", e.getMessage());
	            prepared = false;
	            e.printStackTrace();
	        } catch (SecurityException e) {
	        	// TODO Auto-generated catch block
	        	Log.d("SecurityException", e.getMessage());
	            prepared = false;
	            e.printStackTrace();
	        } catch (IllegalStateException e) {
	            // TODO Auto-generated catch block
	        	Log.d("IllegalStateException", e.getMessage());
	            prepared = false;
	            e.printStackTrace();
	        } catch (IOException e) {
				// TODO Auto-generated catch block
	        	Log.d("IOException", e.getMessage());
	        	prepared = false;
				e.printStackTrace();
			}
	        return prepared;
		}
		
		
		@Override
	    protected void onPostExecute(Boolean result) {
	        // TODO Auto-generated method stub
	        super.onPostExecute(result);
	        intialStage = false;
	        playerCallback.playerStatus(Constants.NEWS_AUDIO_READY, 0);
	        play(currentURL);
	    }

	    @Override
	    protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	    }
	}
	
	class MediaObserver implements Runnable{
		private AtomicBoolean stop = new AtomicBoolean(false);
		private Integer counter = 0; 
		
		public void stop() {
		    stop.set(true);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (!stop.get() && mediaPlayer != null){
				if(mediaPlayer.isPlaying()){
					counter = (mediaPlayer.getCurrentPosition() * 100) / mediaPlayer.getDuration();
					playerCallback.playerStatus(Constants.NEWS_AUDIO_PLAYING, counter);
					try {
						Thread.sleep(1000);
					}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				}else{
					this.stop();
				}
			}
		}
	}
}


