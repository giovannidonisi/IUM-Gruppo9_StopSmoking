package ium.unisa.stopsmoking.fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.databinding.FragmentHealthBinding;
import ium.unisa.stopsmoking.util.AppHelper;

public class HealthFragment extends Fragment {

    private FragmentHealthBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHealthBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Resources resources = requireContext().getResources();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        TextView textView = binding.textHealth;

        int quantity = Integer.parseInt(sharedPreferences.getString("quantity", "10"));
        long stoppedDateInMillis = sharedPreferences.getLong("dateStopped", -1);
        int nonSmokedCigarettes;
        String s;

        if(stoppedDateInMillis > 0) {
            int days = AppHelper.getDateDifferenceInDays(System.currentTimeMillis(), stoppedDateInMillis);
            nonSmokedCigarettes = quantity * days;
            s = String.format(resources.getString(R.string.cigarettes_saved), nonSmokedCigarettes, nonSmokedCigarettes * 13 / 1440);
        } else {
            nonSmokedCigarettes = quantity * 30;
            s = String.format(resources.getString(R.string.cigarettes_would_save), nonSmokedCigarettes, nonSmokedCigarettes * 12, nonSmokedCigarettes * 12 * 14 / 1440);
        }
        textView.setText(s);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}