package com.mkiisoft.linguoo;

public class RssFeed {

	private String title;
	private String link;
	private String content;
	private String mediaurl;

	
	public RssFeed(String title,String link,String content,String mediaurl) {
		this.title= title;
		this.link= link;
		this.content=content;
		this.mediaurl=mediaurl;
		
	}
	
	public RssFeed(String title,String link,String content){
		this(title,link,content,"");
	}

	public RssFeed(String title,String link){
		this(title,link,"");
	}
	
	public RssFeed(String title){
		this(title,"");
	}

	public RssFeed(){
		this("");
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMediaUrl() {
		return mediaurl;
	}

	public void setLanguage(String mediaurl) {
		this.mediaurl = mediaurl;
	}
}
