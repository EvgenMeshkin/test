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

        JSONArray array = new JSONArray(string.substring(string.indexOf("["), (string.indexOf("]")+1)));
        List<T> noteArray = new ArrayList<T>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            noteArray.add(createObject(jsonObject));
        }
        return noteArray;
    }

    protected abstract T createObject(JSONObject jsonObject);
}


