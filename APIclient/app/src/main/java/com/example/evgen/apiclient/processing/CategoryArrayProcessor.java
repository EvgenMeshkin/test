package com.example.evgen.apiclient.processing;

import com.example.evgen.apiclient.bo.Friend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evgen on 15.11.2014.
 */
public class CategoryArrayProcessor implements Processor<List<Friend>,InputStream>{

    @Override
    public List<Friend> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONObject(string).getJSONObject("response").getJSONArray("items");
        //TODO wrapper for array
        List<Friend> noteArray = new ArrayList<Friend>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            Friend friend = new Friend(jsonObject);
            friend.initName();
            noteArray.add(friend);
        }
        return noteArray;
    }

}
