package com.togrulseyid.gags.libs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
  
public class FileCache {
	public static final String SHARED_PNG_FILE_NAME = "GagsViewer.png";
	public static final String SHARED_GIF_FILE_NAME = "GagsViewer.gif";
    private File cacheDir;
  
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"GagsViewer/temp");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
  
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
  
    }
    
    /*
     * @author Togrul
     * get Files full path and name
     * */
    public String getFilePath(String url){
        String filename=String.valueOf(url.hashCode());
        return cacheDir+File.pathSeparator+filename;
    }
  
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
  
    
/* Form here is my code  to get shared file and delete it :D*/  
    
    

	
    public File getSharedFile(){
        File f = new File(cacheDir, SHARED_PNG_FILE_NAME);
        return f;
    }
    
//    public void rename(String url){
//    	File file = new File(cacheDir, String.valueOf(url.hashCode()));
//    	if(file.renameTo(new File(cacheDir, SHARED_PNG_FILE_NAME)))
//    		System.out.println("Yehhoooo: " + file.getName() );
//    	else 
//    		System.out.println("Nooooooooooo:" + file.getName() );
//    }
//    
    
    


    public void clear(String url){
    	File file = new File(cacheDir, String.valueOf(url.hashCode()));
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files) {
            if(f == file){
            	f.delete();
            	System.out.println("Deleted file: " + f.getName());
            }
        }
    }
    
    
    public File getFileDirectory(){
        return cacheDir;
    }
    
    
    
    
    
    
    /*
     * Befor i used Rename it renamed and coused to download again! But know it is just coping it
     * */
    @SuppressWarnings("resource")
	public File getSharedFile(String url,String FILE_NAME) throws IOException {
        File sourceFile =getFile(url);
        File destFile = getFile(FILE_NAME);
        
        if(destFile.exists())
        	destFile.delete();
        
        if(sourceFile == null)
        	return null;

        FileChannel inChannel = new FileInputStream(sourceFile).getChannel();
        FileChannel outChannel = new FileOutputStream(destFile).getChannel();
        	try {	    		   
        		inChannel.transferTo(0, inChannel.size(), outChannel);
        	} finally {
        		if (inChannel != null)
        			inChannel.close();
        		if (outChannel != null)
        			outChannel.close();
        	}
		return destFile;
    }
}