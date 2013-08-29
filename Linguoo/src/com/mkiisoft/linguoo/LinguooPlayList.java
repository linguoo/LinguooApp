package com.mkiisoft.linguoo;

import java.util.ArrayList;
import java.util.HashMap;

import com.mkiisoft.linguoo.util.Constants;

import android.util.Log;

public class LinguooPlayList {
	protected static final String TAG = "Linguoo PlayList";
	private ArrayList<HashMap<String, String>> playListStore;
	private HashMap<String, String> entry;
	private Integer index;
	
	private static Boolean repeatMode = false;
	
	public LinguooPlayList(){
		playListStore = new ArrayList<HashMap<String, String>>();
		index = 0;
	}
	
	public void add(HashMap<String, String> track){
		entry = createEntry(track);		
		playListStore.add(entry);
	}
	
	public void remove(HashMap<String, String> track){
		entry = createEntry(track);				
		playListStore.remove(entry);
	}
	
	public Integer getTotal(){
		return playListStore.size();
	}
	
	public ArrayList<HashMap<String, String>> getStore(){
		return playListStore;
	}
	
	public HashMap<String, String> getCurrentTrack(){
		entry = createEntry(playListStore.get(index));
		return entry;
	}
	
	public Integer getCurrentIndex(){
		return index;
	}
	
	public Integer getCurrentTrackId(){
		entry = createEntry(playListStore.get(index));
		return Integer.parseInt(entry.get(Constants.NEWS_ID));
	}
	
	public String getCurrentTrackTitle(){
		entry = createEntry(playListStore.get(index));
		return entry.get(Constants.NEWS_TITLE);
	}
	
	public String getCurrentTrackURL(){
		entry = createEntry(playListStore.get(index));
		return entry.get(Constants.NEWS_AUDIO);
	}
	
	public void setIndexTo(Integer newIndex){
		if(newIndex >= getTotal())index = getTotal();
		else if(newIndex <= 0)index = 0;
		else index = newIndex;
	}
	
	public void moveForward(){
		if(index >= (getTotal() - 1)){
			if(getRepeateMode())index = 0;
			else index = (getTotal() - 1);
		}else{
			index++;
		}
	}
	
	public void moveBackward(){
		if(index <= 0)index = 0;
		else index--;
	}
	
	public void setRepeatModeOn(){
		repeatMode = true;
	}
	
	public void setRepeateModeOff(){
		repeatMode = false;
	}
	
	public Boolean getRepeateMode(){
		return repeatMode;
	}
	
	private HashMap<String, String> createEntry(HashMap<String, String> track){
		entry = new HashMap<String, String>();
		String newsId = track.get(Constants.NEWS_ID);
		String title = track.get(Constants.NEWS_TITLE);
		String audio_url = track.get(Constants.NEWS_AUDIO);
		
		entry.put(Constants.NEWS_ID, newsId);
		entry.put(Constants.NEWS_TITLE, title);
		entry.put(Constants.NEWS_AUDIO, audio_url);
		return entry;
	}	
	
}
