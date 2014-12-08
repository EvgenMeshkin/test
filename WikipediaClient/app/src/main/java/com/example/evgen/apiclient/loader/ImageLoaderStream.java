package com.example.evgen.apiclient.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.example.evgen.apiclient.processing.Processor;
import com.example.evgen.apiclient.source.HttpDataSource;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by User on 04.12.2014.
 */
public class ImageLoaderStream {
    private static final String TAG = "ImageLoader";
    private Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler=new Handler();//handler to display images in UI thread

    public ImageLoaderStream(Context context){
        executorService= Executors.newFixedThreadPool(5);
    }

    public void DisplayImage(String url, ImageView imageView, HttpDataSource dataSource, Processor processor){
        imageViews.put(imageView, url);
        PhotoToLoad p=new PhotoToLoad(url, imageView, dataSource, processor);
        executorService.submit(new PhotosLoader(p));
    }

     private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public HttpDataSource dataSource;
        public Processor processor;
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
                //memoryCache.put(photoToLoad.url, bmp);
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
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
                photoToLoad.imageView.setImageBitmap(bitmap);
        }
    }

}