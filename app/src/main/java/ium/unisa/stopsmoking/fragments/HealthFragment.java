package ium.unisa.stopsmoking.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.databinding.FragmentHealthBinding;
import ium.unisa.stopsmoking.util.AppHelper;

public class HealthFragment extends Fragment {

    private FragmentHealthBinding binding;
    int days;

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
            days = AppHelper.getDateDifferenceInDays(System.currentTimeMillis(), stoppedDateInMillis);
            nonSmokedCigarettes = quantity * days;
            s = resources.getString(R.string.cigarettes_saved, nonSmokedCigarettes, nonSmokedCigarettes * 13 / 1440);
        } else {
            days = 0;
            nonSmokedCigarettes = quantity * 30;
            s = resources.getString(R.string.cigarettes_would_save, nonSmokedCigarettes, nonSmokedCigarettes * 12, nonSmokedCigarettes * 12 * 14 / 1440);
        }
        textView.setText(s);
        buildCards();

        return root;
    }

    private void buildCards() {
        Context context = requireContext();
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        View view = binding.getRoot();
        ArrayList<Integer> lista = AppHelper.getReachedInDays();
        int size = lista.size();

        for (int i = 0; i < size; i++) {
            int resourceId = resources.getIdentifier("reached" + i, "id", packageName);
            TextView tv = view.findViewById(resourceId);
            int reachedIn = lista.get(i);

            if (days < reachedIn) {
                int count = reachedIn - days;
                tv.setText(resources.getQuantityString(R.plurals.reached_in, count, count));
            }
            else {
                tv.setTextColor(resources.getColor(R.color.success));
                tv.setText(resources.getString(R.string.completed));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}