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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import com.togrulseyid.gags.libs.adapters.CustomJokesAdapter;
import com.togrulseyid.gags.viewers.JokeViewer;
import com.togrulseyid.gags.viewer.R;

/**
 * كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۖ ثُمَّ إِلَيْنَا تُرْجَعُونَ
 * */

public class GagsJokesFragment extends SherlockListFragment  implements OnClickListener {

	// Declare an instance variable for your MoPubView.
//	private MoPubView moPubView;	// MoPub
	private int AddShow = 0;
	
    public static GagsJokesFragment newInstance(String content) {
        GagsJokesFragment fragment = new GagsJokesFragment();
        fragment.mContent = content.toString();
        return fragment;
    }
    
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		jContext = getActivity();
		
        if ((savedInstanceState != null) && savedInstanceState.containsKey(jKEY_CONTENT)) {
            mContent = savedInstanceState.getString(jKEY_CONTENT);
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
	
	
    @SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}


	 	jCurrentURLID = getData();
	 	if(jCurrentURLID !=0 )
	 	{
	 		jCurrentURL = jConsturl + "?url=" + jCurrentURLID;
	 	}


		jView = inflater.inflate(R.layout.jokes, container, false);

	 	// men olajam
	 	jGoBackText = (TextView) jView.findViewById(R.id.jGoBackText);

		jLayout=  (RelativeLayout) jView.findViewById(R.id.jLayout);
		jLv = (ListView) jView.findViewById(android.R.id.list);
		jProgressBar = (ProgressBar) jView.findViewById(R.id.jProgresBar);	
		jGoBackButton = (Button) jView.findViewById(R.id.jGoBackButton);
		
		//Controller UI 
	    footer = inflater.inflate(R.layout.controller, null);
//
//		moPubView = (MoPubView) footer.findViewById(R.id.Jokes_MopubAdView);   /// mopub Ads
		
		
	    leftCont = (ImageButton) footer.findViewById(R.id.arrow_left);
	    homeCont = (ImageButton) footer.findViewById(R.id.home);
	    rightCont = (ImageButton) footer.findViewById(R.id.arrow_right);	    
	    jLv.addFooterView(footer);
	    
	    leftCont.setOnClickListener(this);
	    homeCont.setOnClickListener(this);
	    rightCont.setOnClickListener(this);

	    
		no_internet_connection2 = (TextView) jView.findViewById(R.id.jNo_internet_connection2);
		checking_internet_connection = (TextView) jView.findViewById(R.id.jChecking_internet_connection);
		no_internet_connection = (ImageView) jView.findViewById(R.id.jNo_internet_connection);
		exit_from_app = (Button) jView.findViewById(R.id.jExit_from_app);
		refresh_internet = (Button) jView.findViewById(R.id.jRefresh_internet);

		try {
    		if (!isNetworkAvailable()) {
    			throw new ConnectionException("Please check your internet connection and retry again");
    		}
    		new JokeAsyncTask().execute(jCurrentURL);// Execute JokeAsyncTask and update ListView 
    		
    	} catch (ConnectionException ce) {
    		checkInternetStatus(ce.getMessage());
    	}
		
        return jView;
    }    
    
    
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		try{
			i = new Intent(jContext, JokeViewer.class); 
			i.putExtra( Constants.TAG_TITLE, jGagsList.get(position).get(Constants.TAG_TITLE));
			i.putExtra(	Constants.TAG_TEXT, jGagsList.get(position).get(Constants.TAG_TEXT));		
			startActivity(i);
		}catch(Exception e) {
			Toast.makeText(jContext, e.getMessage(), jTOAST_TIME).show();
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
	    			
	    			new JokeAsyncTask().execute(jCurrentURL);
	    			
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

	
	public boolean isTrue = true;
	private boolean isNetworkAvailable() {
		AddShow = InternetConnection.checkStatus("");
		
		if(AddShow != 0) {
			if(!isTrue) {
				jLayout.setVisibility(RelativeLayout.VISIBLE);
				
				no_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
				checking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
				no_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
				exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
				refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
			}
			isTrue = true;
			return true;			
		}else {
			if(isTrue) {
				jLayout.setVisibility(RelativeLayout.INVISIBLE);

				refresh_internet.setVisibility(RelativeLayout.VISIBLE);
				checking_internet_connection.setVisibility(RelativeLayout.VISIBLE);
				no_internet_connection.setVisibility(RelativeLayout.VISIBLE);
				exit_from_app.setVisibility(RelativeLayout.VISIBLE);
				no_internet_connection2.setVisibility(RelativeLayout.VISIBLE);
			}
			isTrue = false;
			return false;
		}
	}

	
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(jKEY_CONTENT, mContent);
    }

    
    public class JokeAsyncTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
    	
