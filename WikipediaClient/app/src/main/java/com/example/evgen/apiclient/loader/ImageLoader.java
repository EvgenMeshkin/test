package com.example.evgen.apiclient.loader;

/**
 * Created by User on 26.11.2014.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.auth.VkOAuthHelper;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.processing.Processor;
import com.example.evgen.apiclient.source.HttpDataSource;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private MemoryCache memoryCache=new MemoryCache();
    private FileCache fileCache;
    Bitmap bitmap;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new ConcurrentHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler=new Handler();//handler to display images in UI thread
    List<BitmapDisplayer> mArray = new CopyOnWriteArrayList<BitmapDisplayer>();
    Integer mPos;
    private final Object mPauseWorkLock = new Object();
    protected boolean mPauseWork = false;

    public ImageLoader(Context context){
//        fileCache=new FileCache(context);
       executorService=Executors.newFixedThreadPool(5);
    }

    public static interface Callback<Result,Result2> {
        void onResult(Bitmap bitmap, Integer url);
     }
    Callback callback;

    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }


    public void DisplayImage(final Callback callback,final String url,final ImageView imageView,final HttpDataSource dataSource,final Processor processor, final Integer position){
        imageViews.put(imageView, url);
        this.callback = callback;
        Bitmap bitmap=memoryCache.get(url);
        mPos = position;
        if(bitmap!=null) {
          //  imageView.setImageBitmap(bitmap);
            Log.i(TAG, "FromTheCache");
            callback.onResult(bitmap, mPos);
        }   else {
            Log.i(TAG, "Not FromTheCache");
            queuePhoto(url, imageView, dataSource, processor);
        }
    }

    private void queuePhoto(String url, ImageView imageView, HttpDataSource dataSource, Processor processor){
        PhotoToLoad p=new PhotoToLoad(url, imageView, dataSource, processor);
        executorService.submit(new PhotosLoader(p));
    }

    //Task for the queue
    private class PhotoToLoad {
        public final String url;
        public final ImageView imageView;
        public final HttpDataSource dataSource;
        public final Processor processor;
        public PhotoToLoad(String u, ImageView i, HttpDataSource dataSource, Processor processor){
            url=u;
            imageView=i;
            this.dataSource = dataSource;
            this.processor = processor;
        }
    }

    private class PhotosLoader implements Runnable {
        private PhotoToLoad photoToLoad;
        private PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {

            try{
                if(imageViewReused(photoToLoad))
                    return;
                InputStream dataSource = photoToLoad.dataSource.getResult(photoToLoad.url);
                Object processingResult = photoToLoad.processor.process(dataSource);
                Bitmap bmp= (Bitmap) processingResult;
                if(imageViewReused(photoToLoad))
                    return;

                synchronized (mPauseWorkLock) {
                    while (mPauseWork) {
                        try {
                            mPauseWorkLock.wait();
                        } catch (InterruptedException e) {}
                    }
                }

                memoryCache.put(photoToLoad.url, bmp);
                //if (imageViewReused(photoToLoad))
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
               // List<BitmapDisplayer> mArray = new CopyOnWriteArrayList<BitmapDisplayer>();
                if (!mArray.contains(bd)) {
                    mArray.add(bd);
                    handler.post(bd);
                }
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable{
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run(){
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                //photoToLoad.imageView.setImageBitmap(bitmap);
            callback.onResult(bitmap, mPos);
            imageViews.remove(photoToLoad.imageView);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }


//    private static class AsyncDrawable extends BitmapDrawable {
//        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
//
//        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
//            super(res, bitmap);
//            bitmapWorkerTaskReference =
//                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
//        }
//
//        public BitmapWorkerTask getBitmapWorkerTask() {
//            return bitmapWorkerTaskReference.get();
//        }
//    }





}