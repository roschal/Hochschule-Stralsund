package teamg.hochschulestralsund;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import teamg.hochschulestralsund.adapter.LectureMyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Person;

public class LectureItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private ArrayList<Lecture> lectures;

    public LectureItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        getPersons();
        parseBundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.lecture_fragment_item_list, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;

            Context context = view.getContext();

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new LectureMyItemRecyclerViewAdapter(lectures, mListener, this));
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

    /**parse the bundle
     *
     */
    private void parseBundle() {
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private void getPersons() {
        //* get the persons from sql
        lectures = new ArrayList<>();
        CustomSQL customSQL = new CustomSQL(getActivity());

        lectures = customSQL.getLectures();
        customSQL.close();

        //* sort
        Collections.sort(lectures, new Comparator<Lecture>() {
            @Override
            public int compare(Lecture lecture1, Lecture lecture2) {
                return lecture1.event_title.compareTo(lecture2.event_title);
            }
        });
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Lecture lecture);
    }

}
