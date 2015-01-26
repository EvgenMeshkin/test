package by.evgen.android.apiclient;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import by.evgen.android.apiclient.source.CachedHttpDataSource;
import by.evgen.android.apiclient.source.HttpDataSource;
import by.evgen.android.apiclient.source.VkDataSource;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by evgen on 18.10.2014.
 */

public class CoreApplication extends Application {

    private final Map<String, Object> mGetService = new HashMap<String, Object>();

    @Override
    public void onCreate() {
        super.onCreate();
        mGetService.put(HttpDataSource.KEY, new HttpDataSource());
        mGetService.put(VkDataSource.KEY, new VkDataSource());
        mGetService.put(CachedHttpDataSource.KEY, new CachedHttpDataSource(this));
        mGetService.put(ImageLoader.KEY, new ImageLoader(this));
    }

    @Override
    public Object getSystemService(String name) {
        if (mGetService.containsKey(name)) {
            return mGetService.get(name);
        }
        return super.getSystemService(name);
    }

    public static <T> T get(Context context, String key) {
        if (context == null || key == null) {
            throw new IllegalArgumentException("Context and key must not be null");
        }
        T systemService = (T) context.getSystemService(key);
        if (systemService == null) {
            context = context.getApplicationContext();
            systemService = (T) context.getSystemService(key);
        }
        if (systemService == null) {
            throw new IllegalStateException(key + " not available");
        }
        return systemService;
    }

}
