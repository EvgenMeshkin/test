package by.evgen.android.apiclient.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import by.evgen.android.apiclient.fragments.DetailsFragment;

/**
 * Created by evgen on 31.12.2014.
 */
public class RightDrawerItemClickListener implements ListView.OnItemClickListener {

    @Override
    public void onItemClick(
            AdapterView<?> parent, View view, int position, long id
    ) {
        DetailsFragment.setListener(position);
    }

}
