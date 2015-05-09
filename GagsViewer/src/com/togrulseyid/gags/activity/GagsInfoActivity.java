package com.togrulseyid.gags.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.togrulseyid.gags.viewer.R;

/**
 * كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۖ ثُمَّ إِلَيْنَا تُرْجَعُونَ
 * */

public class GagsInfoActivity extends ActionBarActivity {

	private TextView textViewAppName;
	private TextView textViewAboutUS;
	private TextView textViewVersion;
	private Typeface myFont1;
	private Typeface myFont2;
	private Typeface myFont3;
	private PackageInfo info;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gags_linear);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);


		myFont1 = Typeface.createFromAsset(getAssets(), "ttf/angrybirds_regular.ttf");
		myFont2 = Typeface.createFromAsset(getAssets(), "ttf/chicken_butt.ttf");
		myFont3 = Typeface.createFromAsset(getAssets(), "ttf/tequilla_sunrise.ttf");

		textViewAppName = (TextView) findViewById(R.id.textViewAppName);
		textViewAboutUS = (TextView) findViewById(R.id.textViewAboutUS);
		textViewVersion = (TextView) findViewById(R.id.textViewVersion);

		textViewAboutUS.setTypeface(myFont1);
		textViewAppName.setTypeface(myFont2);
		textViewVersion.setTypeface(myFont3);

		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		textViewVersion.setText(String.format(
				getString(R.string._STRING_FORMAT_VERSION_NAME),
				info.versionName));

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
