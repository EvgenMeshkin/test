package by.evgen.android.imageloader;

/**
 * Created by User on 26.11.2014.
 */

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCache {
    private static final String TAG = "MemoryCache";
    private static final int MAX_SIZE = 16 * 1024 * 1024;
    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(MAX_SIZE) {

        @Override
        protected int sizeOf(String key, Bitmap value) {
            int size = value.getRowBytes() * value.getHeight();
            return size;
        }
    };

    public Bitmap get(String id) {
        return cache.get(id);
    }

    public void put(String id, Bitmap bitmap) {
        cache.put(id, bitmap);
    }


}
