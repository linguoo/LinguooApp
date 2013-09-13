package com.mkiisoft.linguoo.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.mkiisoft.linguoo.util.Constants;

public class LinguooNewsManager {
	protected static final String TAG = "Linguoo News Manager";
	
	private static JSONArray JSONData;
	private static ArrayList<HashMap<String, String>> ArrayData;
	private static ArrayList<Integer> selectedIndex;
	
	public LinguooNewsManager(){

	}
	
	public static void setData(String dataSource, String selectedItems){
		initialize(); 
		
		selectedIndex = getSelectedItemsAsArray(selectedItems);
		
		try {
			JSONData = new JSONArray(dataSource);
			for(int i = 0; i < JSONData.length(); i++){
				HashMap<String, String> currentRecord = new HashMap<String, String>();
				for(int b = 0; b < JSONData.getJSONObject(i).length(); b++){
					String key = JSONData.getJSONObject(i).names().optString(b);
					String value = JSONData.getJSONObject(i).getString(JSONData.getJSONObject(i).names().optString(b));
					currentRecord.put(key,value);
				}
				Boolean added = checkIfItemIsSelected(i);
				currentRecord.put(Constants.NEWS_ONPLAYLIST, added.toString());
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
	
	public static void addSelectedIndex(int index){
		selectedIndex.add(index);
	}
	
	public static void removeSelectedIndex(int index){
		if(selectedIndex.size() > 0){
			for(int i=0; i < selectedIndex.size(); i++){
				if(index == selectedIndex.get(i)){
					selectedIndex.remove(i);
				}
			}
		}
	}
	
	public static ArrayList<Integer> getSelectedIndexes(){
		return selectedIndex;
	}	
	
	public static void updateSelectedIndexes(ArrayList<Integer> updatedIndexes){
		selectedIndex = updatedIndexes;
	}
	
	public static String getSelectedIndexesAsString(){
		String indexes = "";
		if(selectedIndex.size() > 0){
			for(int index : selectedIndex){
				indexes += index + ",";
			}
			indexes = indexes.substring(0, indexes.length() - 1);
		}
		return indexes;
	}
	
	public static ArrayList<Integer> getSelectedItemsAsArray(String items){
		selectedIndex = new ArrayList<Integer>();
		if(items != null && !items.equals("")){
			String[] listItems = items.split(",");
			if(listItems.length > 0 ){
				for(String item  : listItems)selectedIndex.add(Integer.parseInt(item));
			}
		}		
		return selectedIndex;			
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
	
	private static Boolean checkIfItemIsSelected(int index){
		if(selectedIndex.size() > 0){
			for(int i=0; i < selectedIndex.size(); i++){
				if(index == selectedIndex.get(i)){
					return true;
				}
			}
		}
		return false;		
	}	
}
