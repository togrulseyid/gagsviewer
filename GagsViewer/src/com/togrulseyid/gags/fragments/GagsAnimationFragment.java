package com.togrulseyid.gags.fragments;

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
import com.togrulseyid.gags.enums.MessagesEnum;
import com.togrulseyid.gags.constants.Constants;
import com.togrulseyid.gags.adapters.CustomAnimationAdapter;
import com.togrulseyid.gags.models.CoreModel;
import com.togrulseyid.gags.models.DataModel;
import com.togrulseyid.gags.models.DataModelArrayList;
import com.togrulseyid.gags.operations.NetworkOperations;
import com.togrulseyid.gags.viewer.R;
import com.togrulseyid.gags.viewers.AnimationViewer;

import java.util.ArrayList;

public class GagsAnimationFragment extends Fragment {
	
	private ArrayList<DataModel> dataModels;
	private int postId;
	private Context context;
	private Intent intent;
	private CustomAnimationAdapter adapter;
	private PullToRefreshListView listView;
	private ActionBar actionBar;
	private LoadAnimationAsyncTask asynTask;
	private TextView newsComplexViewTapToRefresh;
	private TextView textViewAnimationNothing;
	private SharedPreferences sharedPref;
	
    public static GagsAnimationFragment newInstance() {
        return new GagsAnimationFragment();
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		sharedPref = getActivity().getSharedPreferences(getString(R.string._SP_GAGS_VIEWER), Context.MODE_PRIVATE);
		actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
	}

	@Override
	public void onDestroy() {
		if (postId > 1) {
			saveData(postId - 1);
		}
		super.onDestroy();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		postId = getData();
		Log.d("testA", "postId: " + postId);

		View view = inflater.inflate(R.layout.fragment_animation, container, false);
		
		listView = (PullToRefreshListView) view
				.findViewById(R.id.listViewAnimationFragmentList);

		newsComplexViewTapToRefresh = (TextView) view
				.findViewById(R.id.textViewAnimationTapToRefresh);

		textViewAnimationNothing = (TextView) view
				.findViewById(R.id.textViewAnimationNothing);

		newsComplexViewTapToRefresh
				.setOnClickListener(new RefreshClickListener());

		listView.setOnScrollListener(new MyOnScrollListener(actionBar));

		if (dataModels == null) {
			dataModels = new ArrayList<>();
		}

		adapter = new CustomAnimationAdapter(getActivity(), dataModels);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new CustomItemOnItemClickListener());
		listView.setOnRefreshListener(new CustomOnRefreshListener());

		refreshList(true);

		return view;
    }
	
	public void saveData(int postId) {
		sharedPref.edit().putInt(getString(R.string.animation_id), postId).commit();
	}
	
	public int getData() {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		return sharedPref.getInt(getString(R.string.animation_id), 1);
	}

	private class LoadAnimationAsyncTask extends
			AsyncTask<Integer, Void, DataModelArrayList> {

		private boolean isUp;

		public LoadAnimationAsyncTask(boolean isUp) {
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
			return networkOperations.getAnimationList(new CoreModel(params[0]));
		}

		@Override
		protected void onPostExecute(DataModelArrayList result) {
			super.onPostExecute(result);

			if (isAdded() && !isCancelled()) {

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

				intent = new Intent(context, AnimationViewer.class);
				intent.putExtra(Constants.TAG_ALT, dataModels.get(position - 1).getAlt());
				intent.putExtra(Constants.TAG_SRC, dataModels.get(position - 1).getSrc());
				startActivity(intent);
				
			} catch (Exception e) {
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
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
			if (view.getId() == R.id.textViewAnimationTapToRefresh) {
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

		asynTask = new LoadAnimationAsyncTask(isUp);
		asynTask.execute(postId);

	}

	private void isVisibleTapToRefreshView(boolean isVisible) {
		if (isVisible) {
			newsComplexViewTapToRefresh.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			newsComplexViewTapToRefresh.setVisibility(View.GONE);
			textViewAnimationNothing.setVisibility(View.GONE);
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