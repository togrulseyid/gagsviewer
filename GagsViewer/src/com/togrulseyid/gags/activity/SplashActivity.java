package com.togrulseyid.gags.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.togrulseyid.gags.viewer.R;

public class SplashActivity extends ActionBarActivity {

	private ImageView splash;
	private Animation animation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
				Intent intent = new Intent(getApplicationContext(), GagsViewerActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}