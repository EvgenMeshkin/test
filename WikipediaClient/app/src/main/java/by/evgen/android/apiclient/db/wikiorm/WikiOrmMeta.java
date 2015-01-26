package by.evgen.android.apiclient.db.wikiorm;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * Created by User on 15.12.2014.
 */
public class WikiOrmMeta {

    static <T> T getMetaData(Context context, String name) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = info.metaData;
            if (metaData != null)
                return (T) metaData.get(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return (String) packageManager.getApplicationLabel(info);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
