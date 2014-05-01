package mysql;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonArrayCreater {
	public static final String TAG_SRC = "src";
	public static final String TAG_ALT = "alt";

	private ArrayList<HashMap<String, String>> igagsListTemp;
	private JSONArray jsonArray;
	private HashMap<String, String> map;
	private String src, alt;
	
	/*
	 * Convert JSON String to ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> getGagListFromString(String myJsonString) {
		igagsListTemp = new ArrayList<HashMap<String, String>>();
		try {
			jsonArray = new JSONArray(myJsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				src = jsonObject.getString(TAG_SRC);
				alt = jsonObject.getString(TAG_ALT);

				System.out.println(src + "\n" + alt + "\n");
				map = new HashMap<String, String>(); 
				map.put(TAG_SRC, src);
				map.put(TAG_ALT, md5(src+ alt));
				igagsListTemp.add(map);
			}
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return igagsListTemp;
	}

	private String md5(String string) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		 byte[] bytesOfMessage = string.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytesOfMessage);
		return thedigest.toString();
	}
}
