package by.evgen.android.apiclient.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by User on 14.01.2015.
 */
public class FindResponder {

    public static <T> T findFirstResponder (Fragment fragment, Class<T> clazz) {
        FragmentActivity activity = fragment.getActivity();
        if (activity == null)
            return null;
        if (clazz.isInstance(activity)) {
            return clazz.cast(activity);
        }
        Fragment parentFragment = fragment.getParentFragment();
        while (parentFragment != null) {
            if (clazz.isInstance(parentFragment)) {
                return clazz.cast(parentFragment);
            }
            parentFragment = parentFragment.getParentFragment();
        }
        return null;
    }

}
