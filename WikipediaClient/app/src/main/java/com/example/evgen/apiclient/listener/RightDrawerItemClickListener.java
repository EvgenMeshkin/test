package com.example.evgen.apiclient.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.evgen.apiclient.fragments.DetailsFragment;

/**
 * Created by evgen on 31.12.2014.
 */
public class RightDrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick(
            AdapterView<?> parent, View view, int position,  long id
    ) {
        DetailsFragment.setListener(position);
    }
}