//    	@SuppressWarnings("deprecation")
		@Override
		protected ArrayList<HashMap<String,String>> doInBackground(String... urls) {
//    		if (AddShow == Constants.MOPUB) {
//				try {
//					moPubView.setAdUnitId(Constants.MOPUB_ID);
//				    moPubView.loadAd();
//				    moPubView.setOnAdLoadedListener(new OnAdLoadedListener() {
//				        public void OnAdLoaded(MoPubView mpv) {
//
//							moPubView.setVisibility(View.VISIBLE);
//							Toast.makeText(jContext,  "Ad successfully loaded.", Toast.LENGTH_SHORT).show();
//				        }
//				    });
//				    
//				    moPubView.setOnAdFailedListener(new OnAdFailedListener() {
//						
//						@Override
//						public void OnAdFailed(MoPubView arg0) {
//							// TODO Auto-generated method stub
//							moPubView.setVisibility(View.GONE);
//						}
//					});
//				    
//				}catch(Exception e) {
//					e.printStackTrace();
//				}
//		}
        
		
		
			jCurrentURL = urls[0];

			try {
				if (!isNetworkAvailable()) {
					throw new ConnectionException("Please check your internet connection and retry again");
				}
				
//				jResult = JSONStringFromURL(jCurrentURL);
//				jGagsList =getGagListFromString(jResult);
				return getGagListFromString(JSONStringFromURL(jCurrentURL));

			} catch (ConnectionException ce) {
				checkInternetStatus(ce.getMessage());
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
//			return jGagsList;
		}
	
	@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> jGagsList){
			footer.setVisibility(View.VISIBLE);
			
			if(jGagsList == null) {
				//Toast.makeText(jContext, "Somethink went Wrong delete Data please", jTOAST_TIME).show();
				jGoBackText.setVisibility(View.VISIBLE);
				jGoBackButton.setVisibility(View.VISIBLE);
				jProgressBar.setVisibility(View.INVISIBLE);
				jLv.setAdapter(null);
				jLv.setVisibility(View.GONE);
				if(EOJ)
					jGoBackText.setText(errorMessage);
				else
					jGoBackText.setText("Somethink went wrong click refresh menu on top or go back please :(");
			} else {
				try {				
					//myFont1
//					jadapter = new SimpleAdapter(jContext, jGagsList, R.layout.jokes_list_item, 
//							new String[] { Constants.TAG_TITLE}, new int[] {R.id.jokes_list_item_title});
					jadapter = new CustomJokesAdapter(jContext, jGagsList);
					
					
					jLv.setAdapter(jadapter);
				} catch (Exception e) {
					jGoBackButton.setVisibility(View.VISIBLE);
					jGoBackText.setVisibility(View.VISIBLE);
					e.printStackTrace();
				}finally {
					jProgressBar.setVisibility(View.INVISIBLE);
				}
			}
			
			
			
			
			
//			if(AddShow == 0) {
//				if(moPubView.getVisibility() != View.GONE)
//	        		moPubView.setVisibility(View.GONE);
//	        	
//	        	RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) jLv.getLayoutParams(); 
//	            layoutParams.setMargins(0, 0, 0, 0);      
//			}
	   }
	
	
	@Override
		protected void onPreExecute() {
			footer.setVisibility(View.INVISIBLE);
		
			no_internet_connection2.setVisibility(RelativeLayout.INVISIBLE);
			checking_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			no_internet_connection.setVisibility(RelativeLayout.INVISIBLE);
			exit_from_app.setVisibility(RelativeLayout.INVISIBLE);
			refresh_internet.setVisibility(RelativeLayout.INVISIBLE);
		
			jProgressBar.setVisibility(View.VISIBLE);
			jGoBackText.setVisibility(View.INVISIBLE);
			jGoBackButton.setVisibility(View.INVISIBLE);
				
			/* jGoBackButton*/
			jGoBackButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					this.jGoBackButton();
				}
				
				private void jGoBackButton(){					
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}			
						new JokeAsyncTask().execute(jConsturl);
						jCurrentURL = jConsturl;
						jLv.setVisibility(View.VISIBLE);
						
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
		 **/
		protected String JSONStringFromURL(String url) {
			try {
				jHttpclient = new DefaultHttpClient(new BasicHttpParams());
	   			jHttpget = new HttpGet(url);
	   			jHttpget.setHeader("Content-type", "application/json");
	   			jResponse = jHttpclient.execute(jHttpget);
	   			jEntity = jResponse.getEntity();
	   			inputStream = jEntity.getContent();
	   			jReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
	
	   			jSb = new StringBuilder();
	   			String line = null;
	   			while ((line = jReader.readLine()) != null) {
	   				jSb.append(line + "\n");
	   			}

//	   			jResult = jSb.toString();
	   			return jSb.toString();
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
			
//	   		return jResult;
			return null;
			
		}

	/*
	 * Convert JSON String to ArrayList<HashMap<String, String>>
	 * */
		protected ArrayList<HashMap<String, String>> getGagListFromString(String jResult){ //, ArrayList<HashMap<String, String>> jgagsListTemp) {
			if(jResult == null) {
				EOJ = false;
				return null;
			} 
			else
			if(jResult.equals("no more rows\n") || jResult.equalsIgnoreCase("no more rows")) {
				EOJ = true;
				return null;
			}
			else 
			{	
				EOJ = false;
				String iresults[]= jResult.split("!@#");
				jCurrentURLID = Integer.parseInt(iresults[0]);	
				jResult = iresults[1];			
				
				jGagsList = new ArrayList<HashMap<String, String>>();
	       		try {
	       			jJsonArray = new JSONArray(jResult);
	       			for (int i = 0; i < jJsonArray.length(); i++) {
	       				JSONObject jsonObject = jJsonArray.getJSONObject(i);
	       				
	       				title = jsonObject.getString(Constants.TAG_TITLE);
	       				text = jsonObject.getString(Constants.TAG_TEXT);
	       				
	       				jMap = new HashMap<String, String>();
	       				jMap.put(Constants.TAG_TITLE, title);
	       				jMap.put(Constants.TAG_TEXT, text);
	       				Log.d(Constants.TAG_TITLE, title);
	       				Log.i(Constants.TAG_TEXT, text);
	       				jGagsList.add(jMap);
	       			}
	       		} catch(JSONException ne){
	       			ne.printStackTrace();
	       		} catch (Exception e) {
	       			e.printStackTrace();
	       		} 
			}
	   		return jGagsList;
		}
    }

    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.arrow_left:
				jCurrentURLID-=1;
				if(jCurrentURL.equalsIgnoreCase(jConsturl) || jCurrentURLID == 0) {
					Toast.makeText(jContext, "You are at the begining :)", jTOAST_TIME).show();
				} else if(jCurrentURLID>0) {
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						jLv.setAdapter(null);
						new JokeAsyncTask().execute(jConsturl+"?url="+(jCurrentURLID-1));
						
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						System.err.println("Error"+e);
					}
				}
				
			break;
			
			case R.id.home:
				if(jCurrentURL == null)
						jCurrentURL = "";
				
				if((jCurrentURL.equalsIgnoreCase(jConsturl) && jCurrentURLID == 0)) {
					Toast.makeText(jContext, "You are at Home :)", jTOAST_TIME).show();
				}else {
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
					    leftCont.setClickable(true);
					    rightCont.setClickable(true);
					    
					    jLv.setAdapter(null);
						new JokeAsyncTask().execute(jConsturl);
						jCurrentURL = jConsturl;
						
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						System.err.println("Error"+e);
					}
				}
			break;
			
			case R.id.arrow_right:
					try {
						if (!isNetworkAvailable()) {
							throw new ConnectionException("Please check your internet connection and retry again");
						}
						jLv.setAdapter(null);
						new JokeAsyncTask().execute(jConsturl+"?url="+jCurrentURLID);
						jCurrentURL = jConsturl+"?url="+jCurrentURLID;
					} catch (ConnectionException ce) {
						checkInternetStatus(ce.getMessage());
					} catch (Exception e) {
						System.err.println("Error"+e);
					}
				//Toast.makeText(jContext, "Wait while it is downloading...", jTOAST_TIME).show();
			break;
			
			default:
				break;
		}
		saveData(jCurrentURLID-1);
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		saveData(jCurrentURLID-1);
		
		  
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
		editor.putInt(getString(R.string.jokes_id), URLID);
		editor.commit();
	}
	
	
	public int getData() {
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		int URLID = sharedPref.getInt(getString(R.string.jokes_id), 0);
//		System.out.println("URLID"+URLID);
		return URLID;
	}
	
	
	
	
	
	
	
	
	
	private final int jTOAST_TIME = 2000;
    private final String jKEY_CONTENT = "TestFragment:Content";
	private final String jConsturl=Constants.JOKE_URL;
	private String jCurrentURL = Constants.JOKE_URL;
    private String errorMessage = "You have ended the Jokes daily limit";
    private String mContent = "???";
//    private String jResult;
	private String title ; 
	private String text ;
	private int jCurrentURLID = 0;
    private ArrayList<HashMap<String, String>> jGagsList ;
	private Context jContext ;
	private Intent i ;
	private HttpResponse jResponse ;
	private HttpEntity jEntity ;
	private BufferedReader jReader ;
	private StringBuilder jSb ;
	private DefaultHttpClient jHttpclient ;
	private InputStream inputStream;
	private HttpGet jHttpget ;
	private JSONArray jJsonArray ;
	private HashMap<String, String> jMap ;	
	private ListAdapter jadapter;
	private Button jGoBackButton ;
	private View jView ;
	private View footer ;
	private ListView jLv ;
	private RelativeLayout jLayout;    
    private ProgressBar jProgressBar;
    private TextView no_internet_connection2  ;
    private TextView checking_internet_connection ;
    private TextView jGoBackText;
    private Button exit_from_app ;
    private Button refresh_internet ;
    private ImageButton leftCont;
    private ImageButton homeCont;
    private ImageButton rightCont;
    private ImageView no_internet_connection ;

    private boolean EOJ = false;
    

}
