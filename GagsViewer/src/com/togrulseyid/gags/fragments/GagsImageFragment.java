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
import com.togrulseyid.gags.libs.adapters.CustomImageAdapter;
import com.togrulseyid.gags.viewers.ImageViewer;
import com.togrulseyid.gags.viewer.R;

/**
 * كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۖ ثُمَّ إِلَيْنَا تُرْجَعُونَ
 * */

//@SuppressWarnings("deprecation")
public class GagsImageFragment extends SherlockListFragment implements OnClickListener {    
    

	// Declare an instance variable for your MoPubView.
//	private MoPubView moPubView;	// MoPub
	private int AddShow = 0;
	
    @Override
    public void onDestroy() {
		// TODO Auto-generated method stub
		saveData(iCurrentURLID-1);
	    System.gc(); 
	    
	    
	    
//  	  if(AddShow == Constants.MOPUB)
//  		  if (moPubView != null) 
//  	            moPubView.destroy();
      super.onDestroy();
    }
    
    
    
	
    public static GagsImageFragment newInstance(String content) {
        GagsImageFragment fragment = new GagsImageFragment();
        fragment.mContent = content.toString();
        return fragment;
    }
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		iContext = getActivity();
		
        if ((savedInstanceState != null) && savedInstanceState.containsKey(iKEY_CONTENT)) {
            mContent = savedInstanceState.getString(iKEY_CONTENT);
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
    	
	 	iCurrentURLID = getData();
	 	if(iCurrentURLID !=0 )
	 	{
	 		iCurrentURL = iConsturl + "?url=" + iCurrentURLID;
	 	}

		iView = inflater.inflate(R.layout.image, container, false);
	 	iGoBackText = (TextView) iView.findViewById(R.id.iGoBackText);
		iGoBackButton = (Button) iView.findViewById(R.id.iGoBackButton);
	 	
		iLayout=  (RelativeLayout) iView.findViewById(R.id.iLayout);
		iLv = (ListView) iView.findViewById(android.R.id.list);
		iProgressBar = (ProgressBar) iView.findViewById(R.id.iProgresBar);	
		//Controller UI 
	    footer = inflater.inflate(R.layout.controller, null);
	    leftCont = (ImageButton) footer.findViewById(R.id.arrow_left);
	    homeCont = (ImageButton) footer.findViewById(R.id.home);
	    rightCont = (ImageButton) footer.findViewById(R.id.arrow_right);	    
	    iLv.addFooterView(footer);
	    
	    leftCont.setOnClickListener(this);
	    homeCont.setOnClickListener(this);
	    rightCont.setOnClickListener(this);

	    
		no_internet_connection2 = (TextView) iView.findViewById(R.id.iNo_internet_connection2);
		checking_internet_connection = (TextView) iView.findViewById(R.id.iChecking_internet_connection);
		no_internet_connection = (ImageView) iView.findViewById(R.id.iNo_internet_connection);
		exit_from_app = (Button) iView.findViewById(R.id.iExit_from_app);
		refresh_internet = (Button) iView.findViewById(R.id.iRefresh_internet);

		

//		moPubView = (MoPubView) iView.findViewById(R.id.ImagemopubAdView);   /// mopub Ads
		
		try {
    		if (!isNetworkAvailable()) {
    			throw new ConnectionException("Please check your internet connection and retry again");
    		}
    		new ImageAsyncTask().execute(iCurrentURL);// Execute ImageAsyncTask and update ListView
    		
    	} catch (ConnectionException ce) {
    		checkInternetStatus(ce.getMessage());
    	}
		
        return iView;
    }    
    
    
	@Override
    public void onListItemClick(ListView l, View v, int position, long id){
		try{
			i = new Intent(iContext, ImageViewer.class);
			i.putExtra( Constants.TAG_SRC, iGagsList.get(position).get( Constants.TAG_SRC ));
			i.putExtra( Constants.TAG_ALT, iGagsList.get(position).get( Constants.TAG_ALT ));		
			startActivity(i);
		}catch (Exception e){
			Toast.makeText(iContext, e.getMessage(), iTOAST_TIME).show();
			
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
	    			iLv.setAdapter(null);
	    			new ImageAsyncTask().execute(iCurrentURL);
	    			
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
				// TODO Auto-generated method stub
				System.exit(-1);
				
			}
		});
			
