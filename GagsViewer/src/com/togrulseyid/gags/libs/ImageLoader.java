package com.togrulseyid.gags.libs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.togrulseyid.gags.viewer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class ImageLoader extends Activity{
	
	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;
	private Context context;
	private int stub_id = R.drawable.icon;
	private int type;
	
	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}


	public void DisplayImage(String url, int loader, final ImageView imageView, int type) {
		// TODO Auto-generated method stub
		this.type = type; 
		stub_id = loader;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			queuePhoto(url, imageView);
			imageView.setImageResource(loader);
		}
	}
	

	private void queuePhoto(String url, ImageView imageView) {
		// TODO Auto-generated method stub
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		// TODO Auto-generated method stub
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
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(new PlurkInputStream2(is), os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}


	// decodes image and scales it to reduce memory consumption
	@SuppressLint("InlinedApi")
	private Bitmap decodeFile(File f) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		
		int width;
		int height;
		if(PORTRAIT_ORIENTATION == 1 || PORTRAIT_ORIENTATION == 7) {
			width = metrics.widthPixels;
			height = metrics.heightPixels;	
		}else{
			height = metrics.widthPixels;
			width = metrics.heightPixels;	
		}
		
		// type   0 image;  1 animation;  2 video; 
		switch (type) {
			case 0:
				bitmap = Image(f, width , height );
				break;
			case 1:
				bitmap = Animation(f, width , height );
				break;
			case 2:
				bitmap = Image(f, width , height );
				break;
	
			default:
				bitmap = Image(f, width , height );  // bunu sil :D
				break;
		}

		return bitmap;
	}
	
	
	private Bitmap Image(File f, int width , int height ) {
		// TODO Auto-generated method stub
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
			int scaledHeight=height*bitmap.getWidth()/bitmap.getHeight();
			
			if(height/3<bitmap.getHeight()/2)
				return cutBimap(getResizedBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), width, bitmap.getHeight()), width, ((height*2)/5), false);
			else
				return cutBimap(getResizedBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), width, scaledHeight), width, ((height*2)/5), false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}


	private Bitmap Animation(File f, int width , int height) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null; 
		try {
			bitmap = getResizedBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), width, ((height*2)/5));
			
			// ((height*2)/5)
			if(height/3<bitmap.getHeight()/2)
				return getResizedBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), width, bitmap.getHeight());
			else
				return getResizedBitmap(BitmapFactory.decodeStream(new FileInputStream(f)), width, height*bitmap.getWidth()/bitmap.getHeight());
		} catch (FileNotFoundException e) {
			System.out.println("error+"+e);
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		// TODO Auto-generated method stub
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		// RECREATE THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}
	
	
	
	
	
//	private Bitmap Video(File f) {
//		// TODO Auto-generated method stub
//		try {
//			BitmapFactory.Options o = new BitmapFactory.Options();
//			o.inJustDecodeBounds = true;
//			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
//			final int REQUIRED_SIZE = 90; //80
//			int width_tmp = o.outWidth, height_tmp = o.outHeight;
//			int scale = 1;
//			while (true) {
//				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
//					break;
//				width_tmp /= 2;
//				height_tmp /= 2;
//				scale *= 2;
//			}
//			BitmapFactory.Options o2 = new BitmapFactory.Options();
//			o2.inSampleSize = scale;		 
//			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
//		} catch (FileNotFoundException e) {
//		}
//		return null;
//	}


	public void setContext(Context context) {
		this.context = context;
	}
	
	
	private Bitmap cutBimap(Bitmap cutBitmap, int width, int height, boolean b) {
		// TODO Auto-generated method stub
		 Bitmap bmOverlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//ARGB_8888
		 Canvas c = new Canvas(bmOverlay); 
		 c.drawBitmap(cutBitmap, 0, 0, null);
		 return bmOverlay;
	}

	
	
	

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	private class PhotosLoader implements Runnable {
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

	private boolean imageViewReused(PhotoToLoad photoToLoad) {
		// TODO Auto-generated method stub
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	private class BitmapDisplayer implements Runnable {
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