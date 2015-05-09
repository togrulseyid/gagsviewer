package com.togrulseyid.gags.viewers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.youtube.DeveloperKey;
import com.togrulseyid.gags.operations.Utility;
import com.togrulseyid.gags.viewer.R;
import com.togrulseyid.gags.viewers.dialogs.FileDialog;
import com.togrulseyid.gags.viewers.dialogs.SaveYoutubeVideoAsynTask;
import com.togrulseyid.gags.viewers.dialogs.SelectionMode;

public class VideoViewer extends ActionBarActivity {
	
	private String youtubeLink, youtubeTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);			
		setContentView(R.layout.youtube_player);		
		if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportActionBar().hide();
		}
		
        
		Intent in = getIntent();
		youtubeLink = in.getStringExtra( Constants.TAG_URL );
		youtubeTitle = in.getStringExtra( Constants.TAG_ALT );
		
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
                	player.loadVideo(youtubeLink);
                }
                else
                	player.cueVideo("6CHs4x2uqcQ");
            }

            @Override
            public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
            }
        }); 
	    
	}
	
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportActionBar().hide();
		} else {
			getSupportActionBar().show();
		}
	}
 
	 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.video, menu);
        return true;
    }
	    
    

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case android.R.id.home:
		    	this.onBackPressed();
		    	return true;
		    case R.id.share_video:
		    	Utility.shareData(this, youtubeTitle, youtubeTitle, "http://youtu.be/"+ youtubeLink, null, "Share");
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
	        saveFile(filePath, youtubeLink); // Writing to file if posible
	        
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