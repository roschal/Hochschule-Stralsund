package teamg.hochschulestralsund.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import teamg.hochschulestralsund.MensaItemFragment;
import teamg.hochschulestralsund.MensaItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.R;
import teamg.hochschulestralsund.sql.Meal;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meal} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MensaMyItemRecyclerViewAdapter extends RecyclerView.Adapter<MensaMyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Meal> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final MensaItemFragment itemFragment;

    public MensaMyItemRecyclerViewAdapter(ArrayList<Meal> items, OnListFragmentInteractionListener listener, MensaItemFragment itemFragment) {
        mValues = items;
        mListener = listener;
        this.itemFragment = itemFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mensa_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.meal = mValues.get(position);
        holder.textView_mensa_title.setText(holder.meal.meal_title);
        holder.textView_mensa_title.setText("Zusatzstoffe - " + holder.meal.meal_ingredients);
        holder.textView_mensa_price_student.setText("S: " + getPrice(holder.meal.meal_price_student));
        holder.textView_mensa_price_worker.setText("M: " + getPrice(holder.meal.meal_price_worker));
        holder.textView_mensa_price_guest.setText("G: " + getPrice(holder.meal.meal_price_guest));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_mensa_title;
        public final TextView textView_mensa_ingredients;
        public final TextView textView_mensa_price_student;
        public final TextView textView_mensa_price_worker;
        public final TextView textView_mensa_price_guest;

        public Meal meal;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView_mensa_title = view.findViewById(R.id.textView_mensa_title);
            textView_mensa_ingredients = view.findViewById(R.id.textView_mensa_ingredients);
            textView_mensa_price_student = view.findViewById(R.id.textView_mensa_price_student);
            textView_mensa_price_worker = view.findViewById(R.id.textView_mensa_price_worker);
            textView_mensa_price_guest = view.findViewById(R.id.textView_mensa_price_guest);
        }
    }

    private String getPrice(double price) {
        DecimalFormat f = new DecimalFormat("#0.00");

        String priceAsStr = f.format(price);
        priceAsStr = priceAsStr.replace(".", ",") + " €";
        Log.e(priceAsStr, priceAsStr);
        return priceAsStr;
    }
}
