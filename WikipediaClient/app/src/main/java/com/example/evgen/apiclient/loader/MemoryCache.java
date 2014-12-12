package com.example.evgen.apiclient.loader;

/**
 * Created by User on 26.11.2014.
 */
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class MemoryCache {
    private static final String TAG = "MemoryCache";
    private static final int MAX_SIZE = 16 * 1024 * 1024;
    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(MAX_SIZE) {

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }
    };

    public Bitmap get(String id){
            return cache.get(id);
    }

    public void put(String id, Bitmap bitmap){
            cache.put(id, bitmap);
    }


}
