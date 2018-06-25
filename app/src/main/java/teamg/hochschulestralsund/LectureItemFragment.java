package teamg.hochschulestralsund;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import teamg.hochschulestralsund.adapter.LectureMyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Person;

/**
 * FragmentItem zum Anzeigen der Vorlesungen in der Liste der Vorlesungen.
 */
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
        setHasOptionsMenu(true);

        getPersons();
        parseBundle();
    }

    @Override
    /**create the menu
     *
     * @return boolean
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lecture, menu);

        /* set the icon color for 2 menu icons */
        for (int i = 0; i < 2; i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    /**override click handler on menu
     *
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /* show activity to add a new lecture */
            case R.id.action_add_lecture:
                LectureActivity.addLecture(getFragmentManager());

                return false;

            case R.id.action_delete_lectures:
                CustomSQL customSQL = new CustomSQL(getActivity());
                customSQL.deleteLectures();
                customSQL.close();

                LectureActivity.showLectures(getFragmentManager(), false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

    /**
     * parse the bundle
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
