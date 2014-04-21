package com.togrulseyid.gags.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.togrulseyid.gags.viewer.R;

/**
 * كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۖ ثُمَّ إِلَيْنَا تُرْجَعُونَ
 * */

@SuppressLint("NewApi")
public class GagsInfoFragment extends SherlockFragment{
	public View view;

	FragmentTransaction ft;
	
	TextView app_name ;
	TextView aboutUS ;
	TextView version ;
	
	Context context ;
	
	Typeface myFont1 ;
	Typeface myFont2 ;
	Typeface myFont3 ;
	
	private static final String KEY_CONTENT = "GagsFragment:Content";
	public String mContent = "???";

	  public static GagsInfoFragment newInstance(String content) {
		  GagsInfoFragment fragment = new GagsInfoFragment();
		  fragment.mContent = content.toString();/////////////////
		  return fragment;
		}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		myFont1 = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "ttf/angrybirds_regular.ttf");
		myFont2 = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "ttf/chicken_butt.ttf");
		myFont3 = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "ttf/tequilla_sunrise.ttf");

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e("On Config Change", "LANDSCAPE");
		} else {
			Log.e("On Config Change", "PORTRAIT");
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		view = inflater.inflate(R.layout.gags_linear, container, false);
		app_name = (TextView) view.findViewById(R.id.app_name);
		aboutUS  = (TextView) view.findViewById(R.id.aboutUS);
		version  = (TextView) view.findViewById(R.id.version);
		
		aboutUS.setTypeface(myFont1);
		app_name.setTypeface(myFont2);
		version.setTypeface(myFont3);

		
		PackageInfo pInfo = null;
		try {
			pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		version.setText("Version "+pInfo.versionName);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}
	
}
