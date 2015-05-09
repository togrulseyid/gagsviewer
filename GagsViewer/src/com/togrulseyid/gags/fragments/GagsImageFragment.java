package com.togrulseyid.gags.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.togrulseyid.gags.constants.BusinessConstants;
import com.togrulseyid.gags.enums.MessagesEnum;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.adapters.CustomImageAdapter;
import com.togrulseyid.gags.models.CoreModel;
import com.togrulseyid.gags.models.DataModel;
import com.togrulseyid.gags.models.DataModelArrayList;
import com.togrulseyid.gags.operations.NetworkOperations;
import com.togrulseyid.gags.viewer.R;
import com.togrulseyid.gags.viewers.ImageViewer;

public class GagsImageFragment extends Fragment {

	private ArrayList<DataModel> dataModels;
	private int postId;
	private Activity activity;
	private Intent intent;
	private CustomImageAdapter adapter;
	private PullToRefreshListView listView;
	private ActionBar actionBar;
	private LoadImageAsynTask asynTask;
	private TextView newsComplexViewTapToRefresh;
	private TextView textViewChannelFeedNothing;
	private SharedPreferences sharedPref;

	public static GagsImageFragment newInstance(String content) {
		GagsImageFragment fragment = new GagsImageFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();

		actionBar = ((ActionBarActivity)activity).getSupportActionBar();
	}

	@Override
	public void onDestroy() {
		if (postId > 1) {
			saveData(postId - 1);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		postId = getData();
		Log.d("testA", "postId: " + postId);

		View view = inflater.inflate(R.layout.fragment_image, container, false);
		
		listView = (PullToRefreshListView) view
				.findViewById(R.id.listViewImageFragmentList);

		newsComplexViewTapToRefresh = (TextView) view
				.findViewById(R.id.textViewChannelFeedTapToRefresh);

		textViewChannelFeedNothing = (TextView) view
				.findViewById(R.id.textViewChannelFeedNothing);

		newsComplexViewTapToRefresh
				.setOnClickListener(new RefreshClickListener());

		listView.setOnScrollListener(new MyOnScrollListener(actionBar));

		if (dataModels == null) {
			dataModels = new ArrayList<DataModel>();
		}

		adapter = new CustomImageAdapter(getActivity(), dataModels);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new CustomItemOnItemClickListener());
		listView.setOnRefreshListener(new CustomOnRefreshListener());

		refreshList(true);

		return view;
	}

	private class LoadImageAsynTask extends
			AsyncTask<Integer, Void, DataModelArrayList> {

		private boolean isUp;

		public LoadImageAsynTask(boolean isUp) {
			this.isUp = isUp;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected DataModelArrayList doInBackground(Integer... params) {
			NetworkOperations networkOperations = new NetworkOperations(
					getActivity());
			return networkOperations.getImageList(new CoreModel(params[0]));
		}

		@Override
		protected void onPostExecute(DataModelArrayList result) {
			super.onPostExecute(result);

			if (isAdded() && !isCancelled()) {
				
				Log.d("testAB","result: " +  result);
				if (result != null) {
					
					if (result.getMessageId() != null
							&& result.getMessageId().equals(
									MessagesEnum.SUCCESSFUL.getCode())) {

						if (result.getList() != null) {
				
							if (isUp) {
								dataModels.clear();
							}

							for (DataModel imageModel : result.getList()) {
								dataModels.add(imageModel);
							}

						}

						adapter.notifyDataSetChanged();
						listView.onRefreshComplete();
						listView.setMode(Mode.PULL_FROM_END);

					} else if (result.getMessageId() != null
							&& (result.getMessageId().equals(
									MessagesEnum.NO_INTERNET_CONNECTION
											.getCode()) || result
									.getMessageId().equals(
											MessagesEnum.NO_NETWORK_CONNECTION
													.getCode()))) {
						isVisibleTapToRefreshView(true);

					}else {
						listView.onRefreshComplete();
						listView.setMode(Mode.PULL_FROM_END);
					}

					postId++;
				} else {
					listView.onRefreshComplete();
					listView.setMode(Mode.PULL_FROM_END);
					isVisibleTapToRefreshView(true);
				}
				
			}
		}

	}

	public class CustomItemOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			try {

				intent = new Intent(activity, ImageViewer.class);
				intent.putExtra(Constants.TAG_SRC, dataModels.get(position - 1).getSrc());
				intent.putExtra(Constants.TAG_ALT, dataModels.get(position - 1).getAlt());
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(activity, e.getMessage(), BusinessConstants.TOAST_TIME).show();
			}
		}

	}

	private void saveData(int id) {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.image_id), id);
		editor.commit();
	}

	private int getData() {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt(getString(R.string.image_id), 1);
	}

	private class MyOnScrollListener implements OnScrollListener,
			OnScrollChangedListener {

		private ActionBar actionBar;
		private int mLastFirstVisibleItem = 0;

		public MyOnScrollListener(ActionBar actionBar) {
			this.actionBar = actionBar;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			final int currentFirstVisibleItem = listView.getRefreshableView()
					.getFirstVisiblePosition();

			if (currentFirstVisibleItem > mLastFirstVisibleItem) {
				actionBar.hide();
			} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
				actionBar.show();
			}

			mLastFirstVisibleItem = currentFirstVisibleItem;

		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScrollChanged() {
		}

	}

	private class RefreshClickListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.textViewChannelFeedTapToRefresh) {
				isVisibleTapToRefreshView(false);
				refreshList(true);
			}
		}
	}

	private void refreshList(boolean isUp) {
		setMenuVisibility(false);

		if (dataModels != null && dataModels.size() == 0) {

			dataModels.add(new DataModel());
			listView.setMode(Mode.DISABLED);
			adapter.notifyDataSetChanged();

		}

		listView.setRefreshing(false);

		asynTask = new LoadImageAsynTask(isUp);
		asynTask.execute(postId);

	}

	private void isVisibleTapToRefreshView(boolean isVisible) {
		if (isVisible) {
			newsComplexViewTapToRefresh.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			newsComplexViewTapToRefresh.setVisibility(View.GONE);
			textViewChannelFeedNothing.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}

	private class CustomOnRefreshListener implements
			OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

			refreshList(true);

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

			refreshList(false);

		}

	}

}
