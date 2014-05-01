//package com.togrul.seyid.gags.libs.adapters;
//
//import android.annotation.SuppressLint;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.util.Log;
//
//import com.togrulseyid.gags.fragments.GagsAnimationFragment;
//import com.togrulseyid.gags.fragments.GagsInfoFragment;
//import com.togrulseyid.gags.fragments.GagsImageFragment;
//import com.togrulseyid.gags.fragments.GagsJokesFragment;
//import com.togrulseyid.gags.fragments.GagsVideoFragment;
//import com.togrulseyid.gags.library.IconPagerAdapter;
//import com.togrulseyid.gags.viewer.R;
//
///**
// * كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۖ ثُمَّ إِلَيْنَا تُرْجَعُونَ
// * */
//
//public class GagsViewerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
//	private static final String[] CONTENT = new String[] { 
//			 "Images" ,
//			 "Animation",
//			 "Videos",
//			 "Jokes",
//			 "About Us",
//			 };//Animation Animate
//	private static final int[] ICONS = new int[] {
//			 R.drawable.perm_group_images, 
//			 R.drawable.perm_group_gifs,
//			 R.drawable.perm_group_videos,
//			 R.drawable.perm_group_jokes,
//			 R.drawable.perm_group_gags,
//			};
//
//	public GagsViewerAdapter(FragmentManager fm) {
//		super(fm);
//	}
//
//	Fragment frg = new Fragment();
//	
//	@Override
//	public Fragment getItem(int position) {
//		switch (position) {
//			case 0:
//				 frg = GagsImageFragment.newInstance(CONTENT[position % CONTENT.length]);
//				break;
//			case 1:
//				 frg = GagsAnimationFragment.newInstance(CONTENT[position % CONTENT.length]);
//				break;
//			 case 2:
//				  frg = GagsVideoFragment.newInstance(CONTENT[position % CONTENT.length]);
//			 break;
//			 case 3:
//				 frg = GagsJokesFragment.newInstance(CONTENT[position % CONTENT.length]);
//			 break;
//			 case 4:
//				 frg = GagsInfoFragment.newInstance(CONTENT[position % CONTENT.length]);
//			 break;
//			default:
//				break;
//		}
//	
//		return frg;
//	}
//
//	@SuppressLint("DefaultLocale")
//	@Override
//	public CharSequence getPageTitle(int position) {
//		Log.d("CONTENT",CONTENT[position % CONTENT.length].toUpperCase());
//		return CONTENT[position % CONTENT.length].toUpperCase();
//	}
//
//	@Override
//	public int getIconResId(int index) {
//		return ICONS[index];
//	}
//
//	@Override
//	public int getCount() {
//		return CONTENT.length;
//	}
//}