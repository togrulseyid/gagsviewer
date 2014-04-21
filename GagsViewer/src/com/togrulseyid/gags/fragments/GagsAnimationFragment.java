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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.togrulseyid.gags.libs.adapters.CustomAnimationAdapter;
import com.togrulseyid.gags.viewers.AnimationViewer;
import com.togrulseyid.gags.viewer.R;

/**
 * كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۖ ثُمَّ إِلَيْنَا تُرْجَعُونَ
 * */

public class GagsAnimationFragment extends SherlockListFragment implements OnClickListener {

	// Declare an instance variable for your MoPubView.
//	private MoPubView moPubView;	// MoPub
	private int AddShow = 0;
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		saveData(aCurrentURLID-1);
	    System.gc();
	    
	    
//  	  if(AddShow == Constants.MOPUB)
//  		  if (moPubView != null) 
//  	            moPubView.destroy();    
		super.onDestroy();
	}
	
	
	
	
    public static GagsAnimationFragment newInstance(String content) {
        GagsAnimationFragment fragment = new GagsAnimationFragment();
        fragment.mContent = content.toString();
        return fragment;
    }
    
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		aContext = getActivity();
		
        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

	
    @SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
	 	if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

	 	aCurrentURLID = getData();
	 	if(aCurrentURLID !=0 )
	 	{
	 		aCurrentURL = aConsturl + "?url=" + aCurrentURLID;
	 	}

	 	aView = inflater.inflate(R.layout.animation, container, false);
    	
	 	aGoBackText = (TextView) aView.findViewById(R.id.aGoBackText);
    	aGoBackButton = (Button) aView.findViewById(R.id.aGoBackButton);

	 	
    	aLayout=  (RelativeLayout) aView.findViewById(R.id.aLayout);
    	aLv = (ListView) aView.findViewById(android.R.id.list);
		aProgresBar = (ProgressBar) aView.findViewById(R.id.aProgresBar);
		
		//Controller UI 
	    footer = inflater.inflate(R.layout.controller, null);
	    leftCont = (ImageButton) footer.findViewById(R.id.arrow_left);
	    homeCont = (ImageButton) footer.findViewById(R.id.home);
	    rightCont = (ImageButton) footer.findViewById(R.id.arrow_right);	    
	    aLv.addFooterView(footer);
	    
	    leftCont.setOnClickListener(this);
	    homeCont.setOnClickListener(this);
	    rightCont.setOnClickListener(this);

	    
		aNo_internet_connection2 = (TextView) aView.findViewById(R.id.aNo_internet_connection2);
		aChecking_internet_connection = (TextView) aView.findViewById(R.id.aChecking_internet_connection);
		aNo_internet_connection = (ImageView) aView.findViewById(R.id.aNo_internet_connection);
		exit_from_app = (Button) aView.findViewById(R.id.aExit_from_app);
		refresh_internet = (Button) aView.findViewById(R.id.aRefresh_internet);


