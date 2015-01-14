package by.evgen.android.apiclient;


/**
 * Created by User on 07.10.2014.
 */
//TODO remove
public class Authorized {

    private static boolean IS_AUTHORIZED = false;

    public static void setLogged(boolean isLogged) {
        IS_AUTHORIZED = isLogged;
    }

    public static boolean isLogged() {
        return IS_AUTHORIZED;
    }
}
