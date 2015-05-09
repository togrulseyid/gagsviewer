package com.togrulseyid.gags.activity;

import com.togrulseyid.gags.viewer.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		setContentView(R.layout.activity_settings);
		
		
		
		
		
		
		
	}
}
