package com.togrulseyid.gags.viewers;

import java.io.FileOutputStream;
import java.io.PrintStream;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.viewers.dialogs.FileDialog;
import com.togrulseyid.gags.viewers.dialogs.SelectionMode;
import com.togrulseyid.gags.viewer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ZoomControls;

public class JokeViewer extends SherlockActivity {
	
	private static final int REQUEST_SAVE = 0;
	private static final int REQUEST_LOAD = 1;
	private ShareActionProvider mShareActionProvider;
	private  String title;
	private  String text;
	private TextView bunchOfText ;
	ZoomControls buttonPlusMinus ;
	private int textSize = 20;
	private Typeface font;
	private Spanned spanned;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overlay);

		Intent in = getIntent();
		title = in.getStringExtra(Constants.TAG_TITLE);
		text = in.getStringExtra(Constants.TAG_TEXT);

		
		Log.d(Constants.TAG_TITLE, "xx:"+title);
		Log.d(Constants.TAG_TEXT, "xx:"+text);
		
		spanned = Html.fromHtml(text);
		
		font = Typeface.createFromAsset(getApplicationContext().getAssets(), "ttf/angrybirds_regular.ttf");
		
		/*
		title = title.replace("'/", "'");
		title = title.replace("\"/", "\"");
		text = text.replace("'/", "'");
		text = text.replace("\"/", "\"");
		*/

		// Load partially transparent black background
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_bg_black)); // ab_bg_black
		getSupportActionBar().setTitle(title);
		getSupportActionBar().setLogo(R.drawable.icon);
		
		
		if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportActionBar().hide();
		}

		bunchOfText = (TextView) findViewById(R.id.bunch_of_text);		
		buttonPlusMinus = (ZoomControls) findViewById(R.id.zoomControls);
		
		bunchOfText.setTypeface(font);
		bunchOfText.setText(spanned);
		bunchOfText.setTextSize(textSize);
		
		
		

		buttonPlusMinus.setOnZoomInClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(textSize<=72)
					bunchOfText.setTextSize(++textSize);
			}
		});

		buttonPlusMinus.setOnZoomOutClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(textSize>=8)
					bunchOfText.setTextSize(--textSize);
			}
		});
	}
	
	
	
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.jokes, menu);
       
        MenuItem item = menu.findItem(R.id.share_jokes);
		ShareActionProvider provider = (ShareActionProvider) item.getActionProvider();
		provider.setShareIntent(createShareIntent(title, title+"\n\n"+text ));
		    
        return true;
    }
    
	// Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    
    private Intent createShareIntent(String title, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TITLE, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        return shareIntent;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		    case android.R.id.home:
		    	finish();
		    	return true;
		    case R.id.share_jokes:
		    	setShareIntent(new Intent(Intent.ACTION_SEND));
		    	return true;
		    	
		    case R.id.save_jokes:
		    	saveFile();
		    	return true;
		    default: 
		    	return super.onOptionsItemSelected(item);  
		}
	}
	
	@SuppressLint("SdCardPath")
	private void saveFile() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard");
//		intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory()+ File.pathSeparator + "sdcard");
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_CREATE);
        startActivityForResult(intent, REQUEST_SAVE);
	}
	
	
	@Override
	public synchronized void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
	        if (requestCode == REQUEST_SAVE) {
//                System.out.println("Saving...");
	        } else if (requestCode == REQUEST_LOAD) {
//	                System.out.println("Loading...");
	        }
	        
	        String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
	        createFile(filePath); // Writing to file if posible
	        
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }



	private void createFile(String fileName) {
		fileName = fileName + ".txt";
		// TODO Auto-generated method stub
		if(isExternalStorageWritable()) {
			try {
				PrintStream out = null;
				try {
					out = new PrintStream(new FileOutputStream(fileName));
					out.print("Title:\n"+title + "\n\n\nBody:\n" + text+"\n");
				}
				finally {
					if (out != null) out.close();
						makeDialog("Okkay.", "File saved as "+fileName , R.drawable.ok, "09a014");
				}
			} catch (Exception e) {
				makeDialog("Error Occured.", e.getMessage() , R.drawable.error,"edbe19");
			       e.printStackTrace();
			}
			
			
		} else {
			makeDialog("Warning.", "Can not write to External Storage. Sorry Man Storage is not writable :(", R.drawable.warning, "E92828");     
		}
	}
	
	@SuppressWarnings("deprecation")
	public void makeDialog(String title, String message, int id, String color) {
		final AlertDialog alertDialog = new AlertDialog.Builder(JokeViewer.this).create();
		
		WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();  
		lp.dimAmount=0.0f;  
		alertDialog.getWindow().setAttributes(lp);  
		alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); 
        alertDialog.setTitle(Html.fromHtml("<h1><font color='#"+color+"'><b>"+title+"<b></font></h1>"));
        alertDialog.setMessage(Html.fromHtml("<strong>"+message+"</strong>"));
        alertDialog.setIcon(id);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) 
            {
            	alertDialog.hide();
            }
        });

        alertDialog.show(); 
	}
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
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
}