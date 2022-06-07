package ium.unisa.stopsmoking.util;

import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class AppHelper {

    public static final String LOGGER_TAG = "DEBUGGING";

    public static int getDateDifferenceInDays(long d1, long d2) {
        long a = (d1 - d2) / 86400000;
        return (int) a;
    }

    public static void setTheme(String theme) {
        switch (theme) {
            case "light":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public static ArrayList<Integer> getReachedInDays() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(1);
        lista.add(1);
        lista.add(1);
        lista.add(1);
        lista.add(1);
        lista.add(2);
        lista.add(2);
        lista.add(3);
        lista.add(3);
        lista.add(4);
        lista.add(5);
        lista.add(7);
        lista.add(7);
        lista.add(60);
        lista.add(90);
        lista.add(90);
        lista.add(120);
        lista.add(120);
        lista.add(120);
        lista.add(365);
        lista.add(365);
        lista.add(1825);
        lista.add(2555);
        lista.add(2555);
        lista.add(3650);
        return lista;
    }

    public static void log(Object what) {
        Log.d(LOGGER_TAG, String.valueOf(what));
    }

}
