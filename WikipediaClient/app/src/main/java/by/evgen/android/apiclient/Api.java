package by.evgen.android.apiclient;

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
    public static final String SEARCH_GET = BASE_PATH + "action=query&list=search&format=json&";//"https://en.wikipedia.org/w/api.php?action=query&generator=search&gsrlimit=50&prop=info&gsrsearch=meaning";
    public static final String CONTENTS_GET = BASE_PATH + "action=mobileview&format=json&page=";
    public static final String RANDOM_GET = BASE_PATH + "action=query&format=json&list=wikigrokrandom";
    public static final String MOBILE_GET = BASE_PATH + "action=mobileview&sections=all&format=json&page=";
    public static final String MAIN_URL = "https://http://en.wikipedia.org/wiki/";

    public static final String BASE_PATH_VK = "https://api.vk.com/method/";
    public static final String VKNOTES_GET = BASE_PATH_VK + "notes.add?privacy=3&comment_privacy=3&v=5.26&title=";
    public static final String VKFOTOS_GET = BASE_PATH_VK + "users.get?fields=photo_200_orig,city,verified&name_case=Nom&version=5.27&access_token=";
    public static final String VKNOTES_ALL_GET = BASE_PATH_VK + "notes.get?fields=notes$count=100&sort=0&v=5.26&access_token=";
    public static final String VKLIKEIS_GET = BASE_PATH_VK + "likes.isLiked?type=note&v=5.26&item_id=";
    public static final String VKLIKE_GET = BASE_PATH_VK + "likes.add?type=note&v=5.26&item_id=";

}
