package com.togrulseyid.gags.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import com.mopub.mobileads.MoPubView;
import com.togrulseyid.gags.fragments.GagsAnimationFragment;
import com.togrulseyid.gags.fragments.GagsImageFragment;
import com.togrulseyid.gags.fragments.GagsJokesFragment;
import com.togrulseyid.gags.fragments.GagsVideoFragment;
import com.togrulseyid.gags.library.IconPagerAdapter;
import com.togrulseyid.gags.viewer.R;

public class GagsViewerInnerAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	
	private static final String[] CONTENT = new String[] { "Images",
			"Animation", "Videos", "Jokes",};
	
	private static final int[] ICONS = new int[] {
			R.drawable.perm_group_images, R.drawable.perm_group_gifs,
			R.drawable.perm_group_videos, R.drawable.perm_group_jokes, };

	public GagsViewerInnerAdapter(FragmentManager fm ){
		super(fm);
	}

	private MoPubView moPubView;

	public GagsViewerInnerAdapter(FragmentManager fm, MoPubView moPubView) {
		super(fm);
		this.moPubView = moPubView;
	}

	Fragment frg = new Fragment();

	@Override
	public Fragment getItem(int position) {
		switch (position) {

		case 0:
			moPubView.setVisibility(View.VISIBLE);
			frg = GagsImageFragment.newInstance();//CONTENT[position % CONTENT.length]
			break;

		case 1:
			moPubView.setVisibility(View.VISIBLE);
			frg = GagsAnimationFragment.newInstance();
			break;

		case 2:
			moPubView.setVisibility(View.VISIBLE);
			frg = GagsVideoFragment.newInstance();
			break;

		case 3:
			moPubView.setVisibility(View.VISIBLE);
			frg = GagsJokesFragment.newInstance();
			break;

		default:
			break;

		}

		return frg;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Log.d("CONTENT", CONTENT[position % CONTENT.length].toUpperCase());
		return CONTENT[position % CONTENT.length].toUpperCase();
	}

	@Override
	public int getIconResId(int index) {
		return ICONS[index];
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}

}
