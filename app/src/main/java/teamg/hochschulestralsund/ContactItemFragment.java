package teamg.hochschulestralsund;

import android.app.Fragment;
import android.content.Context;
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
import java.util.Iterator;

import teamg.hochschulestralsund.adapter.ContactMyItemRecyclerViewAdapter;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Person;

/**
 * Fragment mit den Kontaktinformationen zu den Hochschulmitarbeitern.
 *
 */
public class ContactItemFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private ContactItemFragment.OnListFragmentInteractionListener mListener;

    private ArrayList persons;
    private CustomSQL customSQL;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPersons();
        parseBundle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ContactMyItemRecyclerViewAdapter(persons, mListener, this));
        }
        return view;
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

    private void parseBundle() {
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

            //* if a search query was committed
            if (getArguments().containsKey(ContactActivity.CODE_CONTACT_SEARCH_QUERY)) {
                String query = getArguments().getString(ContactActivity.CODE_CONTACT_SEARCH_QUERY, "");

                for (Iterator<Person> it = persons.iterator(); it.hasNext(); ) {
                    if (!it.next().toString().contains(query))
                        it.remove();
                }
            }
        }
    }

    private void getPersons() {
        //* get the persons from sql
        persons = new ArrayList<>();
        customSQL = new CustomSQL(getActivity());

        persons = customSQL.getLecturers();
        customSQL.close();

        //* sort
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person person1, Person person2) {
                return person1.surname.compareTo(person2.surname);
            }
        });
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Person person);
    }
}
