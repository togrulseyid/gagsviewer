package com.togrulseyid.gags.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.ironsource.mobilcore.CallbackResponse;
import com.ironsource.mobilcore.MobileCore;
import com.ironsource.mobilcore.MobileCore.AD_UNITS;
import com.ironsource.mobilcore.MobileCore.LOG_TYPE;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.OnAdFailedListener;
import com.mopub.mobileads.MoPubView.OnAdLoadedListener;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.ImageLoader;
import com.togrulseyid.gags.fragments.GagsAnimationFragment;
import com.togrulseyid.gags.fragments.GagsImageFragment;
import com.togrulseyid.gags.fragments.GagsInfoFragment;
import com.togrulseyid.gags.fragments.GagsJokesFragment;
import com.togrulseyid.gags.fragments.GagsVideoFragment;
import com.togrulseyid.gags.library.IconPagerAdapter;
import com.togrulseyid.gags.library.TabPageIndicator;
import com.togrulseyid.gags.viewer.R;

/**
 * 
 * اللَّه�?مَّ لاَسَهْلَ إ�?لاَّ مَا جَعَلْتَه�? سَهْلاً، وَأَنْتَ تَجْعَل�? الْحَزْنَ إ�?ذَا ش�?ئْتَ سَهْلاً
 * 
 *  «Allahummə lə səhlə illə mə cəaltəhu səhlən va əntə təcalu-l-həznə izə şitə səhlən»
 *  (Allahım! Sənin asan etdiyin işdən asanı yoxdur. Həqiqətən, Sənin üçün çətin bir iş (məsələ) yoxdur).
 *  
 * */

@SuppressWarnings("deprecation")
public class GagsViewer extends SherlockFragmentActivity {	
	
	private MoPubView moPubView;// Declare an instance variable for your MoPubView.
	private Activity activity;
	private ViewPager pager;
	private TabPageIndicator indicator ;
	private ImageLoader imageLoader;
	private SubMenu subMenu;
	private MenuItem menuItem;
	private Menu menu;
	private int AddShow = 1;
	private ProgressBar progressBar;
	
	
	private static final String[] CONTENT = new String[] {
		 "Images" ,
		 "Animation",
		 "Videos",
		 "Jokes",
		 "About Us",
		 };
	
	private static final int[] ICONS = new int[] {
		 R.drawable.perm_group_images, 
		 R.drawable.perm_group_gifs,
		 R.drawable.perm_group_videos,
		 R.drawable.perm_group_jokes,
		 R.drawable.perm_group_gags,
		};
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
	    setContentView(R.layout.simple_tabs);

        
    	activity = this;
    	progressBar =  (ProgressBar) findViewById(R.id.mainScreenProgresBar);
        moPubView = (MoPubView) findViewById(R.id.MopubAdView);
        
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabPageIndicator) findViewById(R.id.indicator);
        
        try {
        	new LoadContentTask().execute(getSupportFragmentManager());
        }catch(Exception e) {
        	
        }
