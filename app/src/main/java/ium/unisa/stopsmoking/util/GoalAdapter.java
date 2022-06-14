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
import androidx.preference.PreferenceManager;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ium.unisa.stopsmoking.R;
import ium.unisa.stopsmoking.db.Goal;

public class GoalAdapter extends ArrayAdapter<Goal> {

    public GoalAdapter(Context context, List<Goal> goals){
        super(context,0, goals);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Goal goal = getItem(position);
        if(convertView == null)
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
        linearProgressIndicator.setProgressCompat(50, true);

        // Divido per 20 che è il numero di sigarette in un pacchetto, così da ottenere il prezzo per sigaretta
        float pricePerCigarette = Float.parseFloat(sharedPreferences.getString("price", "5")) / 20;
        int quantity = Integer.parseInt(sharedPreferences.getString("quantity", "10"));
        long stoppedDateInMillis = sharedPreferences.getLong("dateStopped", -1);

        if (stoppedDateInMillis > 0) {
            int days = AppHelper.getDateDifferenceInDays(System.currentTimeMillis(), stoppedDateInMillis);
            int moneySaved = Math.round(quantity * pricePerCigarette * days);
            String s2 = String.format(Locale.getDefault(),"%.2f", goal.getPrice() - moneySaved);
            description.setText(resources.getString(R.string.already_saved, moneySaved, s2, 7));
        }

        return convertView;
    }
}
