package com.example.evgen.apiclient.processing;

import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.apiclient.bo.Friend;
import com.example.evgen.apiclient.bo.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evgen on 15.11.2014.
 */
public class CategoryArrayProcessor extends WrapperArrayProcessor<Category> {

    @Override
    protected Category createObject(JSONObject jsonObject) {
        return new Category(jsonObject);
    }
}
