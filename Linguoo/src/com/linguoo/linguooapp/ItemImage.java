package com.linguoo.linguooapp;


public class ItemImage {
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImageView() {
		return imageView;
	}

	public void setImageView(String imageView) {
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

	
	private String imageView;
	private int imageSelected;
	private String textCategoria;
	private int id;
	
	public ItemImage(String imageView, int imageSelected, String textCategoria, int id) {
		
		this.imageView = imageView;
		this.imageSelected = imageSelected;
		this.textCategoria = textCategoria;
		this.id=id;
	}
	
	
	

}