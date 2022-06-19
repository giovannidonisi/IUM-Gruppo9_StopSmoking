package ium.unisa.stopsmoking.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.db.Benefit;

public class BenefitAdapter extends ArrayAdapter<Benefit> {

    public BenefitAdapter(Context context, List<Benefit> benefits) {
        super(context, 0, benefits);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.benefit_card, parent, false);

        Benefit benefit = getItem(position);

        TextView title = convertView.findViewById(R.id.text_primary);
        TextView description = convertView.findViewById(R.id.text_secondary);
        TextView reachedIn = convertView.findViewById(R.id.reachedIn);
        LinearProgressIndicator progress = convertView.findViewById(R.id.progress);

        title.setText(benefit.getTitle());
        description.setText(benefit.getDescription());
        progress.setProgressCompat(benefit.getPercent(), true);
        int days = benefit.getReachedIn();
        long stoppedDateInMillis = PreferenceManager.getDefaultSharedPreferences(
                getContext()).getLong("dateStopped", -1);

        if (stoppedDateInMillis > 0) {
            if (days > 0) {
                reachedIn.setText(getContext().getResources().getQuantityString(R.plurals.reached_in, days, days));
                reachedIn.setTextColor(ContextCompat.getColor(getContext(), R.color.onPrimaryColor));
            } else {
                reachedIn.setText(getContext().getResources().getString(R.string.completed));
                reachedIn.setTextColor(ContextCompat.getColor(getContext(), R.color.success));
            }
        } else {
            reachedIn.setText(getContext().getResources().getQuantityString(R.plurals.would_reach_in, days, days));
            reachedIn.setTextColor(ContextCompat.getColor(getContext(), R.color.onPrimaryColor));
        }

        return convertView;
    }
}
