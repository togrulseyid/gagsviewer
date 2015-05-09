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

public class CustomImageAdapter extends BaseAdapter {

	private ArrayList<DataModel> dataModels;
	private DataModel dataModel;
	private LayoutInflater inflater;
	private Typeface typeFace;
	private int loader = R.drawable.loader;
	private Activity activity;

	public CustomImageAdapter(Activity activity, ArrayList<DataModel> gagsList) {
		
		this.activity = activity;
		this.dataModels = gagsList;
		inflater = this.activity.getLayoutInflater();
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
	public long getItemId(int itemId) {
		return itemId;
	}

	public View getView(int position, View viewConvert, ViewGroup viewGroup) {
		
		View view = viewConvert;
		ImageViewHolder holder = null;

		if (view == null) {

			holder = new ImageViewHolder();
			view = inflater.inflate(R.layout.image_list_item, viewGroup, false);
			holder.textViewTitle = (TextView) view.findViewById(R.id.textViewFragmentImageListItemTitle);
			holder.textViewImage = (ImageView) view.findViewById(R.id.imageViewFragmentImageListItemImage);
			view.setTag(holder);
			
		} else {
			
			holder = (ImageViewHolder) view.getTag();
			
		}

		dataModel = getItem(position);
		if (dataModel.getAlt() != null) {

			view.setVisibility(View.VISIBLE);
			
			holder.textViewTitle.setTypeface(typeFace);// TTF
			holder.textViewTitle.setText(dataModel.getAlt());

			try {
				Picasso.with(activity).load(dataModel.getSrc())//.fit()
						.placeholder(loader).error(loader)
						.into(holder.textViewImage);
				
			} catch (Exception e) {
				holder.textViewImage.setImageResource(R.drawable.untitled);
			}

		} else {
			view.setVisibility(View.GONE);
		}
		
		return view;
	}

	private static class ImageViewHolder {
		public TextView textViewTitle;
		public ImageView textViewImage;
	}
}