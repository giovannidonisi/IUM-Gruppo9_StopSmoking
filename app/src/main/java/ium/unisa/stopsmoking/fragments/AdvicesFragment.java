package ium.unisa.stopsmoking.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Random;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.databinding.FragmentAdvicesBinding;

public class AdvicesFragment extends Fragment {

    private FragmentAdvicesBinding binding;
    private ArrayList<String> lista;
    private TextView textView1, textView2;
    private MaterialButton leftArrow, rightArrow;
    private ViewFlipper viewFlipper;
    private Animation inFromRight, inFromLeft, outToLeft, outToRight;
    private int counter, currentView = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdvicesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lista = new ArrayList<>();
        textView1 = binding.textAdvices1;
        textView2 = binding.textAdvices2;
        leftArrow = binding.leftArrow;
        rightArrow = binding.rightArrow;
        viewFlipper = binding.viewFlipper;

        Context context = requireContext();
        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        for (int i = 0; i < 3; i++) {
            int resourceId = resources.getIdentifier("advice" + i, "string", packageName);
            lista.add(resources.getString(resourceId));
        }

        inFromRight = AnimationUtils.loadAnimation(context, R.anim.in_from_right);
        outToLeft   = AnimationUtils.loadAnimation(context, R.anim.out_to_left);
        inFromLeft = AnimationUtils.loadAnimation(context, R.anim.in_from_left);
        outToRight   = AnimationUtils.loadAnimation(context, R.anim.out_to_right);

        Random rand = new Random();
        counter = rand.nextInt(lista.size());
        textView1.setText(lista.get(counter));
        leftArrow.setOnClickListener(arrowClickListener);
        rightArrow.setOnClickListener(arrowClickListener);

        return root;
    }

    View.OnClickListener arrowClickListener = view -> {
        int id = view.getId();

        if (id == R.id.left_arrow) {
            counter--;
            if (counter < 0)
                counter = lista.size() - 1;

            if (currentView == 1) {
                textView2.setText(lista.get(counter));
                currentView++;
            } else {
                textView1.setText(lista.get(counter));
                currentView--;
            }

            viewFlipper.setInAnimation(inFromLeft);
            viewFlipper.setOutAnimation(outToRight);
        } else if (id == R.id.right_arrow) {
            counter++;
            if (counter > lista.size() -1)
                counter = 0;

            if (currentView == 1) {
                textView2.setText(lista.get(counter));
                currentView++;
            } else {
                textView1.setText(lista.get(counter));
                currentView--;
            }

            viewFlipper.setInAnimation(inFromRight);
            viewFlipper.setOutAnimation(outToLeft);
        } else return;

        viewFlipper.showNext();
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}