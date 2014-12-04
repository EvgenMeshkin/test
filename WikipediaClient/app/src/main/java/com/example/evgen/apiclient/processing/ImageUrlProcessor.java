package com.example.evgen.apiclient.processing;

import android.util.Log;

import com.example.evgen.apiclient.bo.Category;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by User on 19.11.2014.
 */
public class ImageUrlProcessor implements Processor<List<Category>,InputStream>{

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject jsonObjectquery = jsonObject.getJSONObject("query");
         JSONObject jsonObjectquerypages = jsonObjectquery.getJSONObject("pages");
        Iterator<?> i = jsonObjectquerypages.keys();
        JSONObject jsonObjectquerypages1 = jsonObjectquerypages.getJSONObject(i.next().toString());
        JSONObject jsonObjectquerypages1fullurl = jsonObjectquerypages1.getJSONObject("thumbnail");
        List<Category> noteArray = new ArrayList<Category>(jsonObjectquerypages1fullurl.length());
        Category category = new Category(jsonObjectquerypages1fullurl);
        category.getURLIMAGE();
        noteArray.add(category);
        return noteArray;
    }

}