//		Toast.makeText(iContext, msg, iTOAST_TIME).show();
	}
	

	private boolean isNetworkAvailable() {
		AddShow = InternetConnection.checkStatus("");
		
		if(AddShow != 0) {
			iLayout.setVisibility(RelativeLayout.VISIBLE);
			
			no_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
			checking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			no_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
			refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
			return true;			
		}
		
		iLayout.setVisibility(RelativeLayout.INVISIBLE);
		
		no_internet_connection2.setVisibility(RelativeLayout.VISIBLE);
		checking_internet_connection.setVisibility(RelativeLayout.VISIBLE);
		no_internet_connection.setVisibility(RelativeLayout.VISIBLE);
		exit_from_app.setVisibility(RelativeLayout.VISIBLE);
		refresh_internet.setVisibility(RelativeLayout.VISIBLE);
		return false;
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
        outState.putString(iKEY_CONTENT, mContent);
        super.onSaveInstanceState(outState);
    }   
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.arrow_left:
				iCurrentURLID-=1;
				if(iLastURL.equalsIgnoreCase( iConsturl) || iCurrentURLID == 0 ) {
					Toast.makeText(iContext, "You are at the begining :)", iTOAST_TIME).show();
				} else if(iCurrentURLID>0) {
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						
						iLv.setAdapter(null);
						new ImageAsyncTask().execute(iConsturl+"?url="+(iCurrentURLID-1));

					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						//System.err.println("Error"+e);
					}
				}
				
			break;
			
			case R.id.home:
				if( iLastURL == null )
					iLastURL = "";
				
				if((iCurrentURLID == 0)) {
					Toast.makeText(iContext, "You are at Home :)", iTOAST_TIME).show();
				} else {
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
	
					    leftCont.setClickable(true);
					    rightCont.setClickable(true);
					    iLv.setAdapter(null);
						new ImageAsyncTask().execute(iConsturl);
						iLastURL = iConsturl;
						
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			break;
			
			case R.id.arrow_right:
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						
						iLv.setAdapter(null);
						new ImageAsyncTask().execute(iConsturl + "?url=" + iCurrentURLID);
						iLastURL = iConsturl + "?url=" + iCurrentURLID;
						
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						//System.err.println("Error"+e);
					}
					
			break;
			
			default:
				break;
		}

		saveData(iCurrentURLID-1);
	}
	
	
	
	private SharedPreferences sharedPref;
	private void saveData(int URLID) {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.image_id), URLID);
		editor.commit();
	}

	
	private int getData() {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		int URLID = sharedPref.getInt(getString(R.string.image_id), 0);
		System.out.println("URLID: " + URLID);
		return URLID;
	}
	
	
	
	 
	private class ImageAsyncTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

