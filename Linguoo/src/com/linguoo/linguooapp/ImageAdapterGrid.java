package com.linguoo.linguooapp;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageAdapterGrid extends ArrayAdapter<ItemImage> {

	private Context mContext;
	private ArrayList<ItemImage> arrayCat;
	private int layout,positionGrid;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	

	public ImageAdapterGrid(Context c, ArrayList<ItemImage> array,
			int layoutItem) {
		super(c,layoutItem,array);
		mContext = c;
		this.layout = layoutItem;
		this.arrayCat = array;
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.logo)
		.cacheOnDisc(true)
		.cacheInMemory(true)
		.build();
		this.imageLoader = ImageLoader.getInstance();
		
		this.imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));


	}

	@Override
	public int getCount() {
		return arrayCat.size();

	}

	@Override
	public ItemImage getItem(int arg0) {
		return arrayCat.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("IMG Adapter","Dibujando el item: "+position);
		View v;
		ViewHolder vh= null;
		positionGrid=position;
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.cat_item_grid, parent, false);
			vh = new ViewHolder();
			vh.t = (TextView) v.findViewById(R.id.txt_categoria_grid);
			vh.iv = (ImageView) v.findViewById(R.id.img_categoria_grid);
			vh.chk = (ImageView) v.findViewById(R.id.img_selected);

			v.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
			v=(View) convertView;
		}

		vh.t.setText(arrayCat.get(position).getTextCategoria());
		String image = arrayCat.get(position).getImageView();
		if (image.length() == 0 || image.length() == 1) {
			vh.iv.setImageResource(R.drawable.no_image);
		} else {
			imageLoader.displayImage(arrayCat.get(position).getImageView(), vh.iv,options);
		}
		if (arrayCat.get(positionGrid).getImageSelected() == 1) {
			vh.chk.setBackgroundResource(R.drawable.cat_select);
		} else {
			vh.chk.setBackgroundResource(R.drawable.cat_unselect);
		}

		return v;
	}

	class ViewHolder {
		ImageView iv;
		ImageView chk;
		TextView t;
		int id;

	}

}
