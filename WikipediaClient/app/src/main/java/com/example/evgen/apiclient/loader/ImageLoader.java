package com.example.evgen.apiclient.loader;

/**
 * Created by User on 26.11.2014.
 */


import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Handler;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.evgen.apiclient.CoreApplication;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.fragments.WikiFragment;
import com.example.evgen.apiclient.os.assist.LIFOLinkedBlockingDeque;
import com.example.evgen.apiclient.processing.BitmapProcessor;
import com.example.evgen.apiclient.processing.ImageUrlProcessor;
import com.example.evgen.apiclient.processing.Processor;
import com.example.evgen.apiclient.source.CachedHttpDataSource;
import com.example.evgen.apiclient.source.HttpDataSource;
import com.example.evgen.apiclient.source.VkDataSource;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, String> imageViews = new ConcurrentHashMap<ImageView, String>();
    private ExecutorService executorService;
    private final Object mPauseWorkLock = new Object();
    protected boolean mPauseWork = false;
    public static final String KEY = "ImageLoader";
    private Handler handler = new Handler();
    private Context mContext;
    private AtomicBoolean isPause = new AtomicBoolean(false);
    public ImageLoader(Context context){
    mContext = context;
    executorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
            new LIFOLinkedBlockingDeque<Runnable>());//Executors.newFixedThreadPool(5);
    }

    public static ImageLoader get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    public void setPauseWork(boolean pauseWork) {
        synchronized (mPauseWorkLock) {
            mPauseWork = pauseWork;
            if (!mPauseWork) {
                mPauseWorkLock.notifyAll();
            }
        }
    }

    public void displayImage(final String url,final ImageView imageView){
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if (bitmap != null) {
            Log.i(TAG, "FromTheCache");
            imageView.setImageBitmap(bitmap);
        }   else {
            Log.i(TAG, "Not FromTheCache");
            queueImage(url, imageView);
        }
    }

    public void pause() {
        isPause.set(true);
    }

    private final Object mDelayedLock = new Object();

    public void resume() {
        isPause.set(false);
        synchronized (mDelayedLock) {
            for (ImageView imageView : delayedImagesViews) {
                Object tag = imageView.getTag();
                if (tag != null) {
                    displayImage((String) tag, imageView);
                }
            }
            delayedImagesViews.clear();
        }
    }

    private Set<ImageView> delayedImagesViews = new HashSet<ImageView>();

    private void queueImage(String url, ImageView imageView){
        MemoryValue p=new MemoryValue(url, imageView, mContext);
        executorService.submit(new ImagesLoader(p));
    }

    private class MemoryValue {
        public final String url;
        public final ImageView imageView;
        public final HttpDataSource dataSource;
        public final Processor processor;
        public final HttpDataSource dataUrl;
        public final Processor processUrl;
        public MemoryValue(String u, ImageView i, Context context){
            url=u;
            imageView=i;
            this.dataSource = new CachedHttpDataSource(context);
            this.processor = new BitmapProcessor();
            dataUrl =  new VkDataSource();
            processUrl = new ImageUrlProcessor();
            }
    }

    private class ImagesLoader implements Runnable {
       private final MemoryValue memoryValue;
       //private Handler handler=new Handler();
//        private final WeakReference<ImageView> imageViewReference =  new WeakReference<ImageView>(photoToLoad.imageView);;
        private ImagesLoader(MemoryValue memoryValue){
            this.memoryValue = memoryValue;
        }

        @Override
        public void run() {
            try{
                InputStream dataUrl = memoryValue.dataUrl.getResult(memoryValue.url);
                Object procesUrl = memoryValue.processUrl.process(dataUrl);
                List<Category> data = (List<Category>)procesUrl;
                String str = data.get(0).getURLIMAGE();
                str = str.substring(str.indexOf("px")-2, str.indexOf("px")+2);
                String url = data.get(0).getURLIMAGE().replaceAll(str,"100px");
                InputStream dataSource = memoryValue.dataSource.getResult(url);
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
        String tag=imageViews.get(memoryValue.imageView);
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
          //  if(bitmap!=null)
            memoryValue.imageView.setImageBitmap(bitmap);
        }
    }

}