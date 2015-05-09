package com.togrulseyid.gags.operations;

import android.content.Context;

import com.togrulseyid.gags.models.CoreModel;
import com.togrulseyid.gags.viewer.R;

public class SPProvider {

	public static Object initializeObject(Object object, Context context) {

		CoreModel model = null;
		if (object instanceof CoreModel) {
			model = (CoreModel) object;
		}

		if (model != null) {

//			SharedPreferences sharedPreferences = context.getSharedPreferences(
//					context.getResources().getString(R.string._SP_MOBCHANNEL),
//					Context.MODE_PRIVATE);
			
			model.setAppVersion(context.getResources().getString(
					R.string._APP_VERSION));
			
			
			model.setSysLang("en");
			
//			model.setAppSignature(Utility.getAppSignature(context));

		}

		if (model != null) {
			return model;
		} else {
			return object;
		}

	}

	public static Object initializePostDescModel(Object object, Context context) {

		CoreModel model = null;
		if (object instanceof CoreModel) {
			model = (CoreModel) object;
		}

		if (model != null) {

//			SharedPreferences sharedPreferences = context.getSharedPreferences(
//					context.getResources().getString(R.string._SP_MOBCHANNEL),
//					Context.MODE_PRIVATE);
			
			model.setAppVersion(context.getResources().getString(
					R.string._APP_VERSION));
			
			model.setSysLang("en");
//			model.setAppSignature(Utility.getAppSignature(context));
			
		}

		if (model != null) {
			return model;
		} else {
			return object;
		}

	}

}
