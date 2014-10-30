package com.example.evgen.apiclient;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 30.10.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentWiki extends Fragment {

    private static final String TITLE = "catname"; // Верхний текст
    private static final String DESCRIPTION = "description"; // ниже главного
    private static final String ICON = "icon";  // будущая картинка
    String[] data = {"Друзья", "Подписчики", "Группы", "Фото", "Видео", "Аудио"};

    public FragmentWiki(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //    View rootView = inflater.inflate(R.layout.activity_listview, container, false);


        final View content = inflater.inflate(R.layout.fragment_wiki,null);



        return content;
    }
}