package teamg.hochschulestralsund;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import teamg.hochschulestralsund.adapter.MeetingMyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.adapter.MensaMyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.connect.ParserMensa;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Meal;
import teamg.hochschulestralsund.sql.Meeting;

public class MensaItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ArrayList<Meal> meals = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();;

    public MensaItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle bundle = this.getArguments();

        /* get the day to show */
        if (bundle != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            calendar.setTimeInMillis(bundle.getLong(MensaActivity.CODE_SHOW_MENSA, Calendar.getInstance().getTimeInMillis()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mensa_fragment_item_list, container, false);

        try {
            Log.e("starting", "starting");

            ParserMensa parserMensa = new ParserMensa(getActivity(), calendar);
            parserMensa.execute();

            meals = parserMensa.get();

            for (int i = 0; i < meals.size(); i++ )
                Log.e("sdfdsf", meals.get(i).meal_title);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;

            Context context = view.getContext();

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MensaMyItemRecyclerViewAdapter(meals, mListener, this));
        }

        return view;
    }

    /* update the list with meetings when meeting was edited or deleted*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Meal meal);
    }

}
