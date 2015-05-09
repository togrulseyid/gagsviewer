package com.togrulseyid.gags.libs.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.togrulseyid.gags.models.DataModel;
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
	
	private LayoutInflater inflater;
	private int loader = R.drawable.untitled;
	private Typeface typeFace;
	private Activity activity;
	private ArrayList<DataModel> dataModels;
	
	public CustomVideoAdapter(Activity activity, ArrayList<DataModel> imageModelList) {
		
		this.activity = activity;
		this.dataModels = imageModelList;
		this.inflater = this.activity.getLayoutInflater();
		typeFace = Typeface.createFromAsset(this.activity.getAssets(), "ttf/angrybirds_regular.ttf");

	}

	@Override
	public int getCount() {
		return dataModels.size();
	}

	@Override
	public DataModel getItem(int position) {
		return dataModels.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}
	
	public View getView(int position, View view, ViewGroup viewGroup) {
		VideoViewHolder holder = null;

		if (view == null) {

			holder = new VideoViewHolder();

			view = inflater.inflate(R.layout.video_list_item, viewGroup, false);

			holder.title = (TextView) view.findViewById(R.id.video_list_item_title);
			holder.image = (ImageView) view.findViewById(R.id.video_list_item_image);

			view.setTag(holder);

		} else {
			holder = (VideoViewHolder) view.getTag();
		}
	    
		if (getItem(position).getSrc() != null) {
			view.setVisibility(View.VISIBLE);
			holder.title.setTypeface(typeFace);
			holder.title.setText(getItem(position).getAlt());

			Picasso.with(activity)
					.load("http://img.youtube.com/vi/"
							+ getItem(position).getSrc() + "/"
							+ "hqdefault.jpg").fit().placeholder(loader)
					.error(R.drawable.play_alt_32x32).into(holder.image);
		} else {
			view.setVisibility(View.GONE);
		}

	    return view;
	}
	
	private class VideoViewHolder {
		TextView title;
		ImageView image;
	}

}