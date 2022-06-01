package ium.unisa.stopsmoking.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.atomic.AtomicBoolean;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.util.AppHelper;

public class SettingsFragment extends PreferenceFragmentCompat {

    SwitchPreference hasQuit;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference contribute = findPreference("contribute");
        Preference theme = findPreference("theme");
        hasQuit = findPreference("has_quit");

        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                theme.setIcon(R.drawable.ic_moon);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                theme.setIcon(R.drawable.ic_sun);
                break;
        }

        contribute.setOnPreferenceClickListener(clickListener);
        hasQuit.setOnPreferenceChangeListener(changeListener);
    }

    Preference.OnPreferenceChangeListener changeListener = (preference, newValue) -> {
        String key = preference.getKey();
        if (key.equals("has_quit")) {
            if (!(boolean) newValue) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(android.R.string.dialog_alert_title)
                        .setMessage(R.string.switch_quit_warning)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            PreferenceManager.getDefaultSharedPreferences(requireContext())
                                    .edit()
                                    .putBoolean("has_quit", false)
                                    .putLong("dateStopped", -1)
                                    .commit();
                            hasQuit.setChecked(false);
                        })
                        .show();
            } else {
                PreferenceManager.getDefaultSharedPreferences(requireContext())
                        .edit()
                        .putBoolean("has_quit", true)
                        .putLong("dateStopped", System.currentTimeMillis())
                        .commit();
                hasQuit.setChecked(true);
            }
        }
        return false;
    };

    Preference.OnPreferenceClickListener clickListener = preference -> {
        String key = preference.getKey();
        if (key.equals("contribute")) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://github.com/giovannidonisi/StopSmoking"));
            startActivity(i);
            return true;
        }
        return false;
    };

}