package ium.unisa.stopsmoking.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import ium.unisa.stopsmoking.util.AppHelper;
import ium.unisa.stopsmoking.util.DecimalDigitsInputFilter;
import ium.unisa.stopsmoking.util.GoalAdapter;

public class GoalsFragment extends Fragment {

    private FragmentGoalsBinding binding;
    private ArrayList<Goal> goals;
    private GoalAdapter goalAdapter;
    private TextView textView;
    private ListView listView;
    private SQLiteManager sqLiteManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = binding.listgoals;
        textView = binding.textGoals;
        sqLiteManager = new SQLiteManager(requireContext());
        goals = sqLiteManager.getGoalListArray();
        goalAdapter = new GoalAdapter(requireContext(), goals);
        listView.setAdapter(goalAdapter);
        listView.setOnItemClickListener(onItemClickListener);

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
        ti2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(9, 2)});

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.add_goal)
                .setView(linearLayout)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String name = ti1.getText().toString().trim();
                    if (name.isEmpty()) {
                        AppHelper.showShortToast(requireContext(), R.string.invalid_name);
                        return;
                    }

                    double price;
                    try {
                        price = Double.parseDouble(ti2.getText().toString());
                        if (price < 0.01)
                            throw new IllegalArgumentException();
                    } catch (IllegalArgumentException e) {
                        AppHelper.showShortToast(requireContext(), R.string.invalid_price);
                        e.printStackTrace();
                        return;
                    }

                    Goal newGoal = new Goal(name.replace("\n", " "), price);
                    goals.add(newGoal);
                    sqLiteManager.addGoalToDatabase(newGoal);
                    goalAdapter.notifyDataSetChanged();
                    textView.setVisibility(View.INVISIBLE);
                })
                .show();
    };

    AdapterView.OnItemClickListener onItemClickListener = (adapterView, view, i, l) -> {
        Goal goal = (Goal) listView.getItemAtPosition(i);
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_add_goal, null);
        final TextInputEditText ti1 = linearLayout.findViewById(R.id.ti1);
        final TextInputEditText ti2 = linearLayout.findViewById(R.id.ti2);
        ti2.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(9, 2)});
        ti2.setText(String.valueOf(goal.getPrice()));
        ti1.setText(goal.getName());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.edit_goal)
                .setView(linearLayout)
                .setNegativeButton(R.string.delete, (dialog, which) -> {
                    sqLiteManager.deleteGoalFromDatabase(goal);
                    goals.remove(goal);
                    goalAdapter.notifyDataSetChanged();
                })
                .setNeutralButton(android.R.string.cancel, null)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String name = ti1.getText().toString().trim();
                    if (name.isEmpty()) {
                        AppHelper.showShortToast(requireContext(), R.string.invalid_name);
                        return;
                    }

                    double price;
                    try {
                        price = Double.parseDouble(ti2.getText().toString());
                        if (price < 0.01)
                            throw new IllegalArgumentException();
                    } catch (IllegalArgumentException e) {
                        AppHelper.showShortToast(requireContext(), R.string.invalid_price);
                        e.printStackTrace();
                        return;
                    }

                    Goal newGoal = new Goal(goal.getId(), name.replace("\n", " "), price);
                    sqLiteManager.updateGoalInDatabase(newGoal);
                    int index = goals.indexOf(goal);
                    goals.remove(index);
                    goals.add(index, newGoal);
                    goalAdapter.notifyDataSetChanged();
                })
                .show();
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}