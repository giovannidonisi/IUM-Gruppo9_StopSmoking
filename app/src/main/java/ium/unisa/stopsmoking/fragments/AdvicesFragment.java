package ium.unisa.stopsmoking.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

import ium.unisa.stopsmoking.databinding.FragmentAdvicesBinding;

public class AdvicesFragment extends Fragment {

    private FragmentAdvicesBinding binding;
    private ArrayList<String> lista;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdvicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lista = new ArrayList<>();
        textView = binding.textAdvices;
        Context context = requireContext();
        Resources resources = context.getResources();

        for (int i = 0; i < 3; i++) {
            int resourceId = resources.getIdentifier("advice" + i, "string", context.getPackageName());
            lista.add(resources.getString(resourceId));
        }

        textView.setText(lista.get(0));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}