package com.example.evgen.apiclient.processing;

import android.util.Log;

import com.example.evgen.apiclient.bo.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 05.01.2015.
 */
public class RandomProcessor implements Processor<List<Category>,InputStream>{
    final static String LOG_TAG = RandomProcessor.class.getSimpleName();
    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        Log.d(LOG_TAG, "0");
        String string = new StringProcessor().process(inputStream);
        Log.d(LOG_TAG, "01");
        JSONObject jsonObject = new JSONObject(string);
        Log.d(LOG_TAG, "1");
        JSONObject query = jsonObject.getJSONObject("query");
        Log.d(LOG_TAG, "2");
//        JSONObject wikigrokrandom = query.getJSONObject("wikigrokrandom");
//        List<Category> noteArray = new ArrayList<Category>(wikigrokrandom.length());
//        Category category = new Category(wikigrokrandom);
//        category.getTITLE();
//        Log.d(LOG_TAG, "Item " + category.getTITLE());
//        noteArray.add(category);
//        return noteArray;
        JSONArray array = (JSONArray)query.get("wikigrokrandom");
        Log.d(LOG_TAG, "3");
        List<Category> noteArray = new ArrayList<Category>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject2 = array.getJSONObject(i);
            Log.d(LOG_TAG, "4");
            noteArray.add(new Category(jsonObject2));
        }
        return noteArray;
    }

}
