package ium.unisa.stopsmoking.util;

import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

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

    public static int[] getReachedInDays() {
        return new int[] {1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 5, 7, 7, 60, 90,
                90, 120, 120, 120, 365, 365, 1825, 2555, 2555, 3650};
    }

    public static void log(Object what) {
        Log.d(LOGGER_TAG, String.valueOf(what));
    }

}
