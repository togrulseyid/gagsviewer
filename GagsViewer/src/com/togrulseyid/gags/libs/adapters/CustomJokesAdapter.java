package com.togrulseyid.gags.libs.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.viewer.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomJokesAdapter extends BaseAdapter {
	
	private ArrayList<HashMap<String, String>> gagsList;
	private LayoutInflater inflater;
	private Typeface myFont1;

	public CustomJokesAdapter(Context context, ArrayList<HashMap<String, String>> gagsList) {
		this.gagsList = gagsList;
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

	@SuppressLint("NewApi")
	public View getView(int position, View view, ViewGroup viewGroup) {
		View v = view;
		JokeViewHolder holder = null;

		if (view == null) {
			holder = new JokeViewHolder();
			v = inflater.inflate(R.layout.jokes_list_item, null);
			holder.jokes_list_item_title = (TextView) v.findViewById(R.id.jokes_list_item_title);
			v.setTag(holder);
		} else
			holder = (JokeViewHolder) v.getTag();

		holder.jokes_list_item_title.setTypeface(myFont1);// TTF
//		holder.jokes_list_item_title.setText(getItem(position).get(Constants.TAG_TITLE));
		holder.jokes_list_item_title.setText(Html.fromHtml(getItem(position).get(Constants.TAG_TITLE)));
		return v;
	}

	private static class JokeViewHolder {
		TextView jokes_list_item_title;
	}

}
