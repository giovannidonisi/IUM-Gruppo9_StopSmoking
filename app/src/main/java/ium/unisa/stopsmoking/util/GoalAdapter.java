package ium.unisa.stopsmoking.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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

import java.text.NumberFormat;
import java.util.List;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.db.Goal;

public class GoalAdapter extends ArrayAdapter<Goal> {

    public GoalAdapter(Context context, List<Goal> goals) {
        super(context, 0, goals);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Goal goal = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.goal_card, parent, false);

        Context context = getContext();
        Resources resources = context.getResources();
        TextView title = convertView.findViewById(R.id.goal_title);
        TextView price = convertView.findViewById(R.id.goal_price);
        TextView description = convertView.findViewById(R.id.goal_description);
        LinearProgressIndicator linearProgressIndicator = convertView.findViewById(R.id.progress);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        title.setText(goal.getName());
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        price.setText(resources.getString(R.string.money, formatter.format(goal.getPrice())));

        // Divido per 20 che è il numero di sigarette in un pacchetto, così da ottenere il prezzo per sigaretta
        float pricePerCigarette = Float.parseFloat(sharedPreferences.getString("price", "5")) / 20;
        int quantity = Integer.parseInt(sharedPreferences.getString("quantity", "10"));
        long stoppedDateInMillis = sharedPreferences.getLong("dateStopped", -1);
        int moneyPerDay = Math.round(quantity * pricePerCigarette);
        int percent;
        String s2;
        int totalDays = (int) Math.round(goal.getPrice() / moneyPerDay);

        if (stoppedDateInMillis > 0) {
            int days = AppHelper.getDateDifferenceInDays(System.currentTimeMillis(), stoppedDateInMillis);
            int moneySaved = Math.round(quantity * pricePerCigarette * days);
            double moneyLeft = goal.getPrice() - moneySaved;
            if (moneyLeft > 0) {
                int daysLeft = (int) Math.round(moneyLeft / moneyPerDay);
                percent = (totalDays - daysLeft) * 100 / totalDays;
                s2 = resources.getString(R.string.already_saved, moneySaved,
                        formatter.format(moneyLeft), daysLeft);
            } else {
                percent = 100;
                s2 = resources.getString(R.string.completed);
                description.setTextColor(ContextCompat.getColor(context, R.color.success));
            }
        } else {
            percent = 0;
            s2 = resources.getQuantityString(R.plurals.goal_days, totalDays, totalDays);
        }
        linearProgressIndicator.setProgressCompat(percent, true);
        description.setText(s2);

        return convertView;
    }
}
