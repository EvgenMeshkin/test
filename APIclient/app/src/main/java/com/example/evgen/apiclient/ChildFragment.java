package com.example.evgen.apiclient;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evgen.apiclient.bo.NoteGsonModel;

/**
 * Created by evgen on 08.11.2014.
 */
public class ChildFragment extends Fragment {


    public ChildFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        final View content = inflater.inflate(R.layout.child_fragment, container, false);
//        if(getArguments() != null) {
//            NoteGsonModel obj = (NoteGsonModel) getArguments().getParcelable("key");
//            ((TextView)content.findViewById(android.R.id.text1)).setText(obj.getTitle());
//            ((TextView)content.findViewById(android.R.id.text2)).setText(obj.getContent());
//        }


        return content;
    }

}