//		@SuppressWarnings("deprecation")
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
//								Toast.makeText(iContext,  "Ad successfully loaded.", Toast.LENGTH_SHORT).show();
//					        }
//					    });
//					    
//					    moPubView.setOnAdFailedListener(new OnAdFailedListener() {
//							
//							@Override
//							public void OnAdFailed(MoPubView arg0) {
//								// TODO Auto-generated method stub
////								moPubView.setVisibility(View.GONE);
//							}
//						});
//					    
//					}catch(Exception e) {
//						e.printStackTrace();
//					}
//			}
	        
			
			
			
			
			
			iCurrentURL = urls[0];  // Added new
			iResult =null;
			iGagsList = null;
			try {
				if (!isNetworkAvailable()) {
					throw new ConnectionException("Please check your internet connection and retry again");
				}
				
				return iGagsList = getGagListFromString( JSONStringFromURL( iCurrentURL ), iGagsList );
				
			} catch (ConnectionException ce) {
				checkInternetStatus(ce.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(final ArrayList<HashMap<String, String>> iGagsList) {
			
			footer.setVisibility(View.VISIBLE);
			
			if(iGagsList == null) {
				iGoBackText.setVisibility(View.VISIBLE);
				iGoBackButton.setVisibility(View.VISIBLE);
				iProgressBar.setVisibility(View.INVISIBLE);
				iLv.setAdapter(null);
				iLv.setVisibility(View.GONE);
				if(EOI) {
					iGoBackText.setText(errorMessage);
					homeCont.setClickable(true);
				    leftCont.setClickable(true);
				    rightCont.setClickable(true);
				} else if(!EOI) 
					iGoBackText.setText("Somethink went wrong click top refresh menu or go back please :(");
				else;
			
			}else {
				try {
					iadapter = new CustomImageAdapter(iContext, iGagsList);
					iLv.setAdapter(iadapter);
				} catch (Exception e) {
					iGoBackButton.setVisibility(View.VISIBLE);
					iGoBackText.setVisibility(View.VISIBLE);
					e.printStackTrace();
				}finally {
					iProgressBar.setVisibility(View.INVISIBLE);
				}
			}
			
			
//			if(AddShow == 0) {
//				if(moPubView.getVisibility() != View.GONE)
//	        		moPubView.setVisibility(View.GONE);
//	        	
//	        	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iLv.getLayoutParams(); 
//	            layoutParams.setMargins(0, 0, 0, 0);      
//			}
	   }
		
		
		@Override
		protected void onPreExecute() {
			
			no_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
			checking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			no_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
			refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
			
			
			footer.setVisibility(View.INVISIBLE);
			iProgressBar.setVisibility(View.VISIBLE);
			iGoBackText.setVisibility(View.INVISIBLE);
			iGoBackButton.setVisibility(View.INVISIBLE);
   			
			/* iGoBackButton*/
			iGoBackButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					this.iGoBackButton();
				}
				
				private void iGoBackButton(){
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}

						iLv.setAdapter(null);
						new ImageAsyncTask().execute(iConsturl);
						iLastURL = iConsturl;

						iLv.setVisibility(View.VISIBLE);
						
					    leftCont.setClickable(true);
					    rightCont.setClickable(true);
					    
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			super.onPreExecute();
		}
		
		/*
		 * Download JSON String from site with given URL  
		 * */
		protected String JSONStringFromURL(String url) {
			InputStream inputStream = null;
			
			try {
				
      			iHttpclient = new DefaultHttpClient(new BasicHttpParams());
      			iHttpget = new HttpGet(url);
      			iHttpget.setHeader("Content-type", "application/json");
      			iResponse = iHttpclient.execute(iHttpget);
      			iEntity = iResponse.getEntity();
      			inputStream = iEntity.getContent();
      			iReader = new BufferedReader( new InputStreamReader(inputStream, "UTF-8"), 8 );

      			iSb = new StringBuilder();
      			String line = null;
      			while ((line = iReader.readLine()) != null) {
      				iSb.append(line + "\n");
      			}
      			
      			iResult = iSb.toString();
      			
      		} catch (Exception e) {
      			e.printStackTrace();
			}finally {
      			try {
      				if (inputStream != null)
      					inputStream.close();
      			} catch (Exception e) {
      			}
      		}
			
  		return iResult;
		}
	
		/*
		 * Convert JSON String to ArrayList<HashMap<String, String>>
		 * */
		protected ArrayList<HashMap<String, String>> getGagListFromString(String iResult, ArrayList<HashMap<String, String>> igagsListTemp) {
			if(iResult == null) {
				EOI = false;
				return null;
			} else if(iResult.equals("no more rows\n") || iResult.equalsIgnoreCase("no more rows")) {
				EOI = true;
				return null;
			} else {
				EOI = false;	
				String iresults[]= iResult.split("!@#");
				iCurrentURLID = Integer.parseInt(iresults[0]);// current ID	
				iResult = iresults[1];			
				
				igagsListTemp = new ArrayList<HashMap<String, String>>();
	       		try {
	       			iJsonArray = new JSONArray(iResult);
	       			for (int i = 0; i < iJsonArray.length(); i++) {
	       				JSONObject jsonObject = iJsonArray.getJSONObject(i);
	       				src = jsonObject.getString(Constants.TAG_SRC);
	       				alt = jsonObject.getString(Constants.TAG_ALT);
	       				
	       				System.out.println( src + "\n" + alt + "\n" );
	       				
	       				iMap = new HashMap<String, String>(); // adding HashList to ArrayList
	       				iMap.put(Constants.TAG_SRC, src);
	       				iMap.put(Constants.TAG_ALT, alt);
	       				igagsListTemp.add(iMap);
	       			}
	       		} catch(NumberFormatException ne){
	       			ne.printStackTrace();
	       		} catch (Exception e) {
	       			e.printStackTrace();
	       		} 
			}
      		return igagsListTemp;
		}
	}
	
	
	private final int iTOAST_TIME = 2000 ;
    private final String iKEY_CONTENT = "GagsImageFragment:Content" ;
	private final String iConsturl = Constants.IMAGE_URL ;
	private String iCurrentURL = Constants.IMAGE_URL ;
	private int iCurrentURLID = 0 ;
    private String mContent = "???" ;
    private ArrayList<HashMap<String, String>> iGagsList ;
    private String iResult ;
	private String src ;
	private String alt ;
	private  String iLastURL = Constants.IMAGE_URL ;
	private Context iContext ;
	private Intent i ;
	private HttpResponse iResponse ;
	private HttpEntity iEntity ;
	private BufferedReader iReader ;
	private StringBuilder iSb ;
	private DefaultHttpClient iHttpclient ;
	private HttpGet iHttpget ;
	private JSONArray iJsonArray ;
	private HashMap<String, String> iMap ;	
	private ListAdapter iadapter;
	private View iView ;
	private View footer ;
	private ListView iLv ;
	private RelativeLayout iLayout ;
    private ProgressBar iProgressBar ;
    private TextView no_internet_connection2 ;
    private TextView checking_internet_connection ;
    private ImageView no_internet_connection ;
    private Button exit_from_app ;
    private Button refresh_internet ;
    private ImageButton leftCont ;
    private ImageButton homeCont ;
    private ImageButton rightCont ;
    private TextView iGoBackText ;
	private Button iGoBackButton ;
    private boolean EOI = false ;
    private String errorMessage = "You have ended the Image daily limit :(" ;
    
}
