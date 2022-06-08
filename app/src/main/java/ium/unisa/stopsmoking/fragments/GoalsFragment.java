package ium.unisa.stopsmoking.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.databinding.FragmentGoalsBinding;
import ium.unisa.stopsmoking.util.AppHelper;
import ium.unisa.stopsmoking.util.DecimalDigitsInputFilter;

public class GoalsFragment extends Fragment {

    private FragmentGoalsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView textView = binding.textGoals;
        FloatingActionButton floatingButton = binding.buttonAdd;

        textView.setText(R.string.no_goals);
        floatingButton.setOnClickListener(buttonAddListener);
        return root;
    }

    View.OnClickListener buttonAddListener = view -> {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_add_goal, null);
        final TextInputEditText ti1 = linearLayout.findViewById(R.id.ti1);
        final TextInputEditText ti2 = linearLayout.findViewById(R.id.ti2);
        ti2.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(9,2)});

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.add_goal)
                .setView(linearLayout)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String name = ti1.getText().toString().trim();
                    if (name.isEmpty()) {
                        showToast(R.string.invalid_name);
                        return;
                    }

                    double price;
                    try {
                        price = Double.parseDouble(ti2.getText().toString());
                        if (price < 0.01)
                            throw new IllegalArgumentException();
                    } catch (IllegalArgumentException e) {
                        showToast(R.string.invalid_price);
                        e.printStackTrace();
                        return;
                    }

                    AppHelper.log("Nome: " + name);
                    AppHelper.log("Prezzo: " + price);

                    // TODO: aggiungere obiettivo al DB

                })
                .show();
    };

    private void showToast(int resId) {
        Toast.makeText(requireContext(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}