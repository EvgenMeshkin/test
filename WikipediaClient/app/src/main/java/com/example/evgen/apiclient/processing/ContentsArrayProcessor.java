package com.example.evgen.apiclient.processing;

import android.util.Log;

import com.example.evgen.apiclient.bo.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evgen on 31.12.2014.
 */
public class ContentsArrayProcessor implements Processor<List<String>,InputStream>{
    final static String LOG_TAG = "ContentsArrayProcessor";
    @Override
    public List<String> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject jsonObjectquery = jsonObject.getJSONObject("mobileview");
//        String strid = jsonObjectquery.getString("pages").substring(jsonObjectquery.getString("pages").indexOf("{")+2,jsonObjectquery.getString("pages").indexOf(":")-1);
//        JSONObject jsonObjectquerypages = jsonObjectquery.getJSONObject("pages");
//        JSONObject jsonObjectquerypages1 = jsonObjectquerypages.getJSONObject(strid);
        JSONArray array = (JSONArray)jsonObjectquery.get("sections");
        List<String> noteArray = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject2 = array.getJSONObject(i);
            Category category = new Category(jsonObject2);

            Log.d(LOG_TAG,  category.getLINE());
            category.getLINE();
            noteArray.add(category.getLINE());
           // noteArray.add(new Category(jsonObject2));
        }
       return noteArray;
    }

}