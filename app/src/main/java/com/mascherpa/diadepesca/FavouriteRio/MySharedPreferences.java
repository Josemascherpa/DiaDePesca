package com.mascherpa.diadepesca.FavouriteRio;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String INTEGER_KEY = "rioValue";

    public static void saveInteger(Context context, int value) {//Guardo valor pasnado el int y el contexto
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(INTEGER_KEY, value);
        editor.apply();
    }
    public static int getRioFavs(Context context) {//Obtengo el rio favorito, devolviendo el integer o sea valor del rio, sino 9999
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(INTEGER_KEY, 9999);
    }


}
