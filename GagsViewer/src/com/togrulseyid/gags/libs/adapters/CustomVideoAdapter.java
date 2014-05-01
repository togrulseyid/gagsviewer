package com.togrulseyid.gags.libs.adapters;

import java.util.ArrayList;
import java.util.HashMap;

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

/**
 * Different tumbnails
 *	http://img.youtube.com/vi/<insert-youtube-video-id-here>/0.jpg
 *	http://img.youtube.com/vi/<insert-youtube-video-id-here>/1.jpg
 *	http://img.youtube.com/vi/<insert-youtube-video-id-here>/2.jpg
 *	http://img.youtube.com/vi/<insert-youtube-video-id-here>/3.jpg
 * 
 * With different quality
 * http://img.youtube.com/vi/<insert-youtube-video-id-here>/default.jpg
 * http://img.youtube.com/vi/<insert-youtube-video-id-here>/hqdefault.jpg
 * http://img.youtube.com/vi/<insert-youtube-video-id-here>/mqdefault.jpg
 * http://img.youtube.com/vi/<insert-youtube-video-id-here>/sddefault.jpg
 * http://img.youtube.com/vi/<insert-youtube-video-id-here>/maxresdefault.jpg
 * */
public class CustomVideoAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, String>> gagsList;
	private LayoutInflater inflater;
	private ImageLoader2Video imgLoader;
	private int loader = R.drawable.untitled;
	private Typeface myFont1;	
	
	public CustomVideoAdapter(Context context, ArrayList<HashMap<String, String>> gagsList, int Rlayout, String[] variables, int[] title_and_img) {
		this.gagsList = gagsList;
		imgLoader = new ImageLoader2Video(context);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	
	public View getView(int position, View view, ViewGroup viewGroup) {
		View v = view;
		VideoViewHolder holder = null;

	    if (view == null) {
	    	holder = new VideoViewHolder();
	        v = inflater.inflate(R.layout.video_list_item, null);
	        holder.video_list_item_title  = (TextView) v.findViewById(R.id.video_list_item_title);
	        holder.list_item_tumbnail = (ImageView) v.findViewById(R.id.video_list_item_image);
	        v.setTag(holder);
	    } else 
	        holder = (VideoViewHolder) v.getTag();
	    
	    holder.video_list_item_title.setTypeface(myFont1);
	    holder.video_list_item_title.setText(getItem(position).get("alt"));
	    String tumbnail = "http://img.youtube.com/vi/"+getItem(position).get("src") + "/" + "hqdefault.jpg";//"default.jpg"; // "mqdefault.jpg";  //"hqdefault.jpg";

		try {
			imgLoader.DisplayImage2Video(tumbnail, loader, holder.list_item_tumbnail);
		} catch (Exception e) {
			holder.list_item_tumbnail.setImageResource(R.drawable.play_alt_32x32);
		}

	    return v;
	}
	
	private static class VideoViewHolder {
		TextView video_list_item_title;
		ImageView list_item_tumbnail;
	}

}