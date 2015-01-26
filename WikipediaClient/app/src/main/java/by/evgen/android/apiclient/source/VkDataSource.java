package by.evgen.android.apiclient.source;

import android.content.Context;

import java.io.InputStream;

import by.evgen.android.apiclient.CoreApplication;

/**
 * Created by evgen on 15.11.2014.
 */
public class VkDataSource extends HttpDataSource {

    public static final String KEY = "VkDataSource";

    public static VkDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        return super.getResult(p);
    }

}