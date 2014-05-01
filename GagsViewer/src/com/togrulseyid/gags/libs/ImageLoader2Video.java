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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageLoader2Video {
	
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoader2Video(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	int stub_id = R.drawable.untitled;

	public void DisplayImage2Video(String url, int loader, ImageView imageView) {
		stub_id = loader;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto2Video(url, imageView);
			imageView.setImageResource(loader);
		}
	}

	private void queuePhoto2Video(String url, ImageView imageView) {
		PhotoToLoad2Video p = new PhotoToLoad2Video(url, imageView);
		executorService.submit(new PhotosLoader2Video(p));
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
//			final int REQUIRED_SIZE = 70;
			final int REQUIRED_SIZE = 800;
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
	private class PhotoToLoad2Video {
		public String url;
		public ImageView imageView;

		public PhotoToLoad2Video(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader2Video implements Runnable {
		PhotoToLoad2Video PhotoToLoad2Video;

		PhotosLoader2Video(PhotoToLoad2Video PhotoToLoad2Video) {
			this.PhotoToLoad2Video = PhotoToLoad2Video;
		}

		@Override
		public void run() {
			if (imageViewReused(PhotoToLoad2Video))
				return;
			Bitmap bmp = getBitmap(PhotoToLoad2Video.url);
			memoryCache.put(PhotoToLoad2Video.url, bmp);
			if (imageViewReused(PhotoToLoad2Video))
				return;
			BitmapDisplayer2Video bd = new BitmapDisplayer2Video(bmp, PhotoToLoad2Video);
			Activity a = (Activity) PhotoToLoad2Video.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad2Video PhotoToLoad2Video) {
		String tag = imageViews.get(PhotoToLoad2Video.imageView);
		if (tag == null || !tag.equals(PhotoToLoad2Video.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer2Video implements Runnable {
		Bitmap bitmap;
		PhotoToLoad2Video PhotoToLoad2Video;

		public BitmapDisplayer2Video(Bitmap b, PhotoToLoad2Video p) {
			bitmap = b;
			PhotoToLoad2Video = p;
		}

		public void run() {
			if (imageViewReused(PhotoToLoad2Video))
				return;
			if (bitmap != null)
				PhotoToLoad2Video.imageView.setImageBitmap(bitmap);
			else
				PhotoToLoad2Video.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}