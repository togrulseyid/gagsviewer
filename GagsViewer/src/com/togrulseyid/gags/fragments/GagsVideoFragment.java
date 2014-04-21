package com.togrulseyid.gags.fragments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.togrulseyid.gags.libs.ConnectionException;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.InternetConnection;
import com.togrulseyid.gags.libs.adapters.CustomVideoAdapter;
import com.togrulseyid.gags.viewers.VideoViewer;
import com.togrulseyid.gags.viewer.R;

/**
 * كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۖ ثُمَّ إِلَيْنَا تُرْجَعُونَ
 * */

public class GagsVideoFragment extends SherlockListFragment implements OnClickListener {

	// Declare an instance variable for your MoPubView.
//	private MoPubView moPubView;	// MoPub
	private int AddShow = 0;
	
    public static GagsVideoFragment newInstance(String content) {
        GagsVideoFragment fragment = new GagsVideoFragment();
        fragment.vContent = content.toString();
        return fragment;
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		vContext = getActivity();
		
        if ((savedInstanceState != null) && savedInstanceState.containsKey(vKEY_CONTENT)) {
            vContent = savedInstanceState.getString(vKEY_CONTENT);
        }
    }

    
    @SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   		// TODO Auto-generated method stub
	 	if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	 	
	 	
	 	vCurrentURLID = getData();
	 	if(vCurrentURLID !=0 )
	 	{
	 		vCurrentURL = vConsturl + "?url=" + vCurrentURLID;
	 	}

		vView = inflater.inflate(R.layout.videolist, container, false);
		

	 	// men olajam
	 	vGoBackText = (TextView) vView.findViewById(R.id.vGoBackText);

	 	
	 	
		vLayout=  (RelativeLayout) vView.findViewById(R.id.vLayout);
		vLv = (ListView) vView.findViewById(android.R.id.list);
		vProgressBar = (ProgressBar) vView.findViewById(R.id.vProgresBar);	
		vGoBackButton = (Button) vView.findViewById(R.id.vGoBackButton);
		
		//Controller UI 
	    footer = inflater.inflate(R.layout.controller, null);
	    leftCont = (ImageButton) footer.findViewById(R.id.arrow_left);
	    homeCont = (ImageButton) footer.findViewById(R.id.home);
	    rightCont = (ImageButton) footer.findViewById(R.id.arrow_right);	    
	    vLv.addFooterView(footer);
	    
	    leftCont.setOnClickListener(this);
	    homeCont.setOnClickListener(this);
	    rightCont.setOnClickListener(this);

	    
		no_internet_connection2 = (TextView) vView.findViewById(R.id.vNo_internet_connection2);
		checking_internet_connection = (TextView) vView.findViewById(R.id.vChecking_internet_connection);
		no_internet_connection = (ImageView) vView.findViewById(R.id.vNo_internet_connection);
		exit_from_app = (Button) vView.findViewById(R.id.vExit_from_app);
		refresh_internet = (Button) vView.findViewById(R.id.vRefresh_internet);


//		moPubView = (MoPubView) vView.findViewById(R.id. VideoMopubAdView);   /// mopub Ads
		
		
		try {
    		if (!isNetworkAvailable()) {
    			throw new ConnectionException("Please check your internet connection and retry again");
    		}
    		new VideoAsyncTask(false).execute(vCurrentURL);// Execute VideoAsyncTask and update ListView 
    		
    	} catch (ConnectionException ce) {
    		checkInternetStatus(ce.getMessage());
    	}
		
        return vView;
    }    
       
    
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		try{
			i = new Intent(vContext, VideoViewer.class);
			i.putExtra( Constants.TAG_URL, vGagsList.get(position).get( Constants.TAG_URL ));
			i.putExtra( Constants.TAG_ALT, vGagsList.get(position).get( Constants.TAG_ALT ));		
			startActivity(i);
		}catch (Exception e){
			Toast.makeText(vContext, e.getMessage(), vTOAST_TIME).show();
		}
    }
  	
	
	private void checkInternetStatus(String msg) {
		
		refresh_internet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
	    		try {
	    			refresh_internet.setText(refreshInternetText);
	    			no_internet_connection2.setText(noNnternetConnection2Text);
	    			checking_internet_connection.setText(checkingInternetConnection);
	    			
	    			if (!isNetworkAvailable()) {
	    				throw new ConnectionException("Please check your internet connection and retry again");
	    			}
	    			new VideoAsyncTask(false).execute(vCurrentURL);
	    			
	    		} catch (ConnectionException ce) {
	    			checkInternetStatus(ce.getMessage());
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
			}
		});
		
		exit_from_app.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.exit(-1);
			}
		});
			
