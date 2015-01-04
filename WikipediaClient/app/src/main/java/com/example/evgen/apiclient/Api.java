package com.example.evgen.apiclient;

/**
 * Created by evgen on 15.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://en.wikipedia.org/w/api.php?";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final String CATEGORY_GET = BASE_PATH + "action=query&prop=categories&format=json&titles=Albert%20Einstein";
    public static final String GEOSEARCH_GET = BASE_PATH + "action=query&list=geosearch&format=json&gslimit=100&gsradius=10000&gscoord=";
    public static final String URLVIEW_GET = BASE_PATH + "action=query&prop=info&format=json&inprop=protection&inprop=url&titles=";
    public static final String IMAGEVIEW_GET = BASE_PATH + "action=query&prop=pageimages&piprop=thumbnail&format=json&titles=";
    public static final String VKNOTES_GET = "https://api.vk.com/method/notes.add?title=Wikipedia&privacy=3&comment_privacy=3&v=5.26&text=";
    public static final String VKFOTOS_GET = "https://api.vk.com/method/users.get?fields=photo_200_orig,city,verified&name_case=Nom&version=5.27&user_ids=";
    public static final String SEARCH_GET = "https://en.wikipedia.org/w/api.php?action=query&list=search&format=json&";//"https://en.wikipedia.org/w/api.php?action=query&generator=search&gsrlimit=50&prop=info&gsrsearch=meaning";
    public static final String CONTENTS_GET = "https://en.wikipedia.org/w/api.php?action=mobileview&format=json&page=";
}
