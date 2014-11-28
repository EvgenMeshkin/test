package com.example.evgen.apiclient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.evgen.apiclient.R;

/**
 * Created by User on 14.11.2014.
 */
public class ErrorDialog extends DialogFragment implements DialogInterface.OnClickListener {

    final static String LOG_TAG = "myLogs";
    static String mTitle;

    public static ErrorDialog newInstance(String title) {
        Log.d(LOG_TAG, "Dialog : " + "Newinstance");
        ErrorDialog frag = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("Error:", title);
        //mTitle = title;
        frag.setArguments(args);
        return frag;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("Error:");
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Error: ")
                .setPositiveButton("Repeat", this)
                .setNegativeButton("Cancel", this)
                .setMessage(title);
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        Log.d(LOG_TAG, "Dialog : " + "OnclickDialog");
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
    }
}