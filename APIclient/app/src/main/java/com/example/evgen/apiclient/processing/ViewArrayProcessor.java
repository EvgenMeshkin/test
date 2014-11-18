package com.example.evgen.apiclient.processing;

import android.util.Log;

import com.example.evgen.apiclient.bo.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 18.11.2014.
 */
public class ViewArrayProcessor implements Processor<List<Category>,InputStream>{
    final static String LOG_TAG = "ViewArrayProcessor";
    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject("query").getJSONObject("pages").getJSONObject("-1").getJSONObject("fullurl");
        //TODO wrapper for array
        List<Category> noteArray = new ArrayList<Category>(jsonObject.length());
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject jsonObject = array.getJSONObject(i);
            Category category = new Category(jsonObject);
            category.initUrl();
            noteArray.add(category);
        Log.d(LOG_TAG, "Good");
   //     }
        return noteArray;
    }

}
