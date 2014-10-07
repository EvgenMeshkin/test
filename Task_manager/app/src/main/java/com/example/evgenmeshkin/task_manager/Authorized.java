package com.example.evgenmeshkin.task_manager;

/**
 * Created by User on 07.10.2014.
 */
public class Authorized {

    private static boolean IS_AUTHORIZED = false;

    public static void setLogged(boolean isLogged) {
        IS_AUTHORIZED = isLogged;
    }

    public static boolean isLogged() {
        return IS_AUTHORIZED;
    }
}
