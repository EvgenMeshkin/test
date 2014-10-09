package com.example.evgenmeshkin.task_manager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 09.10.2014.
 */
public class ListviewActivity extends ActionBarActivity {
    private ArrayList<HashMap<String, Object>> catList;
    private static final String TITLE = "catname"; // Верхний текст
    private static final String DESCRIPTION = "description"; // ниже главного
    private static final String ICON = "icon";  // будущая картинка

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);



        ListView listView = (ListView) findViewById(R.id.listView1);

        // создаем массив списков
        catList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hm;

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Рыжик"); // Название
        hm.put(DESCRIPTION, "Рыжий и хитрый"); // Описание
        hm.put(ICON,  R.drawable.abc_ic_go); // Картинка
        catList.add(hm);

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Васька");
        hm.put(DESCRIPTION, "Слушает да ест");
        hm.put(ICON,  R.drawable.abc_ic_go);
        catList.add(hm);

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Мурзик");
        hm.put(DESCRIPTION, "Спит и мурлыкает");
        hm.put(ICON,  R.drawable.abc_ic_go);
        catList.add(hm);

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Барсик");
        hm.put(DESCRIPTION, "Болеет за Барселону hjhjhjh");
        hm.put(ICON,  R.drawable.abc_ic_go);
        catList.add(hm);

        SimpleAdapter adapter = new SimpleAdapter(this, catList,
                R.layout.fragment, new String[] { TITLE, DESCRIPTION, ICON},
                new int[] { R.id.text1, R.id.text2, R.id.img });

        listView.setAdapter(adapter);
    }
    }







