package com.example.evgen.apiclient.processing;

import com.example.evgen.apiclient.bo.Note;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 22.10.2014.
 */
public class NoteArrayProcessor extends WrapperArrayProcessor<Note> {

    @Override
    protected Note createObject(JSONObject jsonObject) {
        return new Note(jsonObject);
    }

}