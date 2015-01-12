package com.example.evgen.apiclient.processing;

import android.util.Log;

import com.example.evgen.apiclient.bo.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12.01.2015.
 */
public class LikeIsProcessor implements Processor<String,InputStream>{
    final static String LOG_TAG = "likeIsProcessor";

    @Override
    public String process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject query = jsonObject.getJSONObject("response");
        String liked = query.getString("liked");
        Log.d(LOG_TAG, "Good" + liked);
        return liked;
    }

}
