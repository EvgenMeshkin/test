package com.example.evgenmeshkin.task_manager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 09.10.2014.
 */
public class GoogleActivity extends ActionBarActivity {
    private ArrayList<HashMap<String, Object>> catList;
    private ArrayList<HashMap<String, Object>> catList1;
    private static final String TITLE = "catname"; // Верхний текст
    private static final String DESCRIPTION = "description"; // ниже главного
    private static final String ICON = "icon";  // будущая картинка
    String[] data = {"one", "two", "three", "four", "five"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

    //    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
    //    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      Spinner spinner = (Spinner) findViewById(R.id.spinner);
    //    spinner.setAdapter(adapter2);

        ListView listView = (ListView) findViewById(R.id.listView1);

        // создаем массив списков
        catList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hm;

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Текст новостей"); // Название
        hm.put(DESCRIPTION, "тект"); // Описание
        hm.put(ICON,  R.drawable.abc_ic_go); // Картинка
        catList.add(hm);

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Текст новостей"); // Название
        hm.put(DESCRIPTION, "тект"); // Описание
        hm.put(ICON,  R.drawable.abc_ic_go);
        catList.add(hm);

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Текст новостей"); // Название
        hm.put(DESCRIPTION, "тект"); // Описание
        hm.put(ICON,  R.drawable.abc_ic_go);
        catList.add(hm);

        hm = new HashMap<String, Object>();
        hm.put(TITLE, "Текст новостей"); // Название
        hm.put(DESCRIPTION, "тект"); // Описание
        hm.put(ICON,  R.drawable.abc_ic_go);
        catList.add(hm);

        SimpleAdapter adapter = new SimpleAdapter(this, catList,
                R.layout.fragmentg, new String[] { TITLE, DESCRIPTION, ICON},
                new int[] { R.id.text1, R.id.text2, R.id.img });

        listView.setAdapter(adapter);

        catList1 = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> hm1;

        hm1 = new HashMap<String, Object>();
     //   hm1.put(TITLE, " "); // Название
        hm1.put(DESCRIPTION, "Все"); // Описание
       // hm1.put(ICON,  R.drawable.abc_ic_go); // Картинка
        catList1.add(hm1);

        hm1 = new HashMap<String, Object>();
        hm1.put(TITLE, R.drawable.abc_ic_search_api_holo_light); // Название
        hm1.put(DESCRIPTION, "Поиск"); // Описание
        hm1.put(ICON,  R.drawable.abc_ic_cab_done_holo_light);
        catList1.add(hm1);

        hm1 = new HashMap<String, Object>();
        hm1.put(TITLE,R.drawable.abc_ic_search_api_holo_light); // Название
        hm1.put(DESCRIPTION, "Новости"); // Описание
        hm1.put(ICON,  R.drawable.abc_ic_cab_done_holo_light);
        catList1.add(hm1);

        hm1 = new HashMap<String, Object>();
        hm1.put(TITLE, R.drawable.abc_ic_search_api_holo_light); // Название
        hm1.put(DESCRIPTION, "Мои круги"); // Описание
        hm1.put(ICON,  R.drawable.abc_ic_cab_done_holo_light);
        catList1.add(hm1);
        SimpleAdapter adapter1 = new SimpleAdapter(this, catList1,
                R.layout.itemspinner, new String[] { TITLE, DESCRIPTION, ICON},
                new int[] { R.id.img1, R.id.text2, R.id.img });
        spinner.setAdapter(adapter1);
    }
}
