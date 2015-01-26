package by.evgen.android.imageloader;

/**
 * Created by User on 26.11.2014.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import by.evgen.android.apiclient.CoreApplication;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.os.assist.LIFOLinkedBlockingDeque;
import by.evgen.android.apiclient.processing.BitmapProcessor;
import by.evgen.android.apiclient.processing.ImageUrlProcessor;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.CachedHttpDataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;

public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, String> imageViews = new ConcurrentHashMap<ImageView, String>();
    private ExecutorService executorService;
    public static final String KEY = "ImageLoader";
    private Context mContext;
    private AtomicBoolean isPause = new AtomicBoolean(false);
    final int stub_id = by.evgen.android.apiclient.R.drawable.stub;
    private Set<ImageView> mImagesViews = new HashSet<ImageView>();
    private final Object mLock = new Object();

    public ImageLoader(Context context) {
        mContext = context;
        executorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
                new LIFOLinkedBlockingDeque<Runnable>());
    }

    public static ImageLoader get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    public void displayImage(final String url, final ImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            Log.i(TAG, "FromTheCache");
            imageView.setImageBitmap(bitmap);
        } else {
            Log.i(TAG, "Not FromTheCache");
            queueImage(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    public void pause() {
        isPause.set(true);
    }

    public void resume() {
        isPause.set(false);
        synchronized (mLock) {
            for (ImageView imageView : mImagesViews) {
                Object tag = imageView.getTag();
                if (tag != null) {
                    displayImage((String) tag, imageView);
                }
            }
            mImagesViews.clear();
        }
    }

    private void queueImage(String url, ImageView imageView) {
        MemoryValue p = new MemoryValue(url, imageView, mContext);
        executorService.submit(new ImagesLoader(p));
    }

    private class MemoryValue {
        public final String url;
        public final ImageView imageView;
        public final HttpDataSource dataSource;
        public final Processor processor;
        public final HttpDataSource dataUrl;
        public final Processor processUrl;

        public MemoryValue(String url, ImageView imageView, Context context) {
            this.url = url;
            this.imageView = imageView;
            this.dataSource = new CachedHttpDataSource(context);
            this.processor = new BitmapProcessor();
            dataUrl = new VkDataSource();
            processUrl = new ImageUrlProcessor();
        }
    }

    private class ImagesLoader implements Runnable {
        private final MemoryValue memoryValue;
        private Handler handler = new Handler();

        private ImagesLoader(MemoryValue memoryValue) {
            this.memoryValue = memoryValue;
        }

        @Override
        public void run() {
            try {
                InputStream dataUrl = memoryValue.dataUrl.getResult(memoryValue.url);
                Object procesUrl = memoryValue.processUrl.process(dataUrl);
                List<Category> data = (List<Category>) procesUrl;
                String str = data.get(0).getUrlImage();
                str = str.substring(str.indexOf("px") - 2, str.indexOf("px") + 2);
                String url = data.get(0).getUrlImage().replaceAll(str, "100px");
                InputStream dataSource = memoryValue.dataSource.getResult(url);
                Object processingResult = memoryValue.processor.process(dataSource);
                Bitmap bmp = (Bitmap) processingResult;
                bmp = CircleMaskedBitmap.getCircleMaskedBitmapUsingShader(bmp, 100);
                if (bmp != null) {
                    memoryCache.put(memoryValue.url, bmp);
                }
                if (imageViewReused(memoryValue)) {
                    return;
                }
                BitmapDisplayer bd = new BitmapDisplayer(bmp, memoryValue);
                handler.post(bd);
            } catch (Throwable th) {
                return;
            }
        }
    }

    synchronized boolean imageViewReused(MemoryValue memoryValue) {
        String tag = imageViews.get(memoryValue.imageView);
        Log.i(TAG, "Check " + tag);
        if (tag.equals(null) || !tag.equals(memoryValue.url))
            return true;
        return false;
    }

    class BitmapDisplayer implements Runnable {
        private Bitmap bitmap;
        private MemoryValue memoryValue;

        public BitmapDisplayer(Bitmap bitmap, MemoryValue memoryValue) {
            this.bitmap = bitmap;
            this.memoryValue = memoryValue;
        }

        public void run() {

            if (imageViewReused(memoryValue)) {
                return;
            }
            memoryValue.imageView.setImageBitmap(bitmap);
        }
    }

//    public static Bitmap scaleTo(Bitmap source, int size){
//        int destWidth = source.getWidth();
//
//        int destHeight = source.getHeight();
//
//        destHeight = destHeight * size / destWidth;
//        destWidth = size;
//
//        if (destHeight < size){
//            destWidth = destWidth * size / destHeight;
//            destHeight = size;
//        }
//
//        Bitmap destBitmap = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(destBitmap);
//        canvas.drawBitmap(source, new Rect(0, 0, source.getWidth(), source.getHeight()), new Rect(0, 0, destWidth, destHeight), new Paint(Paint.ANTI_ALIAS_FLAG));
//        return destBitmap;
//    }
//
//    public static Bitmap getCircleMaskedBitmapUsingShader(Bitmap source, int radius){
//        if (source == null){
//            return null;
//        }
//
//        int diam = radius << 1;
//
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//        Bitmap scaledBitmap = (Bitmap)scaleTo(source, diam);
//        final Shader shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        paint.setShader(shader);
//
//        Bitmap targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(targetBitmap);
//
//        canvas.drawCircle(radius, radius, radius, paint);
//
//        return targetBitmap;
//    }

}