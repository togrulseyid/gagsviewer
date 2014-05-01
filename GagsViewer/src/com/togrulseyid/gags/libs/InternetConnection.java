/**
 * Check internet connection manualy
 * made against for DNS server not found ERROR.
 */
package com.togrulseyid.gags.libs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Togrul
 * @version 0.3
 * 
 * status :
 * 0 	NO INTERNET CONNECTION
 * 1	MoPub Ads
 * 2	LeadBolt Ads
 * 3
 */
public class InternetConnection {
	private static String theLineSum="";
	private static BufferedReader br ;
	private static InputStreamReader isr ;
	private static InputStream in ;
	private static URL u ;

	public static int checkStatus(String params) {
//		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
		
		
		params = Constants.CONNECTION_URL;
		int status=0;
		try {
			u = new URL(params);
			in = u.openStream();
			isr = new InputStreamReader(in);
			br = new BufferedReader(isr);
//			String theLine="";
//			while ((theLine = br.readLine()) != null) {
//				theLineSum +=theLine;
//			}			
			theLineSum = br.readLine();			
			status = Integer.valueOf(theLineSum);
		} catch (Exception e) {
			return 0;
   		} finally {
   			theLineSum = null;
   			u = null; 
   			in = null;
   			isr = null;
   			br = null;
   		}
		
		return status;
	}
	
	
	
// check Internet status effectively
//	public static int checkStatus(String params, Context context) {
//		
//		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//		boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//		int status=0;
//		
//		
//		if (!isConnected){
//			return 0;
//		} else {
//			params = Constants.CONNECTION_URL;
//			
//			try {
//				u = new URL(params);
//				in = u.openStream();
//				isr = new InputStreamReader(in);
//				br = new BufferedReader(isr);		
//				theLineSum = br.readLine();			
//				status = Integer.valueOf(theLineSum);
//			} catch (Exception e) {
//				return 0;
//	   		} finally {
//	   			theLineSum = null;
//	   			u = null; 
//	   			in = null;
//	   			isr = null;
//	   			br = null;
//	   		}
//		}
//		
//		return status;
//	}
}
