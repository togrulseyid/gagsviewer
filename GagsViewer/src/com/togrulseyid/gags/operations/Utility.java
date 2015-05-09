package com.togrulseyid.gags.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.togrulseyid.gags.constants.BusinessConstants;

/**
 * Utility is very useful functions collection which contains null checkers,
 * some local operartions and etc.
 * 
 * @author Toghrul Seyidov
 * @version 1.9, 3 Mar 2015
 * @since 1.0
 */
public class Utility {

	public static final boolean isEmptyOrNull(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}

		return false;
	}

	public static final boolean isZeroOrNull(Integer str) {
		if (str == null || str.equals(0)) {
			return true;
		}

		return false;
	}

	public static String getMD5EncodedString(String string) {

		byte[] data = string.getBytes();
		String result = null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = new BigInteger(1, md.digest(data)).toString(16);
		} catch (NoSuchAlgorithmException e) {
		}

		return result;
	}

	public static final String getAppSignature(Context context) {

		String appSignature = new BigInteger(1, getApkMd5(context))
				.toString(16);

		if (appSignature != null && appSignature.length() < 32) {
			appSignature = "0" + appSignature;
		}

		// return appSignature;
		return "IDoNotKnow!";
	}

	private static final byte[] getApkMd5(Context context) {

		try {
			Signature[] signatures = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_SIGNATURES).signatures;

			Signature signature = signatures[0];
			byte[] hexBytes = signature.toByteArray();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return digest.digest(hexBytes);
		} catch (NameNotFoundException ex) {

		} catch (Exception e) {

		}
		return null;
	}

	/*
	 * Check network connection (it does not depend WiFi or Cell Network)
	 */
	public static final boolean checkNetwork(Context context) {

		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo == null) {
			return false;
		}

		return networkInfo.isConnected();
	}

	/*
	 * 
	 */
	public static final boolean checkInternetConnection() {

		HttpParams httpParameters = new BasicHttpParams();
		// if HTTP connection time is less than
		// BusinessConstants.INTERNET_CHEKING_TIMEOUT seconds, will be
		// IOException
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				BusinessConstants.INTERNET_CHEKING_TIMEOUT);
		// if HTTP downloading time is less than
		// BusinessConstants.INTERNET_CHEKING_TIMEOUT seconds, will be
		// IOException
		HttpConnectionParams.setSoTimeout(httpParameters,
				BusinessConstants.INTERNET_CHEKING_TIMEOUT);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

		try {

			// Developer thought that if Google down, down all servers in the
			// world :)
			HttpGet httpGet = new HttpGet("https://www.google.com");

			HttpResponse response = httpClient.execute(httpGet);

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				if (sb.length() > 0) {
					return true;
				} else {
					return false;
				}
			} catch (Exception ex) {
				return false;
			}

		} catch (ClientProtocolException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	public static String encrypt(String strToEncrypt, String key) {

//		try {
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//			final SecretKeySpec secretKeySpec = new SecretKeySpec(
//					key.getBytes(), "AES");
//			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//
//			final String encryptedString = Base64.encodeToString(
//					cipher.doFinal(strToEncrypt.getBytes()), Base64.DEFAULT);
//
//			return encryptedString;
//		} catch (Exception e) {
//		}
//		return null;
		return strToEncrypt;

	}

	public static String decrypt(String strToDecrypt, String key) {
//		try {
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
//			final SecretKeySpec secretKeySpec = new SecretKeySpec(
//					key.getBytes(), "AES");
//			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//			final String decryptedString = new String(cipher.doFinal(Base64
//					.decode(strToDecrypt.getBytes(), Base64.DEFAULT)));
//			return decryptedString;
//		} catch (Exception e) {
//		}
//		return null;
		return strToDecrypt;
	}


	public static void hideKeyBoard(EditText editText, Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * Hides the soft keyboard
	 */
	public static void hideSoftKeyboard(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);
		}
	}


	@SuppressWarnings("deprecation")
	public static void setRelativeLayoutBackground(
			RelativeLayout relativeLayout, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= 16) {
			relativeLayout.setBackground(drawable);
		} else {
			relativeLayout.setBackgroundDrawable(drawable);
		}
	}

	@SuppressWarnings("deprecation")
	public static void setViewBackground(View view, Drawable drawable) {
		if (Build.VERSION.SDK_INT >= 16) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
	}

	/*
	 * Load Resource String by Id
	 */
	public static int loadStringResourcesByString(Context context, String title) {
		return context.getResources().getIdentifier(title, "string",
				context.getPackageName());
	}

	/*
	 * Load Resource String by Id
	 */
	public static int loadColorResourcesByString(Context context, String title) {
		return context.getResources().getIdentifier(title, "color",
				context.getPackageName());
	}

	public static String getDeviceDefaultEmail(Activity activity) {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(activity).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				String possibleEmail = account.name;
				return possibleEmail;
			}
		}
		return null;
	}


	public static int getDrawableIdentifier(Activity activity, String name) {
		return activity.getResources().getIdentifier(name, "drawable",
				activity.getPackageName());
	}

	public static int getStringIdentifier(Activity activity, String name) {
		return activity.getResources().getIdentifier(name, "string",
				activity.getPackageName());
	}

	
	public static void shareData(Activity activity, String title, String subject, String text,
			String url, String dialogTitle) {
		if (title != null) {
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			if (title != null) {
				sendIntent.putExtra(Intent.EXTRA_TITLE, title);
			}
			if (subject != null) {
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			}
			if (text != null) {
				sendIntent.putExtra(Intent.EXTRA_TEXT, text);
			}
			sendIntent.setType("text/plain");
			
			if (url != null) {
				sendIntent.putExtra(Intent.EXTRA_STREAM, url);
				shareIntent.setType("image/jpeg");
			}
			
			activity.startActivity(Intent
					.createChooser(sendIntent, dialogTitle));
		}
	}

}
