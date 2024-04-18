package com.mascherpa.webscraping.FavouriteRio;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String INTEGER_KEY = "rioValue";

    public static void saveInteger(Context context, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(INTEGER_KEY, value);
        editor.apply();
    }
    public static int getRioFavs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(INTEGER_KEY, 9999); // Si no se encuentra la clave, devuelve 0 por defecto
    }


}
