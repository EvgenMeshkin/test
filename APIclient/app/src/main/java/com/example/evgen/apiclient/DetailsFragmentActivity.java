package com.example.evgen.apiclient;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.evgen.apiclient.bo.NoteGsonModel;
import com.example.evgen.apiclient.fragments.DetailsFragment;

/**
 * Created by User on 13.11.2014.
 */
public class DetailsFragmentActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line with the list so we don't need this activity.
            finish();
            return;
        }

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("key", noteGsonModel);
            DetailsFragment details = new DetailsFragment();
            details.setArguments(getIntent().<Bundle>getParcelableExtra("key"));
            getSupportFragmentManager().beginTransaction().add(
                    android.R.id.content, details).commit();
        }
    }


}
