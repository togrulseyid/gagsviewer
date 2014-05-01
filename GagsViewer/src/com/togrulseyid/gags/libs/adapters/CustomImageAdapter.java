package com.togrulseyid.gags.libs.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.togrulseyid.gags.libs.ImageLoader;
import com.togrulseyid.gags.viewer.R;

public class CustomImageAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> gagsList;
	private LayoutInflater inflater;	
	private ImageLoader imageLoader;
	private Typeface myFont1;	
	private int loader = R.drawable.loader;

	
	public CustomImageAdapter(Context context,	ArrayList<HashMap<String, String>> gagsList) {
		this.gagsList = gagsList;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context);
		imageLoader.setContext(context);
		myFont1 = Typeface.createFromAsset(context.getAssets(), "ttf/angrybirds_regular.ttf");	
	}
	
	@Override
	public int getCount() {
		int size = 0;
		try {
			size = gagsList.size();
		} catch (Exception e) {
		}
		return size;
	}

	@Override
	public HashMap<String, String> getItem(int position) {
		return gagsList.get(position);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}


	@Override
	public int getViewTypeCount() {
	    if (getCount() != 0)
	        return getCount();

	    return 1;
	}
	
	@SuppressLint("NewApi")
	public View getView(int position, View view, ViewGroup viewGroup) {
		View v = view;
		ImageViewHolder holder = null;
	
		if (v == null) {
			holder = new ImageViewHolder();
			v = inflater.inflate(R.layout.image_list_item, null);
			holder.image_list_item_title = (TextView) v.findViewById(R.id.image_list_item_title);
			holder.image_list_item_tumbnail = (ImageView) v.findViewById(R.id.image_list_item_tumbnail);
			v.setTag(holder);
		} else
			holder = (ImageViewHolder) v.getTag();
		
		
		holder.image_list_item_title.setTypeface(myFont1);// TTF
		holder.image_list_item_title.setText(getItem(position).get("alt"));
		
		try {
			imageLoader.DisplayImage(getItem(position).get("src"), loader, holder.image_list_item_tumbnail, 0);
		} catch (Exception e) {
			// TODO: handle exception
			holder.image_list_item_tumbnail.setImageResource(R.drawable.untitled);
		}

		return v;
	}

	private static class ImageViewHolder {
		public TextView image_list_item_title;
		public ImageView image_list_item_tumbnail;
	}
}