//		moPubView = (MoPubView) aView.findViewById(R.id.AnimationMopubAdView);   /// mopub Ads
		
		
    	try {
    		if (!isNetworkAvailable()) {
    			throw new ConnectionException("Please check your internet connection and retry again");
    		}
    		new AnimationAsyncTask().execute(aCurrentURL);  // Execute AnimationAsyncTask and update ListView 		
    		
    	} catch (ConnectionException ce) {
    		checkInternetStatus(ce.getMessage());
    	}
        return aView;
    }

    
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {

		try{
			i = new Intent(aContext, AnimationViewer.class);
			i.putExtra( Constants.TAG_SRC, aGagsList.get(position).get( Constants.TAG_SRC ));
			i.putExtra( Constants.TAG_ALT, aGagsList.get(position).get( Constants.TAG_ALT ));
			startActivity(i);
		}catch (Exception e){
			Toast.makeText(aContext, e.getMessage() + e, aTOAST_TIME).show();
		}
    }

	
	private void checkInternetStatus(String msg) {

		refresh_internet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
	    		try {
	    			if (!isNetworkAvailable()) {
	    				throw new ConnectionException("Please check your internet connection and retry again");
	    			}
	    			aLv.setAdapter(null);
	    			new AnimationAsyncTask().execute(aCurrentURL);
	    			
	    		} catch (ConnectionException ce) {
	    			checkInternetStatus(ce.getMessage());
	    		}catch (Exception e) {
	    			e.printStackTrace();
	    		}
			}
		});
		
		exit_from_app.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.exit(-1);
				
			}
		});
	}
	
	
	private boolean isNetworkAvailable(/*Context aContext*/) {
		AddShow = InternetConnection.checkStatus("");
		
		if(AddShow != 0) {
			aLayout.setVisibility(RelativeLayout.VISIBLE);
			
			aNo_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
			aChecking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			aNo_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
			refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
		return true;			
		}else {
			aLayout.setVisibility(RelativeLayout.INVISIBLE);
			
			aNo_internet_connection2.setVisibility(RelativeLayout.VISIBLE);
			aChecking_internet_connection.setVisibility(RelativeLayout.VISIBLE);
			aNo_internet_connection.setVisibility(RelativeLayout.VISIBLE);
			exit_from_app.setVisibility(RelativeLayout.VISIBLE);
			refresh_internet.setVisibility(RelativeLayout.VISIBLE);
		return false;
		}
	}

	
	private class AnimationAsyncTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
		
		
		@Override
		protected ArrayList<HashMap<String,String>> doInBackground(String... urls) {
			
//			if (AddShow == Constants.MOPUB) {
//					try {
//						moPubView.setAdUnitId(Constants.MOPUB_ID);
//					    moPubView.loadAd();
//					    moPubView.setOnAdLoadedListener(new OnAdLoadedListener() {
//					        public void OnAdLoaded(MoPubView mpv) {
//
//								moPubView.setVisibility(View.VISIBLE);
//								Toast.makeText(aContext,  "Ad successfully loaded.", Toast.LENGTH_SHORT).show();
//					        }
//					    });
//					    
//					    moPubView.setOnAdFailedListener(new OnAdFailedListener() {
//							
//							@Override
//							public void OnAdFailed(MoPubView arg0) {
//								// TODO Auto-generated method stub
//								moPubView.setVisibility(View.GONE);
//							}
//						});
//					    
//					}catch(Exception e) {
//						e.printStackTrace();
//					}
//			}
			
			
			
			aCurrentURL = urls[0];
			
			try {
				if (!isNetworkAvailable()) {
					throw new ConnectionException("Please check your internet connection and retry again");
				}

				aGagsList =  getGagListFromString(JSONStringFromURL(aCurrentURL), aGagsList );
				return aGagsList;
			} catch (ConnectionException ce) {
				checkInternetStatus(ce.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
//			return aGagsList;
		}
		
		
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(final ArrayList<HashMap<String, String>> aGagsList){
			footer.setVisibility(View.VISIBLE);
			
			if(aGagsList == null) {
				//Toast.makeText(aContext, "Somethink went Wrong delete Data please", aTOAST_TIME).show();
				aGoBackText.setVisibility(View.VISIBLE);
				aGoBackButton.setVisibility(View.VISIBLE);
				aProgresBar.setVisibility(View.INVISIBLE);
				aLv.setAdapter(null);
				aLv.setVisibility(View.GONE);
				if(EOA)
					aGoBackText.setText(errorMessage);
				else
					aGoBackText.setText("Somethink went wrong click top refresh menu or go back please :(");
			
			}else {
				try {
					adapter = new CustomAnimationAdapter(aContext, aGagsList);
	
					aLv.setAdapter(adapter);
				} catch (Exception e) {
					aGoBackButton.setVisibility(View.VISIBLE);
					aGoBackText.setVisibility(View.VISIBLE);
					e.printStackTrace();
				}finally {
					aProgresBar.setVisibility(View.INVISIBLE);
				}
			}
			
			
			
			
			
			

//			if(AddShow == 0) {
//				if(moPubView.getVisibility() != View.GONE)
//	        		moPubView.setVisibility(View.GONE);
//	        	
//	        	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) aLv.getLayoutParams(); 
//	            layoutParams.setMargins(0, 0, 0, 0);      
//			}
	   }

		
		@Override
		protected void onPreExecute() {

			footer.setVisibility(View.INVISIBLE);
			aNo_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
			aChecking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			aNo_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
			refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
			
    		aProgresBar.setVisibility(View.VISIBLE);
			aGoBackText.setVisibility(View.INVISIBLE);
			aGoBackButton.setVisibility(View.INVISIBLE);

			/* aGoBackButton*/
			aGoBackButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					this.aGoBackButton();
				}
				
				private void aGoBackButton(){
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}			
						new AnimationAsyncTask().execute(aConsturl);
						aLastURL = aConsturl;

						aLv.setVisibility(View.VISIBLE);
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						//System.err.println("Error"+e);
					}
				}
			});/**/
			super.onPreExecute();

		}
		
		/*
		 * Download JSON String from site with given URL  
		 * */
		protected String JSONStringFromURL(String url) {
			InputStream inputStream = null;
			
			try {
//				
       			aHttpclient = new DefaultHttpClient(new BasicHttpParams());
       			aHttpget = new HttpGet(url);
       			aHttpget.setHeader("Content-type", "application/json");
       			response = aHttpclient.execute(aHttpget);
       			aEntity = response.getEntity();
       			inputStream = aEntity.getContent();
       			aReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

       			aSb = new StringBuilder();
       			String line = null;
       			while ((line = aReader.readLine()) != null) {
       				aSb.append(line + "\n");
       			}
       			
//   				aResult = aSb.toString();
       			return aSb.toString();

           		} catch (Exception e) {
           			e.printStackTrace();
				}finally {
           			try {
           				if (inputStream != null)
           					inputStream.close();
           			} catch (Exception e) {
           			}
           		}
//       		return aResult;
				return null;
		
		}
	
		/*
		 * Convert JSON String to ArrayList<HashMap<String, String>>
		 * */
		protected ArrayList<HashMap<String, String>> getGagListFromString(String aResult, ArrayList<HashMap<String, String>> gagsListTemp) { 
			if(aResult == null) {
				EOA = false;
				return null;
			} 
			else
			if(aResult.equals("no more rows\n") || aResult.equalsIgnoreCase("no more rows")) {
				EOA = true;
				return null;
			}
			else 
			{
				EOA = false;				
				String results[]= aResult.split("!@#");
				aCurrentURLID = Integer.parseInt(results[0]);
				aResult = results[1];			
				
				gagsListTemp = new ArrayList<HashMap<String, String>>();
	       		try {
	       			jsonArray = new JSONArray(aResult);
	       			for (int i = 0; i < jsonArray.length(); i++) {
	       				
	       				JSONObject jsonObject = jsonArray.getJSONObject(i);
	       				src = jsonObject.getString(Constants.TAG_SRC);
	       				alt = jsonObject.getString(Constants.TAG_ALT);	       				
	       				
	       				aMap = new HashMap<String, String>(); // adding HashList to ArrayList
	       				aMap.put(Constants.TAG_SRC, src);
	       				aMap.put(Constants.TAG_ALT, alt);
	       				gagsListTemp.add(aMap);
	       				
	       			}
	       		} catch(NumberFormatException ne){
	       			ne.printStackTrace();
	       		} catch (Exception e) {
	       			e.printStackTrace();
	       		} 
			}
       		return gagsListTemp;
		
		}
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
		    //Log.e("On Config Change","LANDSCAPE");
		}else{
		    //Log.e("On Config Change","PORTRAIT");
		}
	}
	
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }

    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.arrow_left:
				aCurrentURLID-=1;
				if(aLastURL.equalsIgnoreCase(aConsturl) || aCurrentURLID == 0)
				{
					Toast.makeText(aContext, "You are at the begining :)", aTOAST_TIME).show();
				}
				else if(aCurrentURLID>0)
				{
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						aLv.setAdapter(null);
						new AnimationAsyncTask().execute(aConsturl+"?url="+(aCurrentURLID-1));

					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						//System.err.println("Error"+e);
					}
				}
				
			break;
			
			case R.id.home:
				if(aLastURL == null) 
					aLastURL = "";
				
				if((aLastURL.equalsIgnoreCase(aConsturl) || aCurrentURLID == 0))// && isSearched == false)
				{
					Toast.makeText(aContext, "You are at Home :)", aTOAST_TIME).show();
				} else {
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
	
					    leftCont.setClickable(true);
					    rightCont.setClickable(true);
					    
					    aLv.setAdapter(null);
						new AnimationAsyncTask().execute(aConsturl);
						aLastURL = aConsturl;
						
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						//System.err.println("Error"+e);
					}
