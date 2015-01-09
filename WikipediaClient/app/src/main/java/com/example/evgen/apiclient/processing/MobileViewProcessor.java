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

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject query = jsonObject.getJSONObject("mobileview");
        JSONArray array = (JSONArray)query.get("sections");
        List<Category> noteArray = new ArrayList<Category>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject sections = array.getJSONObject(i);
            noteArray.add(new Category(sections));
        }
        return noteArray;
    }

}