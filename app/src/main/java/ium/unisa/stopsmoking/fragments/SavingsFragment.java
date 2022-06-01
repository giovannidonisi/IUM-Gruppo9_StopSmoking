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
import ium.unisa.stopsmoking.databinding.FragmentSavingsBinding;
import ium.unisa.stopsmoking.util.AppHelper;

public class SavingsFragment extends Fragment {

    private FragmentSavingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Resources resources = requireContext().getResources();

        TextView textView = binding.textSavings;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Divido per 20 che è il numero di sigarette in un pacchetto, così da ottenere il prezzo per sigaretta
        float pricePerCigarette = Float.parseFloat(sharedPreferences.getString("price", "5")) / 20;
        int quantity = Integer.parseInt(sharedPreferences.getString("quantity", "10"));
        long stoppedDateInMillis = sharedPreferences.getLong("dateStopped", -1);
        int moneyPerMonth = Math.round(quantity * pricePerCigarette * 30);
        String s;

        if (stoppedDateInMillis > 0) {
            int days = AppHelper.getDateDifferenceInDays(System.currentTimeMillis(), stoppedDateInMillis);
            int moneySaved = Math.round(quantity * pricePerCigarette * days);
            s = String.format(resources.getString(R.string.money_saved), moneySaved, moneyPerMonth, moneyPerMonth * 12);
        } else {
            s = String.format(resources.getString(R.string.money_would_save), moneyPerMonth, moneyPerMonth * 12);
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