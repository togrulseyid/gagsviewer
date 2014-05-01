package com.togrulseyid.gags.viewers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.FileCache;
import com.togrulseyid.gags.libs.MemoryCache;
import com.togrulseyid.gags.viewers.dialogs.FileDialog;
import com.togrulseyid.gags.viewers.dialogs.SelectionMode;
import com.togrulseyid.gags.viewer.R;

public class AnimationViewer extends SherlockActivity {

	private ProgressBar progressBar;
	private static final int maxProges = 600;
	private static final int REQUEST_SAVE = 0 ;
	private static final int REQUEST_LOAD = 1 ;
	private ShareActionProvider mShareActionProvider ;
	private URLConnection urlConnection;
	private File file ;
	private String src ;
	private String alt ;
	private FileCache fileCache;
	private boolean isDdownload = false;
	private Intent in;
	private Context context;
	private GifDecoder mGifDecoder  = null;
    private boolean mIsPlayingGif = false;
    private boolean sleep = true;
    private Bitmap mTmpBitmap;
    
	
    /**
     * Start point
     * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(savedInstanceState);	
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportActionBar().hide();
		} else {
			getSupportActionBar().show();
		}
		
		context  = getApplication().getApplicationContext();
		
		in = getIntent();
		alt = in.getStringExtra(Constants.TAG_ALT);
		src = in.getStringExtra(Constants.TAG_SRC);

		Log.d(Constants.TAG_SRC, ":"+src);
		Log.d(Constants.TAG_ALT, ":"+alt);
		
		
		new MainTask(context).execute(src);

	}



	/**
	 * if orientaion changes what to do?
	 * */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		if(!isDdownload)
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				getSupportActionBar().hide();
			} else {
				getSupportActionBar().show();
			}
		super.onConfigurationChanged(newConfig);
	}
	
    /**
     * Action Bar Sherlock Menu creation
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.animations, menu);
        MenuItem item = menu.findItem(R.id.share_animation);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        mShareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        mShareActionProvider.setShareIntent(createShareIntent(alt, src));
        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Action Bar Sherlock Menu Items Clicked events
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		    case android.R.id.home:
		    	finish();
		    	return true;
		    case R.id.share_animation:
		    	setShareIntent(new Intent(Intent.ACTION_SEND));
		    	return true;
		    	
		    case R.id.save_animation:
		    	saveFile();
		    	return true;
		    default: 
		    	return super.onOptionsItemSelected(item);  
		}
	}
	
	/**
	 * Deletes unused instances onDestroy
	 * */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try{
			mGifDecoder.clear();
			mIsPlayingGif = false;
			sleep = false;
			
			if(!mTmpBitmap.isRecycled())
				mTmpBitmap.recycle();
			context = null;

			try {
				this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Check if local file size same as server side file
	 * */
	private boolean isFullGif(File f, String src) {
		if(f != null){
			// TODO Auto-generated method stub
			int fileSizeAtServer = 0;
			long fileSize = f.length();
			f.length();
			try {
				fileSizeAtServer = sizeOfFile(src);
			} catch (IOException e) {
				e.printStackTrace();
				fileSizeAtServer = 0;
			}
			
			if((fileSize == fileSizeAtServer) || (fileSize > fileSizeAtServer)) {
				return true;
			}else{
				return false;
			}
		}		
		
		return false;
	}
	
	/**
	 * Gets File size in server
	 * */
	private int sizeOfFile(String uri) throws IOException {
		// TODO Auto-generated method stub
		URL url = new URL(uri);
		urlConnection = url.openConnection();
		urlConnection.connect();
		return urlConnection.getContentLength();
	}
		
	/**
	 * Reads bytes from FILE
	 * */
	public  byte[] readFile(File file) throws IOException {
		// TODO Auto-generated method stub
        
        RandomAccessFile f = new RandomAccessFile(file, "r");// Open file
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }
	    
    /**
     * Call to update the share intent
     * */
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
    
    /**
     * Call to create the share intent
     * */
	private Intent createShareIntent(String title, String source_txt) {	
		// TODO Auto-generated method stub
    	Intent share = new Intent(Intent.ACTION_SEND);
    	share.setType("image/*");
    	share.putExtra(Intent.EXTRA_STREAM, getURI(source_txt));
    	share.putExtra(android.content.Intent.EXTRA_SUBJECT, title); 
    	share.putExtra(android.content.Intent.EXTRA_TITLE, title);
    	share.putExtra(android.content.Intent.EXTRA_TEXT, title);
    	share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		return share;
    }
	
	/**
	 * Gets local FILE URI from source_txt 
	 * */
	private Uri getURI(String source_txt) {
		// TODO Auto-generated method stub
		if(fileCache==null)
			fileCache = new FileCache(context);

		try {
			file = fileCache.getSharedFile(source_txt, FileCache.SHARED_GIF_FILE_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (file == null){
			try {
				file = getFromURL(source_txt);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return Uri.fromFile(file);
	}
	
	/**
	 * Download FILE from url string
	 * */
	public File getFromURL(String source_txt) throws IOException {
		// TODO Auto-generated method stub
		URL url = new URL(source_txt);
		
		try{
			file = fileCache.getSharedFile();
			if(file.exists())
				file.delete();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		urlConnection = url.openConnection();
		InputStream is = urlConnection.getInputStream();
		
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayBuffer baf = new ByteArrayBuffer(50);
		
		int current = 0;
		while ((current = bis.read()) != -1) {
			baf.append((byte) current);
		}
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(baf.toByteArray());
		fos.close();	
		
		return file;
	}
   
	/**
	 * Save File menu Operations
	 * */
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
	
	/**
	 * Open File Dialog Result
	 * @return Path of file
	 * */
	@Override
	public synchronized void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
	        if (requestCode == REQUEST_SAVE) {
                System.out.println("Saving...");
                System.out.println(data.getStringExtra(FileDialog.RESULT_PATH));
	        } else if (requestCode == REQUEST_LOAD) {
	                System.out.println("Loading...");
	        }
	        
	        String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
	        createFile(filePath); // Writing to file if posible
	        
        } else if (resultCode == Activity.RESULT_CANCELED) {}
    }
	
	/**
	 * Creates FILE from given path and show appropriate message dialog 
	 * */
	private void createFile(String fileName) {
		fileName = fileName + ".gif";
		if(isExternalStorageWritable()) {
			if(fileCache==null)
				fileCache = new FileCache(context);
				
			try {
				copyFile(fileCache.getFile(src), new File(fileName));
				makeDialog("Okkay.", "Image saved as "+fileName , R.drawable.ok, "09a014");
			} catch (Exception e) {
				makeDialog("Error Occured.", e.getMessage() , R.drawable.error,"edbe19");
			       e.printStackTrace();
			}
	
		} else {
			makeDialog("Warning.", "Can not write to External Storage. Sorry Man Storage is not writable :(", R.drawable.warning, "E92828");     
		}
	}
	
	/**
	 * Copies one FILE to another FILE
	 * */
	@SuppressWarnings("resource")
	private void copyFile(File srcFile, File dstFile) throws IOException {
		if(dstFile.exists())
			dstFile.delete();
		
		if(srcFile == null) {
			srcFile = getFromURL( src ) ;
		}

		
       FileChannel inChannel = new FileInputStream(srcFile).getChannel();
       FileChannel outChannel = new FileOutputStream(dstFile).getChannel();
       try {
          inChannel.transferTo(0, inChannel.size(), outChannel);
       } finally {
          if (inChannel != null)
             inChannel.close();
          if (outChannel != null)
             outChannel.close();
       }
    }
	
	/**
	 * Message Dialog show messages with negative and positive styles
	 * */
	public void makeDialog(String title, String message, int id, String color) {
		final AlertDialog alertDialog = new AlertDialog.Builder(AnimationViewer.this).create();		
		WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();  
		lp.dimAmount=0.0f;  
		alertDialog.getWindow().setAttributes(lp);  
		alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle(Html.fromHtml("<h1><font color='#"+color+"'><b>"+title+"<b></font></h1>"));
        alertDialog.setIcon(id);
        alertDialog.setMessage(Html.fromHtml("<strong>"+message+"</strong>"));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, (CharSequence)"Close", new AlertDialog.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				alertDialog.hide();
			}
		});

//		alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);		
//		alertDialog.getWindow().addFlags(WindowManager.LayoutParams.DIM_AMOUNT_CHANGED);		
//		alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        alertDialog.show(); 
	}
	
	/**
	 * Checks if external storage is available for read and write
	 * */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * DownloadFilesTask downloads GIF FILE from Internet 
	 * Converts InputStream to FILE and Writes it to Cache
	 * Shows GIF with help of AnimationDecoderView.class
	 * */
	private class DownloadFilesTask extends AsyncTask<String, Integer, InputStream> {
		Context context;
		
		public DownloadFilesTask (Context context) {
			this.context = context;
		}
		
		@Override
		protected InputStream doInBackground(String... urls) {
			long total = 0;
			MemoryCache memoryCache ;
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				int lenghtOfFile = sizeOfFile(urls[0]);
				URL url = new URL(urls[0]);
				byte[] chunk = new byte[1024];// 4096
				int bytesRead;
				InputStream stream = url.openStream();
				while ((bytesRead = stream.read(chunk)) > 0) {
					outputStream.write(chunk, 0, bytesRead);
					total += bytesRead;
					progressBar.setProgress((int) ((total * maxProges) / lenghtOfFile));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					memoryCache = new MemoryCache();
					memoryCache.put(urls[0], BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, (outputStream.toByteArray()).length));
				} finally {
					memoryCache = null;
				}
			
			}
			return new ByteArrayInputStream(outputStream.toByteArray());
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			System.out.println("progres: " + progress[0]);
			progressBar.setProgress(progress[0]);
			super.onProgressUpdate(progress);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			setContentView(R.layout.progressbar);
			progressBar = (ProgressBar) findViewById(R.id.progressBar);
			progressBar.getProgressDrawable().setColorFilter(Color.GREEN, Mode.SRC_IN);
			progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY );
			progressBar.setMax(maxProges);
			super.onPreExecute();
		}


		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(InputStream inputStream) {
			// TODO Auto-generated method stub
			super.onPostExecute(inputStream);
			
			isDdownload = false;
			if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				getSupportActionBar().hide();
			} else {
				getSupportActionBar().show();
			}
			
			try {
				setContentView(new AnimationDecoderView(context, inputStream));
				invalidateOptionsMenu();
//				System.gc();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d("DownloadFilesTask", "onPostExecute ended ");
		}
	}
	
	/**
	 * Gets InputStream from local cache FILE 
	 * Shows GIF images with help of AnimationDecoderView.class 
	 * */
	private class ShowTask extends AsyncTask<File, Integer, InputStream>  {
		InputStream is;
		Context context;
		
		public ShowTask (Context context) {
			this.context = context;
		}
		
		@Override
		protected InputStream doInBackground(File... file) {
			try{
				is = new FileInputStream(file[0]);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return is;
		}
		
		@Override
		protected void onPostExecute(InputStream result) {
			// TODO Auto-generated method stub
			
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				getSupportActionBar().hide();
			} else {
				getSupportActionBar().show();
			}
			try{
				setContentView(new AnimationDecoderView(context, result));
				if(!getSupportActionBar().isShowing())
					getSupportActionBar().show();
			}catch(Exception e){
			}
			super.onPostExecute(result);
		}
		
	}
	
	/**
	 * GIF VIEWER Decodes GIF images 
	 * Splits it to frames 
	 * Shows frames and sleeps some time
	 * */
	
	
	Runnable myRun;
	public class AnimationDecoderView extends ImageView {
		
	    final Handler mHandler = new Handler();
	
	    final Runnable mUpdateResults = new Runnable() {
	        public void run() {
	            if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
	            	AnimationDecoderView.this.setImageBitmap(mTmpBitmap);
	            }
	        }
	    };
	
	    public AnimationDecoderView(Context context, InputStream stream) {
	        super(context);
	        playGif(stream);
	    }
	    	
	    private void playGif(InputStream stream) {
	        mGifDecoder = new GifDecoder();

	        try{
		        mGifDecoder.read(stream);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	
	        mIsPlayingGif = true;
			try{
		        myRun = new Runnable() {
		            public void run() {
		                final int n = mGifDecoder.getFrameCount();
		                final int ntimes = mGifDecoder.getLoopCount();
		                int repetitionCounter = 0;
		                do {
							if(sleep) {
			                    for (int i = 0; i < n; i++) {
			                        mTmpBitmap = mGifDecoder.getFrame(i);
			                        int t = mGifDecoder.getDelay(i);
			                        mHandler.post(mUpdateResults);
			                        
			                        try {
			                        	if(sleep) {
			                        		Thread.sleep(t);
			                        	} else
			                        		break;
			                        } catch (Exception e) {
			                            e.printStackTrace();
			                        }
			                    }
			                    
			                    if(ntimes != 0) {
			                        repetitionCounter ++;
			                    }
		                	}
	
		                } while (mIsPlayingGif && (repetitionCounter <= ntimes));
		            }
		        };
				new Thread(myRun ).start();
			}catch (Exception e) {
				Log.d(VIEW_LOG_TAG, e.toString());
			}
	    }
	    
	    public void stopRendering() {
	        mIsPlayingGif = true;
	    }
	}

	
	private class MainTask extends AsyncTask<String, Integer, Boolean> {
		Context context;
		File file;
		
		public MainTask(Context context) {
			this.context = context;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			src = params[0];
			fileCache = new FileCache(context);
			file = fileCache.getFile(src);

			if(file != null && isFullGif(file, src)){
				return true;
			}else{
				return false;
			}
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			setContentView(R.layout.test);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			setContentView(R.layout.test);
			
			// TODO Auto-generated method stub
			if(result){
				try {
					if(getSupportActionBar().isShowing())
						getSupportActionBar().hide();
					new ShowTask(context).execute(file);
//					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				isDdownload = true;
				if(getSupportActionBar().isShowing())
					getSupportActionBar().hide();
				try {
					new DownloadFilesTask(context).execute(src);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
	}
	
	
}