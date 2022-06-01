package ium.unisa.stopsmoking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;

import java.util.Locale;
import java.util.Objects;

import ium.unisa.stopsmoking.databinding.ActivityMainBinding;
import ium.unisa.stopsmoking.util.AppHelper;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lingua
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = sharedPreferences.getString("language", "it");
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, res.getDisplayMetrics());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppHelper.setTheme(sharedPreferences.getString("theme", "system"));
        DynamicColors.applyToActivitiesIfAvailable(getApplication());

        if (sharedPreferences.getBoolean("firstLaunch", true)) {
            Intent i = new Intent(this, FirstLaunchActivity.class);
            startActivity(i);
            finish();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        Objects.requireNonNull(getSupportActionBar()).hide();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_health, R.id.navigation_savings, R.id.navigation_goals, R.id.navigation_advices)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        ImageButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(settingsListener);
    }

    View.OnClickListener settingsListener = view -> {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    };
}