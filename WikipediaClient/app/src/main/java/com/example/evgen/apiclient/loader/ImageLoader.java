package com.example.evgen.apiclient.loader;

/**
 * Created by User on 26.11.2014.
 */


import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.example.evgen.apiclient.CoreApplication;
import com.example.evgen.apiclient.processing.Processor;
import com.example.evgen.apiclient.source.HttpDataSource;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private MemoryCache memoryCache=new MemoryCache();
    private Map<Integer, String> imageViews=new ConcurrentHashMap<Integer, String>();
    private ExecutorService executorService;
    private final Object mPauseWorkLock = new Object();
    protected boolean mPauseWork = false;
   public static final String KEY = "ImageLoader";

    public ImageLoader(Context context){
    executorService=Executors.newFixedThreadPool(5);
    }

    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    public void displayImage(final String url,final ImageView imageView,final HttpDataSource dataSource,final Processor processor, final Integer position){
        imageViews.put(position, url);
        Bitmap bitmap=memoryCache.get(url);
        if((bitmap!=null) && (imageView.getTag()==url)) {
            Log.i(TAG, "FromTheCache");
            imageView.setImageBitmap(bitmap);
        }   else {
            Log.i(TAG, "Not FromTheCache");
            queueImage(url, imageView, dataSource, processor, position);
        }
    }

    private void queueImage(String url, ImageView imageView, HttpDataSource dataSource, Processor processor, Integer position){
        MemoryValue p=new MemoryValue(url, imageView, dataSource, processor, position);
        executorService.submit(new ImagesLoader(p));
    }

    private class MemoryValue {
        public final String url;
        public final ImageView imageView;
        public final HttpDataSource dataSource;
        public final Processor processor;
        public final Integer position;
        public MemoryValue(String u, ImageView i, HttpDataSource dataSource, Processor processor, Integer position){
            url=u;
            imageView=i;
            this.dataSource = dataSource;
            this.processor = processor;
            this.position = position;
        }
    }

    private class ImagesLoader implements Runnable {
       private final MemoryValue memoryValue;
       private Handler handler=new Handler();
//        private final WeakReference<ImageView> imageViewReference =  new WeakReference<ImageView>(photoToLoad.imageView);;
        private ImagesLoader(MemoryValue memoryValue){
            this.memoryValue = memoryValue;
        }

        @Override
        public void run() {
            try{
                InputStream dataSource = memoryValue.dataSource.getResult(memoryValue.url);
                Object processingResult = memoryValue.processor.process(dataSource);
                Bitmap bmp= (Bitmap) processingResult;
//                synchronized (mPauseWorkLock) {
//                    while (mPauseWork) {
//                        try {
//                            mPauseWorkLock.wait();
//                        } catch (InterruptedException e) {}
//                    }
//                }
                if(imageViewReused(memoryValue))
                return;
                Log.i(TAG, "Proverka false");
                memoryCache.put(memoryValue.url, bmp);
                BitmapDisplayer bd = new BitmapDisplayer(bmp, memoryValue);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }

    synchronized boolean imageViewReused(MemoryValue memoryValue){
        String tag=imageViews.get(memoryValue.position);
        Log.i(TAG, "Proverka " + tag);
        if(tag==null || !tag.equals(memoryValue.url))
            return true;
        return false;
    }

    class BitmapDisplayer implements Runnable{
        Bitmap bitmap;
        MemoryValue memoryValue;
        public BitmapDisplayer(Bitmap b, MemoryValue p){bitmap=b;
            memoryValue =p;}
        public void run(){
            if(imageViewReused(memoryValue))
                return;
            if(bitmap!=null)
            memoryValue.imageView.setImageBitmap(bitmap);
        }
    }

}