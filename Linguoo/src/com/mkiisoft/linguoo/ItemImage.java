package com.mkiisoft.linguoo;

import android.R.drawable;

public class ItemImage {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImageView() {
		return imageView;
	}

	public void setImageView(int imageView) {
		this.imageView = imageView;
	}

	public boolean getImageSelected() {
		return imageSelected;
	}

	public void setImageSelected(boolean imageSelected) {
		this.imageSelected = imageSelected;
	}

	public String getTextCategoria() {
		return textCategoria;
	}

	public void setTextCategoria(String textCategoria) {
		this.textCategoria = textCategoria;
	}

	
	private int imageView;
	private boolean imageSelected;
	private String textCategoria;
	private int id;
	
	public ItemImage(int imageView, boolean imageSelected, String textCategoria, int id) {
		
		this.imageView = imageView;
		this.imageSelected = imageSelected;
		this.textCategoria = textCategoria;
		this.id=id;
	}
	
	
	

}
