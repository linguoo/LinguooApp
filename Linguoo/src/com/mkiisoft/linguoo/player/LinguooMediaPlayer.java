package com.mkiisoft.linguoo.player;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.mkiisoft.linguoo.util.Constants;

public class LinguooMediaPlayer  {
	protected static final String TAG = "Linguoo MediaPlayer";
	private static final String mpService = "com.mkiisoft.linguoo.player.LinguooMediaPlayerService";
	
	private Context context;
	private int playListIndex;
	private ArrayList<HashMap<String, String>> playList;
	private HashMap<String, String> entry;
	private Intent mpBroadcastIntent;
	private IntentFilter playbackFilter;
	private LinguooMediaPlayerInterface mpInterface;
	private int playbackStatus;
	private int progressStatus;
	private Boolean autoPlay;
	
	public LinguooMediaPlayer(Context c){
		context = c;
		initialize();
		configurePlaybackFilter();
	}
	
	public void addToPlayList(int index){
		playList.add(createEntry(LinguooNewsManager.getRecordByIndex(index)));
		updatePlayListService();
	}
	
	public void removeFromPlayList(int index){
		playList.remove(createEntry(LinguooNewsManager.getRecordByIndex(index)));
		updatePlayListService();
	}
	
	public int getTotal(){
		return playList.size();
	}
	
	public void play(int index){
		if(index >= 0){
			entry = createEntry(LinguooNewsManager.getRecordByIndex(index));
			sendPlayPlayerService(entry);
		}else{
			if(getTotal() > 0){
				playListIndex = 0;
				entry = createEntry(playList.get(playListIndex));
				sendPlayPlayerService(entry);
			}else{
				sendResumePlayerService();
			}
		}		
		
	}
	
	public void pause(){
		sendPausePlayerService();
	}
	
	public void resume(){
		sendResumePlayerService();
	}
	
	public void moveForward(){
		Log.d(TAG,"Current: " + playListIndex + " - Total: " + getTotal());
		
		entry = createEntry(playList.get(playListIndex));	
		
		if(playListIndex >= (getTotal() - 1))playListIndex = 0;
		else playListIndex++;		
		
		sendPlayPlayerService(entry);
	}
	
	public int getPlaybackStatus(){
		return playbackStatus;
	}
	
	public Boolean isPlaying(){
		if(playbackStatus == mpInterface.ON_PLAYER_PLAYING)return true;
		else return false;
	}

	public void setAutoPlay(Boolean value){
		autoPlay = value;
		sendBroadcastAsString("autoPlay", autoPlay, "setAutoPlay");
	}
	
	private void initialize(){
		mpBroadcastIntent = new Intent();
		playList = new ArrayList<HashMap<String, String>>();
		mpInterface = (LinguooMediaPlayerInterface)context;	
		
	}
	
	private HashMap<String, String> createEntry(HashMap<String, String> data){
		entry = new HashMap<String, String>();
		
		entry.put(Constants.NEWS_ID, data.get(Constants.NEWS_ID));
		entry.put(Constants.NEWS_TITLE, data.get(Constants.NEWS_TITLE));
		entry.put(Constants.NEWS_AUDIO, data.get(Constants.NEWS_AUDIO));
		entry.put(Constants.NEWS_THUMB, data.get(Constants.NEWS_THUMB));

		return entry;
		
	}
	
	private ArrayList<String> getPlayData(HashMap<String, String> data){
		ArrayList<String> playData = new ArrayList<String>();
		playData.add(data.get(Constants.NEWS_ID));
		playData.add(data.get(Constants.NEWS_AUDIO));
		playData.add(data.get(Constants.NEWS_TITLE));
		playData.add(data.get(Constants.NEWS_THUMB));
		return playData;
	}
	
	private void sendBroadcastAction(String action){
		Log.d(TAG, "Enviando Action...");
		mpBroadcastIntent.setAction(action);         
        context.sendBroadcast(mpBroadcastIntent); 
	}
	
	private void sendBroadcastAsString(String param, Boolean value, String action){
		mpBroadcastIntent.setAction(action);         
		mpBroadcastIntent.putExtra(param, value.toString());       
        context.sendBroadcast(mpBroadcastIntent); 
	}
	
	private void sendBroadcastAsArray(String param, ArrayList<String> value, String action){
		mpBroadcastIntent.setAction(action);         
		mpBroadcastIntent.putStringArrayListExtra(param, value);       
        context.sendBroadcast(mpBroadcastIntent); 
	}
	
	private void updatePlayListService(){
		sendBroadcastAsArray("playList",getPlayList(),"updatePlayList");
		sendBroadcastAsArray("titleList",getTitleList(),"updateTitleList");
		sendBroadcastAsArray("imageList",getImageList(),"updateImageList");
	}
	
	private void sendPlayPlayerService(HashMap<String, String> entry){
		sendBroadcastAsArray("play",getPlayData(entry),"playThisAudio");
	}
	
	private void sendPausePlayerService(){
		sendBroadcastAction("pauseThisAudio");
	}
	
	private void sendResumePlayerService(){
		sendBroadcastAction("resumeThisAudio");
	}
	
	private ArrayList<String> getPlayList(){
		return extractDataFromPlayList(Constants.NEWS_AUDIO);		
	}
	
	private ArrayList<String> getTitleList(){
		return extractDataFromPlayList(Constants.NEWS_TITLE);		
	}
	
	private ArrayList<String> getImageList(){
		return extractDataFromPlayList(Constants.NEWS_THUMB);		
	}
		
	private ArrayList<String> extractDataFromPlayList(String dataTag){
		ArrayList<String> pl = new ArrayList<String>();
		for(int i=0; i < playList.size(); i++){
			pl.add(playList.get(i).get(dataTag));
		}
		return pl;
	}
	
	/*******************************************************************************************************/
	
	public void configurePlaybackFilter(){
		playbackFilter = new IntentFilter();
		playbackFilter.addAction("playbackStatus");
		playbackFilter.addAction("progressStatus");
		playbackFilter.addAction("playbackTitle");
	}
	
	public void registerPlaybackReceiver(){
		context.registerReceiver(playbackReceiver, playbackFilter);
	}
	
	public void unregisterPlaybackReceiver(){
		context.unregisterReceiver(playbackReceiver);
	}
	
	public void startMediaPlayerService(){
		if(checkIfMediaPlayerServiceIsRunning()){
			stopMediaPlayerService();
		}
		Intent serviceIntent = new Intent(context, LinguooMediaPlayerService.class);
		context.startService(serviceIntent);
		
	}
	
	public void stopMediaPlayerService(){
		context.stopService(new Intent(context, LinguooMediaPlayerService.class));
	}
	
	private Boolean checkIfMediaPlayerServiceIsRunning(){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			Log.d(TAG,"Service: " + service.service.getClassName());
			if (mpService.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	private BroadcastReceiver playbackReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent bufferIntent) {
			// TODO Auto-generated method stub
			if(bufferIntent.getAction().equals("playbackStatus")){
				playbackStatus = bufferIntent.getIntExtra("playback", -1);
				mpInterface.playerStatusHandler(playbackStatus);
			}
			if(bufferIntent.getAction().equals("progressStatus")){
				progressStatus = bufferIntent.getIntExtra("progress", -1);
				mpInterface.playerProgressHandler(progressStatus);
			}
			if(bufferIntent.getAction().equals("playbackTitle")){
				mpInterface.playerTitleHandler(bufferIntent.getStringExtra("title"),bufferIntent.getStringExtra("image"));
			}
		}
	};

}
