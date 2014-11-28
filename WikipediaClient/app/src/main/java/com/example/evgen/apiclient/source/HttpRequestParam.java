package com.example.evgen.apiclient.source;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21.10.2014.
 */
public class HttpRequestParam {

    private static final ArrayList<String> DATA;

    static {
        DATA = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            DATA.add("test value " + i);
        }
    }

    public static List<String> getData() throws Exception {
        return (List<String>) DATA;
    }

    public static List<String> setData(String s) throws Exception {
        DATA.add(s);
        return (List<String>) DATA;
    }

}
