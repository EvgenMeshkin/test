package com.example.evgen.apiclient.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.bo.NoteGsonModel;

/**
 * Created by User on 22.10.2014.
 */
public class DetailsFragment extends Fragment {
    public DetailsFragment(){}

    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        final View content = inflater.inflate(R.layout.fragment_details, container, false);
        if(getArguments() != null) {
            NoteGsonModel obj = (NoteGsonModel) getArguments().getParcelable("key");
            ((TextView)content.findViewById(android.R.id.text1)).setText(obj.getTitle());
            ((TextView)content.findViewById(android.R.id.text2)).setText(obj.getContent());
        }


        return content;
    }

}