//		Toast.makeText(vContext, msg, vTOAST_TIME).show();
	}
	

	private boolean isNetworkAvailable() {
		AddShow = InternetConnection.checkStatus("");
		
		if(AddShow != 0) {
			vLayout.setVisibility(RelativeLayout.VISIBLE);
			
			no_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
			checking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			no_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
			refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
			return true;			
		} else {
			vLayout.setVisibility(RelativeLayout.INVISIBLE);
			
			no_internet_connection2.setVisibility(RelativeLayout.VISIBLE);
			checking_internet_connection.setVisibility(RelativeLayout.VISIBLE);
			no_internet_connection.setVisibility(RelativeLayout.VISIBLE);
			exit_from_app.setVisibility(RelativeLayout.VISIBLE);
			refresh_internet.setVisibility(RelativeLayout.VISIBLE);
			return false;
		}
	}


	
	
	class VideoAsyncTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
		public VideoAsyncTask(boolean isSearch) {
			if(isSearch){
			    leftCont.setClickable(false);
			    rightCont.setClickable(false);				
			} else {
			}
		}
		
		
		@Override
		protected ArrayList<HashMap<String,String>> doInBackground(String... urls) {
			vCurrentURL = urls[0]; 

			try {
				if (!isNetworkAvailable()) {
					throw new ConnectionException("Please check your internet connection and retry again");
				}
				
				return getGagListFromString(JSONStringFromURL(vCurrentURL));
			} catch (ConnectionException ce) {
				checkInternetStatus(ce.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
		
		
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> vGagsList){
			footer.setVisibility(View.VISIBLE);
			if(vGagsList == null) {
				refresh_internet.setText(refreshInternetText_No);
				no_internet_connection2.setText(noNnternetConnection2Text_No);
				checking_internet_connection.setText(checkingInternetConnection_No);
				no_internet_connection2.setVisibility(RelativeLayout.VISIBLE);
				checking_internet_connection.setVisibility(RelativeLayout.VISIBLE);
				no_internet_connection.setVisibility(RelativeLayout.VISIBLE);
				exit_from_app.setVisibility(RelativeLayout.VISIBLE);
				refresh_internet.setVisibility(RelativeLayout.VISIBLE);

				vProgressBar.setVisibility(View.INVISIBLE);
				if(EOV) {
					vGoBackText.setText(errorMessage);
					vLv.setAdapter(null);
					vLv.setVisibility(View.INVISIBLE);
					vLayout.setVisibility(RelativeLayout.VISIBLE);
					vGoBackText.setVisibility(View.VISIBLE);
					vGoBackButton.setVisibility(View.VISIBLE);
					no_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
					checking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
					no_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
					exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
					refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
				}
				else
					vGoBackText.setText("Somethink went wrong click top refresh menu or go back please :(");
			
			}else {
				try {				
					vAdapter = new CustomVideoAdapter(vContext, vGagsList,
							R.layout.list_item, new String[] { Constants.TAG_ALT,
									Constants.TAG_URL }, new int[] {R.id.video_list_item_title, R.id.video_list_item_image });

					vLv.setAdapter(vAdapter);
				} catch (Exception e) {
					vGoBackButton.setVisibility(View.VISIBLE);
					vGoBackText.setVisibility(View.VISIBLE);
					e.printStackTrace();
				}finally {
					vProgressBar.setVisibility(View.INVISIBLE);
				}
			}
	   }
		

		@Override
		protected void onPreExecute() {
			footer.setVisibility(View.INVISIBLE);
			no_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
			checking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			no_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
			refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
			
			vProgressBar.setVisibility(View.VISIBLE);
			vGoBackText.setVisibility(View.INVISIBLE);
			vGoBackButton.setVisibility(View.INVISIBLE);

			/* vGoBackButton*/
			vGoBackButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					this.vGoBackButton();
				}
				
				private void vGoBackButton(){
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}			
						new VideoAsyncTask(false).execute(vConsturl);
						vCurrentURL = vConsturl;

						vLv.setVisibility(View.VISIBLE);
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						System.err.println("Error"+e);
					}
				}
			});/**/
			super.onPreExecute();
		}
		
		/*
		 * Download JSON String from site with given URL  
		 * */
		InputStream inputStream;
		protected String JSONStringFromURL(String url) {
			try {
//				if(!isNetworkAvailable()) {
//					throw new ConnectionException("DNS server not responding! :(");
//				}
				
       			vHttpclient = new DefaultHttpClient(new BasicHttpParams());
       			vHttpget = new HttpGet(url);
       			vHttpget.setHeader("Content-type", "application/json");
       			vResponse = vHttpclient.execute(vHttpget);
       			vEntity = vResponse.getEntity();
       			inputStream = vEntity.getContent();
       			vReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

       			vSb = new StringBuilder();
       			String line = null;
       			while ((line = vReader.readLine()) != null) {
       				vSb.append(line + "\n");
       			}
       			
       			vResult = vSb.toString();
//       			System.out.println("vResult is :\t" + vResult);
       			System.out.printf("vResult is  \t\t%.50s:", vResult);
//       			Log.d("vResult:",vResult);
//           		} catch (ConnectionException ce) {
//           			checkInternetStatus(ce.getMessage());
           		} catch (Exception e) {
       				checkInternetStatus("DNS server not responding! :(");
           			e.printStackTrace();
				}finally {
           			try {
           				if (inputStream != null)
           					inputStream.close();
           			} catch (Exception e) {
           			}
           		}
       		return vResult;
		}
	
		/*
		 * Convert JSON String to ArrayList<HashMap<String, String>>
		 * */
		protected ArrayList<HashMap<String, String>> getGagListFromString(String vResult) {
			//, ArrayList<HashMap<String, String>> igagsListTemp) {
			if(vResult == null) {
				EOV = false;
//				System.out.println("vResult is null :" + vResult);
//				System.out.printf("vResult is  null %.50s:", vResult);
				return null;
			} 
			else
			if(vResult.equals("no more rows\n") || vResult.equalsIgnoreCase("no more rows")) {
				EOV = true;
				System.out.println("no more rows");
				return null;
			}
			else 
			{
				EOV = false;
				String iresults[]= vResult.split("!@#");
				vCurrentURLID = Integer.parseInt(iresults[0]);// current ID	
				vResult = iresults[1];			
				
				vGagsList = new ArrayList<HashMap<String, String>>();
	       		try {
	       			vJsonArray = new JSONArray(vResult);
	       			for (int i = 0; i < vJsonArray.length(); i++) {
	       				JSONObject jsonObject = vJsonArray.getJSONObject(i);
	       				src = jsonObject.getString(Constants.TAG_URL);
	       				alt = jsonObject.getString(Constants.TAG_ALT);
	       				
	       				vMap = new HashMap<String, String>();
	       				vMap.put(Constants.TAG_URL, src);
	       				vMap.put(Constants.TAG_ALT, alt);
	       				vGagsList.add(vMap);
	       			}
	       		} catch(NumberFormatException ne){
	       			ne.printStackTrace();
	       		} catch (Exception e) {
	       			e.printStackTrace();
	       		} finally {
//	       			System.out.printf("vGagsList is %.50s",vGagsList);
	       		}
			}
       		return vGagsList;
		}
	
	}
	
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//		    Log.e("On Config Change","LANDSCAPE");
		}else{
//		    Log.e("On Config Change","PORTRAIT");
		}
	}

    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(vKEY_CONTENT, vContent);
    }   
    
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.arrow_left:
				vCurrentURLID-=1;
				if(vCurrentURL.equalsIgnoreCase(vConsturl) || vCurrentURLID == 0)
				{
					Toast.makeText(vContext, "You are at the begining :)", vTOAST_TIME).show();
				}
				else if(vCurrentURLID>0)
				{
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						
						new VideoAsyncTask(false).execute(vConsturl+"?url="+(vCurrentURLID-1));
						
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						System.err.println("Error"+e);
					}
				}
				
			break;
			
			case R.id.home:
				if(vCurrentURL == null)
					vCurrentURL = "";
				
				if((vCurrentURL.equalsIgnoreCase(vConsturl) || vCurrentURLID == 0))// && isSearched == false)
				{
					Toast.makeText(vContext, "You are at home :)", vTOAST_TIME).show();
				}
				else //if(vCurrentURLID>0)
				{
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
					    leftCont.setClickable(true);
					    rightCont.setClickable(true);
					    
						new VideoAsyncTask(false).execute(vConsturl);
						vCurrentURL = vConsturl;
						
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						System.err.println("Error"+e);
					}
