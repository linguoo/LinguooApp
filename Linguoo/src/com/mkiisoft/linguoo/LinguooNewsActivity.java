package com.mkiisoft.linguoo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.ListView;
import android.view.View;
import android.view.View.OnClickListener;
import com.mkiisoft.linguoo.CustomNewsAdapter.newsHolder;

public class LinguooNewsActivity extends Activity implements AccessibilityEventSource {
	protected static final String TAG = "Linguoo Noticias";
	static final String KEY_NEWS = "item"; 
	static final String KEY_CATEGORY = "category";
	static final String KEY_ID = "id";
	static final String KEY_TITLE = "title";
	static final String KEY_CONTENT = "content";
	static final String KEY_THUMB = "thumb_url";
	static final String KEY_AUDIO = "audio_url";
	
	CustomNewsAdapter adapter;
	ListView newsList;
	btnAddClick btnAdd;
	itemClick itemPlay;
	newsHolder nHolder;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainView();
        launchNews();        
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void sendAccessibilityEvent(int arg0) {
			
	}

	@Override
	public void sendAccessibilityEventUnchecked(AccessibilityEvent arg0) {
		
	}
	
	/*  *********************************************************************************************** */
	
	private void setMainView() {
		setContentView(R.layout.news_layout);
		newsList = (ListView) this.findViewById(R.id.lstNews);
	}
	
	private void launchNews(){
		try {
			InputStream is = getAssets().open("news.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				try {
					Document doc = db.parse(new InputSource(is));
					doc.getDocumentElement().normalize();
		            NodeList nodeList = doc.getElementsByTagName(KEY_NEWS);
		            populateList(nodeList);
		            
				} catch (SAXException e) {
					e.printStackTrace();
				}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void populateList(NodeList nodeList){
		
		ArrayList<HashMap<String, String>> arrayNewsList = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < nodeList.getLength(); i++){
			HashMap<String, String> curNews = new HashMap<String, String>();			
			
			Node node = nodeList.item(i);
			
			Element curEl = (Element) node;
            NodeList nl = curEl.getElementsByTagName(KEY_ID);
            Element idElement = (Element) nl.item(0);
            nl = idElement.getChildNodes();
            curNews.put(KEY_ID, ((Node) nl.item(0)).getNodeValue());
            
            curEl = (Element) node;
            nl = curEl.getElementsByTagName(KEY_CATEGORY);
            idElement = (Element) nl.item(0);
            nl = idElement.getChildNodes();
            curNews.put(KEY_CATEGORY, ((Node) nl.item(0)).getNodeValue());
            
            curEl = (Element) node;
            nl = curEl.getElementsByTagName(KEY_TITLE);
            idElement = (Element) nl.item(0);
            nl = idElement.getChildNodes();
            curNews.put(KEY_TITLE, ((Node) nl.item(0)).getNodeValue());
            
            curEl = (Element) node;
            nl = curEl.getElementsByTagName(KEY_CONTENT);
            idElement = (Element) nl.item(0);
            nl = idElement.getChildNodes();
            curNews.put(KEY_CONTENT, ((Node) nl.item(0)).getNodeValue());
            
            curEl = (Element) node;
            nl = curEl.getElementsByTagName(KEY_THUMB);
            idElement = (Element) nl.item(0);
            nl = idElement.getChildNodes();
            curNews.put(KEY_THUMB, ((Node) nl.item(0)).getNodeValue());
            
            curEl = (Element) node;
            nl = curEl.getElementsByTagName(KEY_AUDIO);
            idElement = (Element) nl.item(0);
            nl = idElement.getChildNodes();
            curNews.put(KEY_AUDIO, ((Node) nl.item(0)).getNodeValue());

            arrayNewsList.add(curNews);
		}
	
		btnAdd = new btnAddClick();
		itemPlay = new itemClick();
		adapter = new CustomNewsAdapter(this, R.layout.news_item_layout, arrayNewsList, itemPlay, btnAdd);        
		newsList.setAdapter(adapter);
	}
	
	
	private void playNews(View v){
		nHolder = (newsHolder)v.getTag();
		
	}
	
	private void addNews(View v){
		nHolder = (newsHolder)v.getTag();
		
	}
	
	
	/*  *********************************************************************************************** */
	
	private class itemClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			playNews(v);
		}		
	}
	
	private class btnAddClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			View vParent = (View) v.getParent();
			addNews(vParent);
		}		
	}		
}