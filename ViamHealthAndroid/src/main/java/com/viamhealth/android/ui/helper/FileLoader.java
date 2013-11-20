package com.viamhealth.android.ui.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.viamhealth.android.ui.cache.FileCache;
import com.viamhealth.android.ui.cache.MemoryCache;
import com.viamhealth.android.utils.UIUtility;

import org.apache.commons.io.FileUtils;

/**
 * Created by naren on 27/10/13.
 */
public class FileLoader {
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    ExecutorService executorService;
    final Context mContext;
    final String authToken;

    public FileLoader(Context context, String token){
        mContext = context;
        fileCache=new FileCache(context);
        authToken = token;
        executorService=Executors.newFixedThreadPool(5);
    }

    public interface OnFileLoadedListener {
        public void OnFileLoaded(File file);
    }

    public void LoadFile(String url, String filename, OnFileLoadedListener listener) {
        List<Byte> byteArray = memoryCache.get(url);
        File f = fileCache.getFile(url, UIUtility.getFileExtension(filename));
        if(f.exists() && f.length()>0){
            listener.OnFileLoaded(f);
        } else {
            queueFile(url, UIUtility.getFileExtension(filename), listener);
            //imageView.setImageResource(loader);
        }
    }

    private void queueFile(String url, String extension, OnFileLoadedListener listener) {
        FileToLoad p=new FileToLoad(url, extension, listener);
        executorService.submit(new FilesLoader(p));
    }

    private byte[] getByteArray(String url, String extension) {
        File f=fileCache.getFile(url, extension);

        //from SD cache
        byte[] b = decodeFile(f);
        if(b!=null)
            return b;

        //from web
        try {
            byte[] byteArray=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setRequestMethod("GET");
            if(authToken!=null)
                conn.setRequestProperty("Authorization", "Token " + authToken);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            UIUtility.CopyStream(is, os);
            os.close();
            byteArray = decodeFile(f);
            return byteArray;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private byte[] decodeFile(File f){
        try {
            return FileUtils.readFileToByteArray(f);
        } catch (IOException e) {
            Log.e("TAG", "File to Byte Array conversion failed", e.getCause());
        }
        return null;
    }

    //Task for the queue
    private class FileToLoad {
        public String url;
        public OnFileLoadedListener listener;
        public String extension;
        public FileToLoad(String u, String e, OnFileLoadedListener l){
            url=u;
            listener = l;
            extension = e;
        }
    }

    class FilesLoader implements Runnable {
        FileToLoad fileToLoad;
        FilesLoader(FileToLoad fileToLoad){
            this.fileToLoad=fileToLoad;
        }

        @Override
        public void run() {
            //if(imageViewReused(photoToLoad))
            //    return;
            byte[] ba = getByteArray(fileToLoad.url, fileToLoad.extension);
            List<Byte> byteArr = new ArrayList<Byte>(ba.length);
            for(int i=0; i<ba.length; i++) byteArr.add(ba[i]);
            memoryCache.put(fileToLoad.url, byteArr);
            FileDisplayer bd=new FileDisplayer(ba, fileToLoad);
            Activity a=(Activity)mContext;
            a.runOnUiThread(bd);
        }
    }

    /*boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }*/

    //Used to display bitmap in the UI thread
    class FileDisplayer implements Runnable {
        byte[] byteArray;
        FileToLoad fileToLoad;
        public FileDisplayer(byte[] b, FileToLoad f){byteArray=b;fileToLoad=f;}
        public void run()
        {
            fileToLoad.listener.OnFileLoaded(fileCache.getFile(fileToLoad.url, fileToLoad.extension));
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
