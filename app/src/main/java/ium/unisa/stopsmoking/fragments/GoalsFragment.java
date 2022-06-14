package ium.unisa.stopsmoking.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.databinding.FragmentGoalsBinding;
import ium.unisa.stopsmoking.db.Goal;
import ium.unisa.stopsmoking.db.SQLiteManager;
import ium.unisa.stopsmoking.util.DecimalDigitsInputFilter;
import ium.unisa.stopsmoking.util.GoalAdapter;

public class GoalsFragment extends Fragment {

    private FragmentGoalsBinding binding;
    private Goal selectedGoal;
    private ArrayList<Goal> goals;
    private GoalAdapter goalAdapter;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = binding.listgoals;
        textView = binding.textGoals;
        SQLiteManager sqLiteManager = new SQLiteManager(requireContext());
        goals = sqLiteManager.getGoalListArray();
        goalAdapter = new GoalAdapter(requireContext(), goals);
        listView.setAdapter(goalAdapter);

        if (goals.size() > 0)
            textView.setVisibility(View.INVISIBLE);

        FloatingActionButton floatingButton = binding.buttonAdd;
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

                    SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(requireContext());
                    if (selectedGoal == null) {
                        int id = goals.size();
                        Goal newGoal= new Goal(id, name, price);
                        goals.add(newGoal);
                        sqLiteManager.addGoalToDatabase(newGoal);
                    } else {
                        selectedGoal.setName(name);
                        selectedGoal.setPrice(price);
                        sqLiteManager.updateGoalInDatabase(selectedGoal);
                    }
                    goalAdapter.notifyDataSetChanged();
                    textView.setVisibility(View.INVISIBLE);
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