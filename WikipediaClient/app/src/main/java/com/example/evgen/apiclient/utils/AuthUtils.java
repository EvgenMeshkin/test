package com.example.evgen.apiclient.utils;

import com.example.evgen.apiclient.auth.VkOAuthHelper;

/**
 * Created by User on 30.10.2014.
 */
//TODO
public class AuthUtils {
    private static boolean IS_AUTHORIZED = false;

    public static boolean isLogged() {
        return VkOAuthHelper.isLogged();
    }

}