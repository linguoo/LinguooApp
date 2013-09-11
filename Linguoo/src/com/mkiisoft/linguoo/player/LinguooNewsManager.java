package com.mkiisoft.linguoo.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.mkiisoft.linguoo.util.Constants;

public class LinguooNewsManager {
	protected static final String TAG = "Linguoo News Manager";
	
	private static JSONArray JSONData;
	private static ArrayList<HashMap<String, String>> ArrayData;
	
	
	public LinguooNewsManager(){

	}
	
	public static void setData(String dataSource){
		initialize(); 
		
		try {
			JSONData = new JSONArray(dataSource);
			for(int i = 0; i < JSONData.length(); i++){
				HashMap<String, String> currentRecord = new HashMap<String, String>();
				for(int b = 0; b < JSONData.getJSONObject(i).length(); b++){
					String key = JSONData.getJSONObject(i).names().optString(b);
					String value = JSONData.getJSONObject(i).getString(JSONData.getJSONObject(i).names().optString(b));
					currentRecord.put(key,value);
				}
				currentRecord.put(Constants.NEWS_ONPLAYLIST, "false");
				ArrayData.add(currentRecord);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initialize(){
		clearData();
		ArrayData = new ArrayList<HashMap<String, String>>();
	}
	
	public static JSONArray getDataAsJSONArray(){
		return JSONData;
	}
	
	public static ArrayList<HashMap<String, String>> getDataAsArrayList(){
		return ArrayData;
	}
	
	public static void clearData(){
		JSONData = null;
		ArrayData = null;
	}
	
	public static int getTotal(){
		return JSONData.length();
	}
	
	public static HashMap<String, String> getRecordByIndex(int index){
		if(getTotal() <= 0)return null;
		else if(getTotal() == 1 && index == 1)return ArrayData.get(0);
		else if(index >= getTotal())return null;			
		else return ArrayData.get(index);
	}
	
	public static int getNewsIdByIndex(int index){
		return getValueAsInteger(index,Constants.NEWS_ID);
	}
	
	public static int getNewsCategoryIdByIndex(int index){
		return getValueAsInteger(index,Constants.NEWS_CATEGORY);
	}
	
	public static int getNewsFeedslnByIndex(int index){
		return getValueAsInteger(index,Constants.NEWS_FEEDSLN);
	}
	
	public static String getNewsTitleByIndex(int index){
		return getValueAsString(index,Constants.NEWS_TITLE);
	}
	
	public static String getNewsContentByIndex(int index){
		return getValueAsString(index,Constants.NEWS_CONTENT);
	}
	
	public static String getNewsImageByIndex(int index){
		return getValueAsString(index,Constants.NEWS_THUMB);
	}
	
	public static String getNewsAudioByIndex(int index){
		return getValueAsString(index,Constants.NEWS_AUDIO);
	}
	
	public static String getNewsNarratorByIndex(int index){
		return getValueAsString(index,Constants.NEWS_NARRATOR);
	}
	
	public static String getNewsDateByIndex(int index){
		return getValueAsString(index,Constants.NEWS_DATE);
	}
	
	public static Boolean getNewsOnPlayListByIndex(int index){
		return Boolean.parseBoolean(getValueAsString(index,Constants.NEWS_ONPLAYLIST));
	}	
	
	public static void setNewsOnPlayListByIndex(int index, boolean status){
		HashMap<String, String> currentRecord;
		
		currentRecord = ArrayData.get(index);
		currentRecord.put(Constants.NEWS_ONPLAYLIST, String.valueOf(status));
		ArrayData.set(index, currentRecord);

	}
	
	private static int getValueAsInteger(int index, String tag){
		if(getRecordByIndex(index) == null)return -1;
		else{
			return Integer.parseInt(getRecordByIndex(index).get(tag));
		}
	}
	
	private static String getValueAsString(int index, String tag){
		if(getRecordByIndex(index) == null)return null;
		else{
			return getRecordByIndex(index).get(tag);
		}
	}	
	
	
}
