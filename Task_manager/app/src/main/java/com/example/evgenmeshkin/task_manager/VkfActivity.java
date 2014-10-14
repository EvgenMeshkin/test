package com.example.evgenmeshkin.task_manager;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by evgen on 13.10.2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VkfActivity extends Fragment {
    private ArrayList<HashMap<String, Object>> catList;
    private static final String TITLE = "catname"; // Верхний текст
    private static final String DESCRIPTION = "description"; // ниже главного
    private static final String ICON = "icon";  // будущая картинка
    String[] data = {"a", "b", "c", "d", "e", "f"};

    GridView gvMain;
    ArrayAdapter<String> adaptergrid;


    String[] colors = { "Красны", "Оранжевый", "Желтый", "Зелёный", "Голубой", "Синий", "Фиолетовый"};
    public VkfActivity(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
              //    View rootView = inflater.inflate(R.layout.activity_listview, container, false);

        final View content = inflater.inflate(R.layout.activity_vkf,null);
        ListView list = (ListView) content.findViewById(R.id.listView1);
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

        adaptergrid = new ArrayAdapter<String>(getActivity(), R.layout.itemgrid, R.id.tvText, data);
        gvMain = (GridView) content.findViewById(R.id.gridView1);
        gvMain.setAdapter(adaptergrid);

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), catList,
                R.layout.fragmentg, new String[] { TITLE, DESCRIPTION, ICON},
                new int[] { R.id.text1, R.id.text2, R.id.img });


        //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),    android.R.layout.simple_list_item_1,colors);

        list.setAdapter(adapter);


        return content;
    }
}

