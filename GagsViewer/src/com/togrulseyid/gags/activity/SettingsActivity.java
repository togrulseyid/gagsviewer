package com.togrulseyid.gags.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.togrulseyid.gags.dialogs.ConfirmDialog;
import com.togrulseyid.gags.enums.LanguageCodeEnum;
import com.togrulseyid.gags.operations.Utility;
import com.togrulseyid.gags.viewer.R;

public class SettingsActivity extends ActionBarActivity {

	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		setContentView(R.layout.activity_settings);

		sharedPreferences = getSharedPreferences(getString(R.string._SP_GAGS_VIEWER), MODE_PRIVATE);

	}

	private ConfirmDialog confirmDialog;
	private View viewPostLang;
	public void postLanguageClick(View view) {

		viewPostLang = getLayoutInflater().inflate(
				R.layout.dialog_change_post_language, null);

		confirmDialog = new ConfirmDialog(getApplicationContext(), getResources()
				.getString(R.string.title_change_post_language),
				viewPostLang);
		confirmDialog.show();
		confirmDialog.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				if (view.getId() == R.id.buttonDialogConfirmOK) {
					onPostLangChangeListener(viewPostLang, confirmDialog);
				}
			}
		});

	}


	public void clearHistory(View view) {
		sharedPreferences.edit().clear().commit();
	}

	public void shareApp(View view) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		sharingIntent
				.putExtra(
						android.content.Intent.EXTRA_TEXT,
						getString(R.string.text_view_settings_share_intent_text));
		startActivity(Intent.createChooser(sharingIntent,
				getString(R.string.title_intent_send_to)));

	}

	public void rateApp(View view) {

		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
		}
	}

	private void onPostLangChangeListener(View view,
										  ConfirmDialog dialogFragment) {
		String langId;
		CheckBox checkableFrameLayoutLangAz = (CheckBox) view
				.findViewById(R.id.checkBoxSettingsPostLangAz);
		CheckBox checkableFrameLayoutLangEn = (CheckBox) view
				.findViewById(R.id.checkBoxSettingsPostLangEn);
		CheckBox checkableFrameLayoutLangRu = (CheckBox) view
				.findViewById(R.id.checkBoxSettingsPostLangRu);
		CheckBox checkableFrameLayoutLangTr = (CheckBox) view
				.findViewById(R.id.checkBoxSettingsPostLangTr);

		langId = "";
		if (checkableFrameLayoutLangAz.isChecked()) {
			langId += "," + LanguageCodeEnum.AZERBAIJAN.getCode();
		}
		if (checkableFrameLayoutLangEn.isChecked()) {
			langId += "," + LanguageCodeEnum.ENGLISH.getCode();
		}
		if (checkableFrameLayoutLangTr.isChecked()) {
			langId += "," + LanguageCodeEnum.TURKISH.getCode();
		}
		if (checkableFrameLayoutLangRu.isChecked()) {
			langId += "," + LanguageCodeEnum.RUSSIAN.getCode();
		}

		if (!Utility.isEmptyOrNull(langId)) {

			langId = langId.substring(1);

			sharedPreferences.edit()
					.putString(getString(R.string._SP_POSTS_LANG), langId)
					.commit();
			dialogFragment.dismiss();
		} else {
			Toast.makeText(this,
					getString(R.string.message_error_no_lang_choosen),
					Toast.LENGTH_SHORT).show();
		}
	}
}
