package com.example.evgenmeshkin.task_manager.source;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 16.10.2014.
 */
public class DataSource {
    private static final List<String> DATA;

    static {
        DATA = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            DATA.add("test value " + i);
        }
    }

    public static List<String> getData() throws Exception {
        Thread.currentThread().sleep(DateUtils.SECOND_IN_MILLIS * 2);
        return DATA;
    }
    public static List<String> setData(String S) throws Exception {
        Thread.currentThread().sleep(DateUtils.SECOND_IN_MILLIS * 2);
        DATA.add(S);
        return DATA;
    }

}

