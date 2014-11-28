package com.example.evgen.apiclient.processing;

import com.example.evgen.apiclient.bo.User;

import org.json.JSONObject;

/**
 * Created by User on 22.10.2014.
 */
public class UserArrayProcessor extends WrapperArrayProcessor<User> {

    @Override
    protected User createObject(JSONObject jsonObject) {
        return new User(jsonObject);
    }

}
