package com.linguoo.linguooapp.player;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.linguoo.linguooapp.util.Constants;

public class LinguooMediaPlayer  {
	protected static final String TAG = "Linguoo MediaPlayer";
	private static final String mpService = "com.linguoo.linguooapp.player.LinguooMediaPlayerService";
	
	private int playListIndex;
	private int playbackStatus;
	private int progressStatus;
	private ArrayList<HashMap<String, String>> playList;
	private HashMap<String, String> entry;
	private Intent mpBroadcastIntent;
	private IntentFilter playbackFilter;
	private LinguooMediaPlayerInterface mpInterface;
	private String autoPlay;
	private Context context;
	private String newsTitle;
	private String newsImage;
	
	public LinguooMediaPlayer(Context c){
		context = c;
		initialize();
		configurePlaybackFilter();
	}
	
	public void addToPlayList(int index){
		playList.add(createEntry(LinguooNewsManager.getRecordByIndex(index)));
		LinguooNewsManager.addSelectedIndex(index);
		updatePlayListService();
	}
	
	public void addToPlayList(String items){
		ArrayList<Integer> itemsList = LinguooNewsManager.getSelectedItemsAsArray(items);
		clearPlaylist();
		entry = null;
		playListIndex = 0;
		
		if(itemsList != null && itemsList.size() > 0){
			for(int index : itemsList){
				playList.add(createEntry(LinguooNewsManager.getRecordByIndex(index)));
			}
			updatePlayListService();
		}
	}


	public void removeFromPlayList(int index){
		playList.remove(createEntry(LinguooNewsManager.getRecordByIndex(index)));
		LinguooNewsManager.removeSelectedIndex(index);
		updatePlayListService();
	}
	
	public String getSelectedIndexes(){
		return LinguooNewsManager.getSelectedIndexesAsString();
	}
	
	public int getTotal(){
		return playList.size();
	}
	
	public void play(int index){
		if(index >= 0){
			entry = createEntry(LinguooNewsManager.getRecordByIndex(index));
			sendPlayPlayerService(entry);
		}else{			
			if(playbackStatus == LinguooMediaPlayerInterface.ON_PLAYER_PAUSE || 
			   playbackStatus == LinguooMediaPlayerInterface.ON_PLAYER_COMPLETE){
				sendResumePlayerService();
			}
			/*else{
				if(getTotal() > 0){
					Log.d(TAG,"PORQUE HAY ELEMENTOS: " + getTotal() + " PARA REPRODUCIR");
					playListIndex = 0;
					entry = createEntry(playList.get(playListIndex));
					sendPlayPlayerService(entry);
				}
			}*/
		}
	}
	
	public void pause(){
		sendPausePlayerService();
	}
	
	public void resume(){
		sendResumePlayerService();
	}
	
	public void moveForward(){
		sendBroadcastAction("moveForward");
	}
	
	public int getPlaybackStatus(){
		return playbackStatus;
	}
	
	public void clearPlaylist(){
		playList = null;
		playList = new ArrayList<HashMap<String, String>>();
	}
	
	public Boolean isPlaying(){
		if(playbackStatus == LinguooMediaPlayerInterface.ON_PLAYER_PLAYING)return true;
		else return false;
	}
	
	public Boolean isPaused(){
		if(playbackStatus == LinguooMediaPlayerInterface.ON_PLAYER_PAUSE)return true;
		else return false;
	}
	
	public Boolean isResumed(){
		if(playbackStatus == LinguooMediaPlayerInterface.ON_PLAYER_RESUME)return true;
		else return false;
	}

	public Boolean isCallAttached(){
		if(playbackStatus == LinguooMediaPlayerInterface.ON_CALL_ATTACHED)return true;
		else return false;
	}

	public void setAutoPlay(Boolean value){
		autoPlay = value.toString();
		sendBroadcastAsString("autoPlay", autoPlay, "setAutoPlay");
	}
		
	public void updatePlayerView(){
		sendBroadcastAction("updatePlayerView");
	}
	
	public String getNewsTitle(){
		return newsTitle;
	}
	
	public String getNewsImage(){
		return newsImage;
	}
	
	private void initialize(){
		mpBroadcastIntent = new Intent();
		mpInterface = (LinguooMediaPlayerInterface)context;	
		clearPlaylist();
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
		mpBroadcastIntent.setAction(action);         
        context.sendBroadcast(mpBroadcastIntent); 
	}
	
	private void sendBroadcastAsString(String param, String value, String action){
		mpBroadcastIntent.setAction(action);         
		mpBroadcastIntent.putExtra(param, value);       
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
			//stopMediaPlayerService();
		}
		Intent serviceIntent = new Intent(context, LinguooMediaPlayerService.class);
		context.startService(serviceIntent);
		
	}
	
	public void stopMediaPlayerService(){
		context.stopService(new Intent(context, LinguooMediaPlayerService.class));
	}
	
	public void restartPlayer(){
		clearPlaylist();
		sendBroadcastAction("resetPlayer");
	}
	
	private Boolean checkIfMediaPlayerServiceIsRunning(){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
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
				newsTitle = bufferIntent.getStringExtra("title");
				newsImage = bufferIntent.getStringExtra("image");
				mpInterface.playerTitleHandler(newsTitle,newsImage);
			}
		}
	};

}
