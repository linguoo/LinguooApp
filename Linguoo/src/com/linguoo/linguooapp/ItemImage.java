package com.linguoo.linguooapp;

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

	public int getImageSelected() {
		return imageSelected;
	}

	public void setImageSelected(int imageSelected) {
		this.imageSelected = imageSelected;
	}

	public String getTextCategoria() {
		return textCategoria;
	}

	public void setTextCategoria(String textCategoria) {
		this.textCategoria = textCategoria;
	}

	
	private int imageView;
	private int imageSelected;
	private String textCategoria;
	private int id;
	
	public ItemImage(int imageView, int imageSelected, String textCategoria, int id) {
		
		this.imageView = imageView;
		this.imageSelected = imageSelected;
		this.textCategoria = textCategoria;
		this.id=id;
	}
	
	
	

}