package ium.unisa.stopsmoking.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ium.unisa.stopsmoking.databinding.FragmentAdvicesBinding;
import ium.unisa.stopsmoking.util.AppHelper;

public class AdvicesFragment extends Fragment {

    private FragmentAdvicesBinding binding;
    private ArrayList<String> lista;
    private TextView textView;
    //private ImageButton leftArrow, rightArrow;
    private MaterialButton leftArrow, rightArrow;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdvicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lista = new ArrayList<>();
        textView = binding.textAdvices;
        leftArrow = binding.leftArrow;
        rightArrow = binding.rightArrow;

        Context context = requireContext();
        Resources resources = context.getResources();

        for (int i = 0; i < 3; i++) {
            int resourceId = resources.getIdentifier("advice" + i, "string", context.getPackageName());
            lista.add(resources.getString(resourceId));
        }

        leftArrow.setOnClickListener(arrowClickListener);
        rightArrow.setOnClickListener(arrowClickListener);
        textView.setText(lista.get(0));

        return root;
    }

    View.OnClickListener arrowClickListener = view -> {
        AppHelper.log("Clicked " + view.getId());
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}