//        makeNotification();
    	
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if(AddShow == Constants.MOPUB)
			if (moPubView != null) 
				moPubView.destroy();
	}

	@Override
	protected void onPause() {
		super.onPause();		
		if(pager == null) {
			try {
	        	new LoadContentTask().execute(getSupportFragmentManager());
	        }catch(Exception e) {
	        	
	        }
		}
	}
	
	

	private class LoadContentTask extends AsyncTask<FragmentManager, Object, FragmentPagerAdapter> {
		
	    @Override
	    protected FragmentPagerAdapter doInBackground(FragmentManager... arg) {
	    	super.onPreExecute();
	    	FragmentPagerAdapter frgadapter = new GagsViewerInnerAdapter(arg[0]);
	    	
	    	return frgadapter;
	    }
	    
	    @Override
	    protected void onPreExecute() {
		    // TODO Auto-generated method stub
		    super.onPreExecute();;
	    	progressBar.setVisibility(View.VISIBLE);
	    }
	    
	    @Override
	    protected void onPostExecute(FragmentPagerAdapter adapter) {
	        progressBar.setVisibility(View.GONE);
	        pager.setAdapter(adapter);
	        indicator.setViewPager(pager);
	        pager.setOffscreenPageLimit(4);
	        pager.setPageMargin(25);    	

	        
	        
	        try {
        	//	    MobileCore
        			MobileCore.init(activity, Constants.DEV_HASH,  LOG_TYPE.DEBUG, AD_UNITS.ALL_UNITS);
        	//		MobileCore
		        			
		        			
	    			moPubView.setAdUnitId(Constants.MOPUB_ID);
				    moPubView.loadAd();
				    moPubView.setOnAdLoadedListener(new OnAdLoadedListener() {
						
						@Override
						public void OnAdLoaded(MoPubView arg0) {
							// TODO Auto-generated method stub
							moPubView.setVisibility(View.VISIBLE);
						}
					});
				    moPubView.setOnAdFailedListener(new OnAdFailedListener() {
						
						@Override
						public void OnAdFailed(MoPubView error) {
							// TODO Auto-generated method stub
//							Toast.makeText(getApplicationContext(), "Ad successfully failed.", Toast.LENGTH_SHORT).show();
							moPubView.setVisibility(View.GONE);
						}
					});
				}catch(Exception e) {
					e.printStackTrace();
				}
	        
	        
	    }
	    
	}

	@Override
	public void onBackPressed() {
		MobileCore.showOfferWall(this, new CallbackResponse() { //    MobileCore
			@Override
			public void onConfirmation(TYPE type) {
				finish();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
	    subMenu = menu.addSubMenu(0, 0, 1, "");//002''

	    subMenu.add(0, 2, 2, R.string.menu_clear_cache)
	    .setIcon(R.drawable.ic_menu_delete_red); //.setIcon(R.drawable.ic_delete);
	    
//	    subMenu.add(0, 3, 3, R.string.action_settings)
//	    .setIcon(R.drawable.ic_menu_preferences);//option
	    
//	    subMenu.add(0, 3, 3, R.string.search)
//	    .setIcon(R.drawable.ic_search);
	    
	    
	    subMenu.add(0, 4, 4, R.string.about)
	    .setIcon(R.drawable.ic_menu_info_details);//ic_dialog_info
	    	    
	    subMenu.add(0, 5, 5, R.string.exit)
	    .setIcon(R.drawable.ic_lock_power_off);
	    
	    
	    menuItem = subMenu.getItem();
	    menuItem.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
	    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	    return super.onCreateOptionsMenu(menu);
	}
    

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case 0:
				break;
				
			case 2:
				imageLoader  = new ImageLoader(getApplicationContext());
				imageLoader.clearCache();
				break;
				
//			case 3:
//				startActivity(new Intent(GagsViewer.this, Preference.class));
////				startActivity(new Intent(GagsViewer.this, Search.class));
//				break;
				
			case 4:
				pager.setCurrentItem(4);
				break;
				
			case 5:			      
					MobileCore.showOfferWall(this, new CallbackResponse() { //    MobileCore
						@Override
						public void onConfirmation(TYPE type) {
							finish();
						}
					});
		      
				break;
				
			default:
				return false;
		}
		return true;
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    if (Constants.DEVICE_VERSION < Constants.DEVICE_HONEYCOMB) {
	        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_MENU) {
	        	menu.performIdentifierAction(subMenu.getItem().getItemId(), 0);
	            return true;
	        }
	    }
	    return super.onKeyUp(keyCode, event);
	}
	

	/**
	 * ViewPager Adapter. Loads Images, Animations, Videos, Jokes & GagsInfo
	 * */
	public class GagsViewerInnerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {


		public GagsViewerInnerAdapter(FragmentManager fm) {
			super(fm);
		}

		Fragment frg = new Fragment();
		
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			
				case 0:
					moPubView.setVisibility(View.VISIBLE);
					 frg = GagsImageFragment.newInstance(CONTENT[position % CONTENT.length]);
				break;
				
				case 1:
					moPubView.setVisibility(View.VISIBLE);
					 frg = GagsAnimationFragment.newInstance(CONTENT[position % CONTENT.length]);
				break;
					
				 case 2:
					 moPubView.setVisibility(View.VISIBLE);
					  frg = GagsVideoFragment.newInstance(CONTENT[position % CONTENT.length]);
				 break;
				 
				 case 3:
					 moPubView.setVisibility(View.VISIBLE);
					 frg = GagsJokesFragment.newInstance(CONTENT[position % CONTENT.length]);
				 break;
				 
				 case 4:
					 moPubView.setVisibility(View.GONE);
					 frg = GagsInfoFragment.newInstance(CONTENT[position % CONTENT.length]);
				 break;
				 
				default:
					break;
					
			}
		
			return frg;
		}

		
		@SuppressLint("DefaultLocale")
		@Override
		public CharSequence getPageTitle(int position) {
			Log.d("CONTENT",CONTENT[position % CONTENT.length].toUpperCase());
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getIconResId(int index) {
			return ICONS[index];
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}


	/**
	 * Notification Builder
	 **/
//	private void makeNotification() {
//		// TODO Auto-generated method stub
//		NotificationCompat.Builder mBuilder =
//			    new NotificationCompat.Builder(this)
//			    .setSmallIcon(R.drawable.icon)
//			    .setContentTitle("My notification")
//			    .setContentText("Hello World!");
//		        
//		        
//		Intent resultIntent = new Intent(this, VideoViewer.class);
//		 // Because clicking the notification opens a new ("special") activity, there's
//		 // no need to create an artificial back stack.
//		 PendingIntent resultPendingIntent =
//		     PendingIntent.getActivity(
//		     this,
//		     0,
//		     resultIntent,
//		     PendingIntent.FLAG_UPDATE_CURRENT
//		 );
//		 mBuilder.setContentIntent(resultPendingIntent);
//		        
//		 // Sets an ID for the notification
//		int mNotificationId = 001;
//		// Gets an instance of the NotificationManager service
//		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		// Builds the notification and issues it.
//		mNotifyMgr.notify(mNotificationId, mBuilder.build());
//	}
}