package com.example.evgen.apiclient.processing;

import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by evgen on 13.01.2015.
 */
public class NoteProcessor implements Processor<Long,InputStream>{
    final static String LOG_TAG = "NoteProcessor";

    @Override
    public Long process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
       // JSONObject query = jsonObject.getJSONObject("response");
        Long id = jsonObject.getLong("response");
        Log.d(LOG_TAG, "Good" + id);
        return id;
    }

}