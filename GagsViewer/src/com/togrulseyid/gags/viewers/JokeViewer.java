package com.togrulseyid.gags.viewers;

import java.io.FileOutputStream;
import java.io.PrintStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.togrulseyid.gags.constants.Constants;
import com.togrulseyid.gags.operations.Utility;
import com.togrulseyid.gags.viewer.R;
import com.togrulseyid.gags.viewers.dialogs.FileDialog;
import com.togrulseyid.gags.viewers.dialogs.SelectionMode;

public class JokeViewer extends ActionBarActivity {
	
	private static final int REQUEST_SAVE = 0;
	private static final int REQUEST_LOAD = 1;
	private  String title;
	private  String text;
	private TextView bunchOfText ;
	private ZoomControls buttonPlusMinus ;
	private int textSize = 20;
	private Typeface font;
	private Spanned spanned;
	private Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joke_viewer);

		bundle = getIntent().getExtras();
		title = bundle.getString(Constants.TAG_TITLE);
		text = bundle.getString(Constants.TAG_TEXT);

		if (title == null || text == null) {
			finish();
		}
		
		spanned = Html.fromHtml(text);
		
		font = Typeface.createFromAsset(getApplicationContext().getAssets(), "ttf/angrybirds_regular.ttf");
		
		// Load partially transparent black background
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Html.fromHtml(title));
		
		
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
				if(textSize<=72)
					bunchOfText.setTextSize(++textSize);
			}
		});

		buttonPlusMinus.setOnZoomOutClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
		getMenuInflater().inflate(R.menu.jokes, menu);
		return true;
	}
    
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case android.R.id.home:
		    	finish();
		    	return true;
		    case R.id.share_jokes:

				Utility.shareData(this, title, title, text, null, "Share");
				
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
		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportActionBar().hide();
		} else {
			getSupportActionBar().show();
		}
	}
}