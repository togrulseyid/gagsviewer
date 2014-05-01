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

import com.togrulseyid.gags.libs.ImageLoader2Video;
import com.togrulseyid.gags.viewer.R;

public class CustomAnimationAdapter extends BaseAdapter {
	
	private ArrayList<HashMap<String, String>> gagsList;
	LayoutInflater inflater;
	ImageLoader2Video imgLoader;
	private Typeface myFont1;	
	int loader = R.drawable.loader;
	
	public CustomAnimationAdapter(Context context, ArrayList<HashMap<String, String>> gagsList ) {
		this.gagsList = gagsList;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imgLoader = new ImageLoader2Video(context);
		myFont1 = Typeface.createFromAsset(context.getAssets(), "ttf/angrybirds_regular.ttf");	
	}

	@Override
	public int getCount() {
		int size = 0;
		try {
			size = gagsList.size();
		} catch (Exception e) {
//			return 0;
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

	
//	 TextView animation_list_item_title ;
//	 ImageView animation_list_item_tumbnail ;
	 
	@SuppressLint({ "NewApi", "ResourceAsColor" })
	public View getView(int position, View view, ViewGroup viewGroup) {
		View v = view;
		AnimationViewHolder holder = null;
		if (view == null) {
			holder = new AnimationViewHolder();
	        v = inflater.inflate(R.layout.animation_list_item, null);
	        holder.animation_list_item_title = (TextView) v.findViewById(R.id.animation_list_item_title);
	        holder.animation_list_item_tumbnail = (ImageView) v.findViewById(R.id.animation_list_item_image);
	        v.setTag(holder);
	    } else 
	        holder = (AnimationViewHolder) v.getTag();
	   
		holder.animation_list_item_title.setTypeface(myFont1);// TTF
	    holder.animation_list_item_title.setText(getItem(position).get("alt"));
	    
		try {
			imgLoader.DisplayImage2Video(getItem(position).get("src"), loader, holder.animation_list_item_tumbnail);
//				imgLoader.DisplayImage(getItem(position).get("src"), loader, holder.animation_list_item_tumbnail,1);
		} catch (Exception e) {
			holder.animation_list_item_tumbnail.setImageResource(R.drawable.untitled);
		}

		    return v;
		}
	
	private static class AnimationViewHolder {
		TextView animation_list_item_title;
		ImageView animation_list_item_tumbnail;
	}	
	
	@Override
	public int getViewTypeCount() {
	    if (getCount() != 0)
	        return getCount();
	
	    return 1;
	}
}