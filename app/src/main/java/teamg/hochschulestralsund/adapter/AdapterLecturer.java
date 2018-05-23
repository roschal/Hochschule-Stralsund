package teamg.hochschulestralsund.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import teamg.hochschulestralsund.sql.Lecturer;
import teamg.hochschulestralsund.sql.Location;

public class AdapterLecturer extends ArrayAdapter<Lecturer> {

    private ArrayList<Lecturer> lecturers;
    private ArrayList<Lecturer> allLecturers;

    private Context context;
    private int resource;

    private ListFilter listFilter = new ListFilter();

    public AdapterLecturer(Context context, int resource, ArrayList<Lecturer> lecturers) {
        super(context, resource, lecturers);
        this.lecturers = lecturers;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return lecturers.size();
    }

    @Override
    public Lecturer getItem(int position) {
        Lecturer lecturer = lecturers.get(position);
        return lecturer;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resource, parent, false);
        }

        TextView textView = new TextView(getContext());
        textView.setText(getItem(position).toString());

        return textView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (allLecturers == null) {
                synchronized (lock) {
                    allLecturers = new ArrayList<Lecturer>(lecturers);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = allLecturers;
                    results.count = allLecturers.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<Lecturer> matchValues = new ArrayList<Lecturer>();

                for (int i = 0; i < lecturers.size(); i++) {
                    Lecturer lecturer = lecturers.get(i);
                    /* search for surname and forename and full name */
                    if (lecturer.forename.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(lecturer);
                    }
                    else if (lecturer.surname.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(lecturer);
                    }
                    else if (lecturer.toString().toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(lecturer);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                lecturers = (ArrayList<Lecturer>)results.values;
            } else {
                lecturers = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}