package com.togrulseyid.gags.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;

import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.BannerAdListener;
import com.togrulseyid.gags.library.TabPageIndicator;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.ImageLoader;
import com.togrulseyid.gags.libs.adapters.GagsViewerInnerAdapter;
import com.togrulseyid.gags.viewer.R;


public class GagsViewerActivity extends ActionBarActivity {

	private MoPubView moPubView;
	private ViewPager pager;
	private TabPageIndicator indicator;
	private ImageLoader imageLoader;
	private SubMenu subMenu;
	private Menu menu;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
    	
		setContentView(R.layout.simple_tabs);

		progressBar = (ProgressBar) findViewById(R.id.mainScreenProgresBar);
		moPubView = (MoPubView) findViewById(R.id.MopubAdView);

		pager = (ViewPager) findViewById(R.id.pager);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);

		try {
			new LoadContentTask().execute(getSupportFragmentManager());
		} catch (Exception e) {

		}
	}

	@Override
	public void onDestroy() {
		if (moPubView != null)
			moPubView.destroy();

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (pager == null) {
			try {
				new LoadContentTask().execute(getSupportFragmentManager());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private class LoadContentTask extends
			AsyncTask<FragmentManager, Object, FragmentPagerAdapter> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected FragmentPagerAdapter doInBackground(FragmentManager... params) {
			return new GagsViewerInnerAdapter(params[0], moPubView);
		}

		@Override
		protected void onPostExecute(FragmentPagerAdapter adapter) {
			progressBar.setVisibility(View.GONE);
			pager.setAdapter(adapter);
			indicator.setViewPager(pager);
			pager.setOffscreenPageLimit(4);
			pager.setPageMargin(25);

			try {
				moPubView.setAdUnitId(Constants.MOPUB_ID);
				moPubView.loadAd();

				moPubView.setBannerAdListener(new BannerAdListener() {
					@Override
					public void onBannerLoaded(MoPubView banner) {
						moPubView.setVisibility(View.VISIBLE);
					}

					@Override
					public void onBannerFailed(MoPubView banner,
							MoPubErrorCode errorCode) {
						moPubView.setVisibility(View.GONE);
					}

					@Override
					public void onBannerExpanded(MoPubView banner) {
					}

					@Override
					public void onBannerCollapsed(MoPubView banner) {
					}

					@Override
					public void onBannerClicked(MoPubView banner) {
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_clear_cache:
			imageLoader = new ImageLoader(getApplicationContext());
			imageLoader.clearCache();
			break;

		case R.id.menu_about:

			startActivity(new Intent(this, GagsInfoActivity.class));
			
			break;

		case R.id.menu_exit:
			finish();
			break;
			
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;

		default:
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constants.DEVICE_VERSION < Constants.DEVICE_HONEYCOMB) {
			if (event.getAction() == KeyEvent.ACTION_UP
					&& keyCode == KeyEvent.KEYCODE_MENU) {
				menu.performIdentifierAction(subMenu.getItem().getItemId(), 0);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

}