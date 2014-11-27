package com.example.evgen.apiclient.processing;

import com.example.evgen.apiclient.bo.Category;
import org.json.JSONObject;

/**
 * Created by evgen on 15.11.2014.
 */
public class CategoryArrayProcessor extends WrapperArrayProcessor<Category> {

    @Override
    protected Category createObject(JSONObject jsonObject) {
        return new Category(jsonObject);
    }
}
