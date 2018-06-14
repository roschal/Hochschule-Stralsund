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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import teamg.hochschulestralsund.adapter.MeetingMyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.adapter.MyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Meeting;

public class MeetingItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ArrayList<Meeting> meetings;

    public MeetingItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        /* get the day to show */
        if (bundle != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.meeting_fragment_item_list, container, false);

        /* set the adapter */
        meetings = new ArrayList<>();
        CustomSQL customSQL = new CustomSQL(getActivity());

        meetings = customSQL.getMeetings();
        customSQL.close();

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;

            Context context = view.getContext();

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MeetingMyItemRecyclerViewAdapter(meetings, mListener, this));
        }

        return view;
    }

    /* update the list with lectures when lecture was edited or deleted*/
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
        void onListFragmentInteraction(Meeting meeting);
    }

}
