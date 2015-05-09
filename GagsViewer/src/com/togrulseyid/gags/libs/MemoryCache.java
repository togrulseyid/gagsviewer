package com.togrulseyid.gags.libs;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
  
public class MemoryCache {
	private Map<String, SoftReference<Bitmap>> cache=Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());
//	private Map<String, SoftReference<File>> cache2 =Collections.synchronizedMap(new HashMap<String, SoftReference<File>>());
  
    public Bitmap get(String id){
        if(!cache.containsKey(id))
            return null;
        SoftReference<Bitmap> ref=cache.get(id);
        return ref.get();
    }
  
    public void put(String id, Bitmap bitmap){
        cache.put(id, new SoftReference<Bitmap>(bitmap));
    }
  
    public void clear() {
        cache.clear();
    }
    
//    public File get2(String id){
//        if(!cache2.containsKey(id))
//            return null;
//        SoftReference<File> ref=cache2.get(id);
//        return ref.get();
//    }
}