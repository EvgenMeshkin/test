package com.example.evgen.apiclient.processing;

import com.example.evgen.apiclient.bo.JSONObjectWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 22.10.2014.
 */
public abstract class WrapperArrayProcessor <T extends JSONObjectWrapper> implements Processor<List<T>,InputStream> {

    @Override
    public List<T> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
      //  char[] buf = new char[string.indexOf("[")-string.indexOf("]")];
      //  string.getChars(string.indexOf("["), (string.indexOf("]")+1), buf, 0);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject jsonObject1 = jsonObject.getJSONObject("query");
       // JSONObject jsonObject2 = jsonObject1.getJSONObject("pages");
     //   JSONObject jsonObject3 = jsonObject2.getJSONObject("query");
        JSONArray array = (JSONArray)jsonObject1.get("geosearch");//.substring(string.indexOf("["), (string.indexOf("]")+1)));
       // array.getJSONArray("geosearch");
        List<T> noteArray = new ArrayList<T>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject2 = array.getJSONObject(i);
       //     JSONObject innerJsonObject = jsonObject.getJSONObject("geosearch");
            noteArray.add(createObject(jsonObject2));
        }
        return noteArray;
    }

    protected abstract T createObject(JSONObject jsonObject);
}


