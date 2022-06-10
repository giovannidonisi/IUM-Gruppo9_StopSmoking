package ium.unisa.stopsmoking;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class FirstLaunchActivity extends AppCompatActivity {

    private SwitchMaterial switchStopped;
    private DatePicker datePicker;
    private MaterialButton okButton;
    private TextView tvQuantity;
    private EditText etQuantity;
    private EditText etPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        Objects.requireNonNull(getSupportActionBar()).hide();

        switchStopped = findViewById(R.id.switch_stopped);
        datePicker = findViewById(R.id.date_pick);
        okButton = findViewById(R.id.ok_button);
        tvQuantity = findViewById(R.id.tv_quantity);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);

        datePicker.setMinDate(0L);
        datePicker.setMaxDate(System.currentTimeMillis());

        okButton.setOnClickListener(listener);
        switchStopped.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                datePicker.setVisibility(View.VISIBLE);
                tvQuantity.setText(R.string.first_launch_quantity2);
            }
            else {
                datePicker.setVisibility(View.INVISIBLE);
                tvQuantity.setText(R.string.first_launch_quantity1);
            }
        });
    }

    View.OnClickListener listener = view -> {
        long l = -1;
        if (switchStopped.isChecked()) {
            int y = datePicker.getYear();
            int m = datePicker.getMonth();
            int d = datePicker.getDayOfMonth();
            Calendar calendar = Calendar.getInstance();
            calendar.set(y, m, d);
            l = calendar.getTimeInMillis();
        }

        String q = etQuantity.getText().toString().trim();
        if (q.isEmpty())
            q = "10";

        String p = etPrice.getText().toString().trim();
        if (p.isEmpty())
            p = "5";

        PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .edit()
                .putBoolean("has_quit", switchStopped.isChecked())
                .putLong("dateStopped", l)
                .putString("price", p)
                .putString("quantity", q)
                .putBoolean("firstLaunch", false)
                .apply();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    };
}