//				isSearched = false;
				}
			break;
			
			case R.id.arrow_right:
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						aLv.setAdapter(null);
						new AnimationAsyncTask().execute(aConsturl+"?url="+aCurrentURLID);
						aLastURL = aConsturl+"?url="+aCurrentURLID;
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						//System.err.println("Error"+e);
					}
				//Toast.makeText(aContext, "Wait while it is downloading...", aTOAST_TIME).show();
			break;
			
			default:
				break;
		}
		
		saveData(aCurrentURLID-1);
	}



	public SharedPreferences sharedPref;
	public void saveData(int URLID) {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.animation_id), URLID);
		editor.commit();
	}

	
	public int getData() {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		int URLID = sharedPref.getInt(getString(R.string.animation_id), 0);
		return URLID;
	}
	
	
	
	
	
	
	
	private final int aTOAST_TIME = 2000;
	private final String aConsturl=Constants.ANIMATION_URL;
	private final String KEY_CONTENT = "GagsAnimationFragment:Content";
	private String aCurrentURL = Constants.ANIMATION_URL;
	private String aLastURL = Constants.ANIMATION_URL;
	private String mContent = "???";
	private String errorMessage = "You have ended the Animation daily limit :(";
	private String src ;
	private String alt ;
//    private String aResult;
//    private boolean isSearched = false;
    private boolean EOA = false;
	private int aCurrentURLID = 0;
	private Context aContext ;
	private Intent i ;
	private HttpResponse response ;
	private HttpEntity aEntity ;
	private BufferedReader aReader ;
	private StringBuilder aSb ;
	private DefaultHttpClient aHttpclient ;
	private HttpGet aHttpget ;
	private JSONArray jsonArray ;
	private ArrayList<HashMap<String, String>> aGagsList ;
	private HashMap<String, String> aMap ;
	private ListAdapter adapter;
	private View aView ; 
	private View footer ;
    private ListView aLv ;  
    private RelativeLayout aLayout;    
    private ProgressBar aProgresBar;
    private TextView aNo_internet_connection2  ;
    private TextView aChecking_internet_connection ;
    private TextView aGoBackText;
    private Button exit_from_app ;
    private Button refresh_internet ;
    private Button aGoBackButton ;
	private ImageButton leftCont;
	private ImageButton homeCont;
	private ImageButton rightCont;
    private ImageView aNo_internet_connection ;

    
    
}
