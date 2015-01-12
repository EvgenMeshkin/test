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
public class LikeIsProcessor implements Processor<List<Category>,InputStream>{
    final static String LOG_TAG = "likeIsProcessor";

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject query = jsonObject.getJSONObject("response");
        JSONArray array = (JSONArray)query.get("items");
        List<Category> noteArray = new ArrayList<Category>(array.length());
        Log.d(LOG_TAG, "Good");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject2 = array.getJSONObject(i);
            noteArray.add(new Category(jsonObject2));
        }
        return noteArray;
    }

}
