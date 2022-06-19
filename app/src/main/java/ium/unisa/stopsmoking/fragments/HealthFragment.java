package ium.unisa.stopsmoking.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.databinding.FragmentHealthBinding;
import ium.unisa.stopsmoking.db.Benefit;
import ium.unisa.stopsmoking.util.AppHelper;
import ium.unisa.stopsmoking.util.BenefitAdapter;

public class HealthFragment extends Fragment {

    private FragmentHealthBinding binding;
    private int days;
    private long stoppedDateInMillis;
    private ListView benefitList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHealthBinding.inflate(inflater, container, false);
        Resources resources = requireContext().getResources();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        TextView textView = binding.textHealth;

        int quantity = Integer.parseInt(sharedPreferences.getString("quantity", "10"));
        stoppedDateInMillis = sharedPreferences.getLong("dateStopped", -1);
        int nonSmokedCigarettes;
        String s;

        if (stoppedDateInMillis > 0) {
            days = AppHelper.getDateDifferenceInDays(System.currentTimeMillis(), stoppedDateInMillis);
            nonSmokedCigarettes = quantity * days;
            s = resources.getString(R.string.cigarettes_saved, nonSmokedCigarettes, nonSmokedCigarettes * 13 / 1440);
        } else {
            days = 0;
            nonSmokedCigarettes = quantity * 30;
            s = resources.getString(R.string.cigarettes_would_save, nonSmokedCigarettes, nonSmokedCigarettes * 12, nonSmokedCigarettes * 12 * 14 / 1440);
        }
        textView.setText(s);
        benefitList = binding.benefitList;
        buildCards();

        return binding.getRoot();
    }

    private void buildCards() {
        Context context = requireContext();
        Resources resources = context.getResources();
        int[] lista = AppHelper.getReachedInDays();
        ArrayList<Benefit> benefitArrayList = new ArrayList<>();

        for (int i = 0; i < lista.length; i++) {
            int reachedIn = lista[i];
            int percent;
            int count = reachedIn - days;

            if (stoppedDateInMillis > 0) {
                if (days < reachedIn) {
                    percent = days * 100 / reachedIn;
                } else {
                    percent = 100;
                }
            } else {
                percent = 0;
            }

            int resourceId = resources.getIdentifier("title" + i, "string", context.getPackageName());
            String title = resources.getString(resourceId);
            resourceId = resources.getIdentifier("secondary_text" + i, "string", context.getPackageName());
            String description = resources.getString(resourceId);
            Benefit benefit = new Benefit(title, description, count , percent);
            benefitArrayList.add(benefit);
        }

        BenefitAdapter benefitAdapter = new BenefitAdapter(requireContext(), benefitArrayList);
        benefitList.setAdapter(benefitAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}