//				isSearched = false;
				}
				
			break;
			
			case R.id.arrow_right:
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						new VideoAsyncTask(false).execute(vConsturl+"?url="+vCurrentURLID);
						vCurrentURL = vConsturl+"?url="+vCurrentURLID;
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						System.err.println("Error"+e);
					}
//				Toast.makeText(vContext, "Wait while it is downloading...", vTOAST_TIME).show();
			break;
			
			default:
				break;
		}
		
		saveData(vCurrentURLID-1);
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		saveData(vCurrentURLID-1);  // save data
		

	    
//  	  if(AddShow == Constants.MOPUB)
//  		  if (moPubView != null) 
//  	            moPubView.destroy();
	  	  
	  	  
	  	  
	    System.gc(); 
		super.onDestroy();
	}
	
	
	public SharedPreferences sharedPref;
	public void saveData(int URLID) {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.video_id), URLID);
		editor.commit();
	}


	public int getData() {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		int URLID = sharedPref.getInt(getString(R.string.video_id), 0);
		//System.out.println("URLID"+URLID);
		return URLID;
	}

	
	
	
	
	
	
	
	
	
	
	
	/*
	 * Variables
	 */
	private final int vTOAST_TIME = 2000;
    private final String vKEY_CONTENT = "TestFragment:Content";
	private final String vConsturl = Constants.VIDEO_URL;
	
    private String vResult;
	private String src ;
	private String alt ;
    private String vContent = "???";
	private String vCurrentURL = Constants.VIDEO_URL;
    private String refreshInternetText_No = "Retry Again";
    private String refreshInternetText = "Refresh";
    private String noNnternetConnection2Text = "You have no internet connection!";
    private String noNnternetConnection2Text_No = "Somethink went wrong Retry Again!";
    private String checkingInternetConnection = "Checking for internet connection!";
    private String checkingInternetConnection_No = "For getting Gags Retry Again!!";
    private String errorMessage = "You have ended the Video daily limit :(";
    
	private Context vContext ;
	private Intent i ;
	private int vCurrentURLID = 0;
	
    private ArrayList<HashMap<String, String>> vGagsList ;
	private HashMap<String, String> vMap ;	
	private JSONArray vJsonArray ;
	private HttpResponse vResponse ;
	private HttpEntity vEntity ;
	private BufferedReader vReader ;
	private StringBuilder vSb ;
	private DefaultHttpClient vHttpclient ;
	private HttpGet vHttpget ;
	
	private ListAdapter vAdapter;
	private View vView ;
	private View footer ;
	private ListView vLv ;
	private RelativeLayout vLayout;    
    private ProgressBar vProgressBar;
    private TextView no_internet_connection2  ;
    private TextView checking_internet_connection ;
    private TextView vGoBackText;
    private Button exit_from_app ;
    private Button refresh_internet ;
	private Button vGoBackButton ;
    private ImageButton leftCont;
    private ImageButton homeCont;
    private ImageButton rightCont;
    private ImageView no_internet_connection ;
    
//    private boolean isSearched = false;
    private boolean EOV = false;
    
}
