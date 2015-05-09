package com.togrulseyid.gags.viewers;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase.DisplayType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.togrulseyid.gags.libs.Constants;
import com.togrulseyid.gags.libs.FileCache;
import com.togrulseyid.gags.libs.MemoryCache;
import com.togrulseyid.gags.libs.Utils;
import com.togrulseyid.gags.operations.Utility;
import com.togrulseyid.gags.viewer.R;
import com.togrulseyid.gags.viewers.dialogs.FileDialog;
import com.togrulseyid.gags.viewers.dialogs.SelectionMode;

public class ImageViewer extends ActionBarActivity {
	
	private ImageViewTouch imageViewTouch;
	private String source_txt, subject;
	private ImageViewer imageViewer ;
	private Intent intent;
	private int loader;
	private File file ;
	private FileCache fileCache2 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			 
		setContentView(R.layout.imageview);

		imageViewTouch = (ImageViewTouch) findViewById(R.id.ImageViewTouch);
		imageViewTouch.setDisplayType(DisplayType.FIT_TO_SCREEN );
		
		intent = getIntent();
		source_txt = intent.getStringExtra(Constants.TAG_SRC);
		subject = intent.getStringExtra(Constants.TAG_ALT);
		
		loader = R.drawable.loader;
		imageViewer = new ImageViewer();
		imageViewer.ImageLoader(getApplicationContext());
		imageViewer.DisplayImage(source_txt, loader, imageViewTouch);
		getSupportActionBar().setTitle(subject);	
		
		
		if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getSupportActionBar().hide();
		}
	}
	
	
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_images, menu);
       
        try{
	        fileCache2 = new FileCache(getApplicationContext());
	        fileCache2.clear(source_txt);
        }catch(Exception e){
        	
        }
        
        return true;
    }

//	private Uri getURI(String source_txt) {
//		if(fileCache2==null)
//			fileCache2 = new FileCache(getApplicationContext());
//
//		try {
//			file = fileCache2.getSharedFile(source_txt, FileCache.SHARED_PNG_FILE_NAME);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		if (file == null){
//			try {
//				file = getFromURL(source_txt);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return Uri.fromFile(file);
//	}
    
	public File getFromURL(String source_txt) throws IOException {
		URL url = new URL(source_txt);
		
		file = fileCache2.getSharedFile();
		file = fileCache2.getSharedFile(source_txt, FileCache.SHARED_PNG_FILE_NAME);
		
		URLConnection ucon = url.openConnection();
		InputStream is = ucon.getInputStream();
		
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
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		    case android.R.id.home:
		    	finish();
		    	return true;
		    case R.id.share_image:
		    	
		    	Utility.shareData(this, subject, subject, source_txt, null, "Share");
		    	
		    	
		    	return true;
		    	
		    case R.id.save_image:
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
//		intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory()+ File.pathSeparator+ "mnt"+File.pathSeparator + "sdcard");

        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_CREATE);
        startActivityForResult(intent, Constants.REQUEST_SAVE);
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
	        createFile(filePath); // Writing to file if posible
	        
        } else if (resultCode == Activity.RESULT_CANCELED) {
        }
    }



	private void createFile(String fileName) {
		fileName = fileName + ".png";
		if(isExternalStorageWritable()) {
			if(fileCache2==null)
				fileCache2 = new FileCache(getApplicationContext());
				
			try {
//				copyFile(new File(fileCache2.getFileDirectory()+ File.separator + FileCache.SHARED_PNG_FILE_NAME), new File(fileName));
				copyFile(fileCache2.getFile(source_txt), new File(fileName));
				makeDialog("Okkay.", "Image saved as "+fileName , R.drawable.ok, "09a014");
			} catch (Exception e) {
				makeDialog("Error Occured.", e.getMessage() , R.drawable.error,"edbe19");
			       e.printStackTrace();
			}
	
		} else {
			makeDialog("Warning.", "Can not write to External Storage. Sorry Man Storage is not writable :(", R.drawable.warning, "E92828");     
		}
	}
	
	
	
	@SuppressWarnings("resource")
	private void copyFile(File src, File dst) throws IOException {
		if(dst.exists())
			dst.delete();
		
       FileChannel inChannel = new FileInputStream(src).getChannel();
       FileChannel outChannel = new FileOutputStream(dst).getChannel();
       try {
          inChannel.transferTo(0, inChannel.size(), outChannel);
       } finally {
          if (inChannel != null)
             inChannel.close();
          if (outChannel != null)
             outChannel.close();
       }
    }
	
	
	
	
	@SuppressWarnings("deprecation")
	public void makeDialog(String title, String message, int id, String color) {
		final AlertDialog alertDialog = new AlertDialog.Builder(ImageViewer.this).create();
		
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
	
	
//========================================================================================
	
	
	
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
	
	/******************************************
	 * *
	 * * ImageView With Zoom Effect
	 * *
	 ******************************************/
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public void ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	int stub_id = R.drawable.icon;

	public void DisplayImage(String url, int loader, ImageViewTouch imageView) {
		stub_id = loader;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
//			imageView.setMaxZoom(5f); // ImageViewTouch Zoom
		}else {
			queuePhoto(url, imageView);
			imageView.setImageResource(loader);
		}
	}

	private void queuePhoto(String url, ImageViewTouch imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
//			final int REQUIRED_SIZE = 700;
			final int REQUIRED_SIZE = 1000;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageViewTouch imageView;

		public PhotoToLoad(String u, ImageViewTouch i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}
}
