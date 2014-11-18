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
        Log.d(LOG_TAG, "Run");
        String string = new StringProcessor().process(inputStream);
        Log.d(LOG_TAG, "Run1");
        JSONObject jsonObject = new JSONObject(string);
        Log.d(LOG_TAG, "Run2");
        JSONObject jsonObjectquery = jsonObject.getJSONObject("query");
        String strid = jsonObjectquery.getString("pages").substring(jsonObjectquery.getString("pages").indexOf("{")+2,jsonObjectquery.getString("pages").indexOf(":")-1);
        Log.d(LOG_TAG, strid);
        JSONObject jsonObjectquerypages = jsonObjectquery.getJSONObject("pages");
        Log.d(LOG_TAG, "Run4");
         JSONObject jsonObjectquerypages1 = jsonObjectquerypages.getJSONObject(strid);
        Log.d(LOG_TAG, "Run5");
       // JSONObject jsonObjectquerypages1fullurl = jsonObjectquerypages1.getJSONObject("fullurl");
        //TODO wrapper for array
        Log.d(LOG_TAG, "Run6");
        List<Category> noteArray = new ArrayList<Category>(jsonObjectquerypages1.length());
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject jsonObject = array.getJSONObject(i);
        Log.d(LOG_TAG, "Run7");
            Category category = new Category(jsonObjectquerypages1);
            category.getURL();
        Log.d(LOG_TAG, category.toString());
            noteArray.add(category);
        Log.d(LOG_TAG, "Good");
   //     }
        return noteArray;
    }

}
