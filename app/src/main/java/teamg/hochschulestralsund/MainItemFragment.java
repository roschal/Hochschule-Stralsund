package teamg.hochschulestralsund;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import teamg.hochschulestralsund.adapter.MyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Person;

public class MainItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ArrayList<Lecture> lectures;
    private CustomSQL customSQL;
    private Calendar calendar;

    private TextView left;
    private TextView center;
    private TextView right;

    public MainItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        calendar = Calendar.getInstance();

        /* get the day to show */
        if (bundle != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            calendar.setTimeInMillis(bundle.getLong(MainActivity.CODE_SHOW_DAY, Calendar.getInstance().getTimeInMillis()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        /* set the adapter */
        lectures = new ArrayList<>();
        customSQL = new CustomSQL(getActivity());

        lectures = customSQL.getLectures(calendar.get(Calendar.DAY_OF_WEEK));
        customSQL.close();

        //* sort
        Collections.sort(lectures, new Comparator<Lecture>() {
            @Override
            public int compare(Lecture lecture1, Lecture lecture2) {
                long time1 = lecture1.event_begin.getTimeInMillis();
                long time2 = lecture2.event_begin.getTimeInMillis();

                if(time1 < time2)
                    return -1;
                else if (time1 == time2)
                    return 0;

                return 1;
            }
        });

        if (view instanceof ConstraintLayout) {
            /* left and right navigation */
            left = view.findViewById(R.id.textview_toolbar_left);
            center = view.findViewById(R.id.textview_toolbar_center);
            right = view.findViewById(R.id.textview_toolbar_right);

            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).showPreviosDay(getFragmentManager());
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).showNextDay(getFragmentManager());
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

    private void setDays() {
        left.setText(getDayAndDate(MainActivity.getPreviosDay(calendar)));
        center.setText(getDayAndDate(calendar));
        right.setText(getDayAndDate(MainActivity.getNextDay(calendar)));
    }

    /* convert day of week from int to String */
    private String getDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);

        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }

    /* convert day of week and Date from int to String */
    private String getDayAndDate(Calendar calendar) {
        String display = "";
        Calendar calendarToday = Calendar.getInstance();

        if (calendar.get(Calendar.DATE) == calendarToday.get(Calendar.DATE)) {
            display += "(" + getString(R.string.today) + ") ";
        }

        display += calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) +
                ", " +
                MainActivity.parseDate(calendar);

        return display;
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
        void onListFragmentInteraction(Lecture lecture);
    }
}
