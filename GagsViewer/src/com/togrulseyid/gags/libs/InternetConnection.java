/**
 * Check internet connection manualy
 * made against for DNS server not found ERROR.
 */
package com.togrulseyid.gags.libs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.togrulseyid.gags.constants.UrlConstants;

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
		
		params = UrlConstants.CONNECTION_URL;
		int status=0;
		try {
			u = new URL(params);
			in = u.openStream();
			isr = new InputStreamReader(in);
			br = new BufferedReader(isr);
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
}
