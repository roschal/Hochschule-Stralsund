package teamg.hochschulestralsund;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;

public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private CustomSQL customSQL;
    private int DAY_OF_WEEK;

    private TextView left;
    private TextView center;
    private TextView right;

    public ItemFragment() {
    }

    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        /* get the day to show */
        if (bundle != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            DAY_OF_WEEK = bundle.getInt(MainActivity.CODE_SHOW_DAY, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        }

        init(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        /* set the adapter */
        ArrayList<Lecture> lectures = customSQL.getLectures(DAY_OF_WEEK);

        if (view instanceof ConstraintLayout) {
            /* left and right navigation */
            left = view.findViewById(R.id.textview_toolbar_left);
            center = view.findViewById(R.id.textview_toolbar_center);
            right = view.findViewById(R.id.textview_toolbar_right);

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).showPreviosDay();
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).showNextDay();
                }
            });

            RecyclerView recyclerView = view.findViewById(R.id.list);

            Context context = recyclerView.getContext();

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(lectures, mListener, this));

            setDays();
        }

        return view;
    }

    /* update the list with lectures when lecture was edited or deleted*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void setDays() {
        left.setText(getDay(MainActivity.getPreviosDay(DAY_OF_WEEK)));
        center.setText(getDay(DAY_OF_WEEK));
        right.setText(getDay(MainActivity.getNextDay(DAY_OF_WEEK)));
    }

    /* convert day of week from int to String */
    private String getDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);

        return calendar.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault());
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

    /* init the database */
    public void init(Context context) {
        customSQL = new CustomSQL(context);
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Lecture lecture);
    }
}
