package com.example.evgen.apiclient.source;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.CoreApplication;
import com.example.evgen.apiclient.auth.VkOAuthHelper;

import java.io.InputStream;

/**
 * Created by evgen on 15.11.2014.
 */
public class VkDataSource extends HttpDataSource {

    public static final String KEY = "VkDataSource";

    public static VkDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
//        String signUrl = VkOAuthHelper.sign(p);
//        String versionValue = Uri.parse(signUrl).getQueryParameter(Api.VERSION_PARAM);
//        if (TextUtils.isEmpty(versionValue)) {
//            signUrl = signUrl + "&" + Api.VERSION_PARAM + "=" + Api.VERSION_VALUE;
//        }
        return super.getResult(p);
    }

}