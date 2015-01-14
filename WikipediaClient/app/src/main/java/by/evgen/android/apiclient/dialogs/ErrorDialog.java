package by.evgen.android.apiclient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

/**
 * Created by User on 14.11.2014.
 */
public class ErrorDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static ErrorDialog newInstance(String title) {
        ErrorDialog frag = new ErrorDialog();
        Bundle args = new Bundle();
        args.putString("Error:", title);
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
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}