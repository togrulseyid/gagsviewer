package com.togrulseyid.gags.viewers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.ironsource.mobilcore.CallbackResponse;
import com.ironsource.mobilcore.MobileCore;
import com.ironsource.mobilcore.MobileCore.AD_UNITS;
import com.ironsource.mobilcore.MobileCore.LOG_TYPE;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.youtube.DeveloperKey;
import com.togrulseyid.gags.viewers.dialogs.FileDialog;
import com.togrulseyid.gags.viewers.dialogs.SaveYoutubeVideoAsynTask;
import com.togrulseyid.gags.viewers.dialogs.SelectionMode;
import com.togrulseyid.gags.viewer.R;

public class VideoViewer extends SherlockFragmentActivity {
	
	private String YoutubeLink, YoutubeTitle;
	private ShareActionProvider mShareActionProvider ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);			
		setContentView(R.layout.youtube_player);		
		if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e("On Config Change", "LANDSCAPE");
			getSupportActionBar().hide();
		}
		
        
		Intent in = getIntent();
		YoutubeLink = in.getStringExtra( Constants.TAG_URL );
		YoutubeTitle = in.getStringExtra( Constants.TAG_ALT );
		
		FragmentManager fragmentManager = getSupportFragmentManager();
	    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

	    YouTubePlayerSupportFragment fragment = new YouTubePlayerSupportFragment();
	    fragmentTransaction.add(R.id.fragmentz, fragment);	    
	    fragmentTransaction.commit();
	    fragment.setMenuVisibility(true);
	    
	    Toast.makeText(getApplicationContext(), "For Full Screen turn phone horizontally :)", Constants.TOAST_TIME).show();
	    
	   
	    fragment.initialize(DeveloperKey.DEVELOPER_KEY, new OnInitializedListener() {
            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
            	player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            	player.getFullscreenControlFlags();
            	player.setPlayerStyle(PlayerStyle.CHROMELESS);
            	player.setShowFullscreenButton(true);
                if (!wasRestored) {
                	player.loadVideo(YoutubeLink);
                }
                else
                	player.cueVideo("6CHs4x2uqcQ");
            }

            @Override
            public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
            }
        }); 
	    

//      MobileCore
		MobileCore.init(this, Constants.DEV_HASH,  LOG_TYPE.DEBUG, AD_UNITS.ALL_UNITS);
	}
	
	
//  MobileCore
    @Override
    public void onBackPressed() {
	      MobileCore.showOfferWall(this, new CallbackResponse() {
		      @Override
		      public void onConfirmation(TYPE type) {
		    	  finish();
		      }
	      });
    }
    
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Log.e("On Config Change", "LANDSCAPE");
			getSupportActionBar().hide();
		} else {
			Log.e("On Config Change", "PORTRAIT");
			getSupportActionBar().show();
		}
	}
 
	 
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.video, menu);
        MenuItem item = menu.findItem(R.id.share_video);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        mShareActionProvider.setShareIntent(createShareIntent(YoutubeTitle, YoutubeLink));
	    
        return true;
    }
	    
    
	 // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    

    @SuppressLint("InlinedApi")
	private Intent createShareIntent(String title, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TITLE, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://youtu.be/"+ text);
        return shareIntent;
    }

    
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		    case android.R.id.home:
		    	this.onBackPressed();
//		    	finish();
		    	return true;
		    case R.id.share_video:
		    	setShareIntent(new Intent(Intent.ACTION_SEND));
		    	return true;
		    	
		    case R.id.save_video:
		    	saveFile();
		    	return true;
		    default: 
		    	return super.onOptionsItemSelected(item);  
		}
	}


	private void saveFile(String filename, String youtubeLink) {
    	Intent intent = new Intent(VideoViewer.this, SaveYoutubeVideoAsynTask.class);
    	intent.putExtra(Constants.TAG_NAME, filename);
    	intent.putExtra(Constants.TAG_URL, youtubeLink);
    	startActivity(intent);
	}
	
	
	@Override
	public synchronized void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
	        if (requestCode == Constants.REQUEST_SAVE) {
                System.out.println("Saving...");
                System.out.println(data.getStringExtra(FileDialog.RESULT_PATH));
	        } else if (requestCode == Constants.REQUEST_LOAD) {
	                System.out.println("Loading...");
	        }
	        
	        String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
	        saveFile(filePath, YoutubeLink); // Writing to file if posible
	        
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }
	
	@SuppressLint("SdCardPath")
	private void saveFile() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard");
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_CREATE);
        startActivityForResult(intent, Constants.REQUEST_SAVE);
	}
}