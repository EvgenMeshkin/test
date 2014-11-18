package com.example.evgen.apiclient;

/**
 * Created by evgen on 15.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://en.wikipedia.org/w/api.php?";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final String CATEGORY_GET = BASE_PATH + "action=query&prop=categories&format=json&titles=Albert%20Einstein";
    public static final String GEOSEARCH_GET = BASE_PATH + "action=query&list=geosearch&format=json&gsradius=10000&gscoord=53.677226|23.8489383";
    public static final String URLVIEW_GET = BASE_PATH + "action=query&prop=info&format=json&inprop=protection&inprop=url&titles=";

}
