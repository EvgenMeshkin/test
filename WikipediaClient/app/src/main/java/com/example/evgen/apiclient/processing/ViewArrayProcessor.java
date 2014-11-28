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

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject jsonObjectquery = jsonObject.getJSONObject("query");
        String strid = jsonObjectquery.getString("pages").substring(jsonObjectquery.getString("pages").indexOf("{")+2,jsonObjectquery.getString("pages").indexOf(":")-1);
        JSONObject jsonObjectquerypages = jsonObjectquery.getJSONObject("pages");
        JSONObject jsonObjectquerypages1 = jsonObjectquerypages.getJSONObject(strid);
        List<Category> noteArray = new ArrayList<Category>(jsonObjectquerypages1.length());
        Category category = new Category(jsonObjectquerypages1);
        category.getURL();
        noteArray.add(category);
        return noteArray;
    }

}
