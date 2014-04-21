package com.togrulseyid.gags.main;

import com.actionbarsherlock.app.SherlockActivity;
import com.togrulseyid.gags.viewer.R;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends SherlockActivity {

	private ImageView splash;
	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		splash = (ImageView) findViewById(R.id.splash);

		animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
		animation.reset();
		splash.clearAnimation();
		splash.startAnimation(animation);

		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				Intent GagsViewerActivity = new Intent("com.togrulseyid.gags.main.GAGSVIEWER");
				startActivity(GagsViewerActivity);
				finish();
			}
		});
	}
}