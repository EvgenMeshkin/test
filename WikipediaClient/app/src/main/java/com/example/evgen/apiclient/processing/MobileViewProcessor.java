package com.example.evgen.apiclient.processing;

import android.util.Log;

import com.example.evgen.apiclient.bo.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 08.01.2015.
 */
public class MobileViewProcessor implements Processor<List<Category>,InputStream>{
    final static String LOG_TAG = MobileViewProcessor.class.getSimpleName();

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject query = jsonObject.getJSONObject("mobileview");
        JSONArray array = (JSONArray)query.get("sections");
        List<Category> noteArray = new ArrayList<Category>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject2 = array.getJSONObject(i);
            Log.d(LOG_TAG, array.getJSONObject(i).toString());
            noteArray.add(new Category(jsonObject2));
        }
        return noteArray;
    }

}