package com.togrulseyid.gags.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.togrulseyid.gags.models.DataModel;
import com.togrulseyid.gags.viewer.R;

public class CustomJokeAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private Typeface typeFace;
	private Activity activity;
	private ArrayList<DataModel> dataModes;

	public CustomJokeAdapter(Activity activity, ArrayList<DataModel> dataModes) {
	
		this.activity = activity;
		this.dataModes = dataModes;
		inflater = (LayoutInflater) this.activity.getLayoutInflater();
		typeFace = Typeface.createFromAsset(this.activity.getAssets(), "ttf/angrybirds_regular.ttf");
	
	}

	@Override
	public int getCount() {
		return dataModes.size();
	}

	@Override
	public DataModel getItem(int position) {
		return dataModes.get(position);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder = null;

		if (view == null) {
			
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.jokes_list_item, viewGroup, false);
			holder.textViewTitle = (TextView) view.findViewById(R.id.jokes_list_item_title);
			view.setTag(holder);
			
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		if(getItem(position).getTitle() != null) {
			
			view.setVisibility(View.VISIBLE);
			holder.textViewTitle.setTypeface(typeFace);// TTF
			holder.textViewTitle.setText(Html.fromHtml(getItem(position).getTitle()));
		
		} else {
			view.setVisibility(View.GONE);
		}

		return view;
	}

	private static class ViewHolder {
		TextView textViewTitle;
	}

}
