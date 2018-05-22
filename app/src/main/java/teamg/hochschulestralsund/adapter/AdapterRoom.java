package teamg.hochschulestralsund.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teamg.hochschulestralsund.R;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Location;

public class AdapterRoom extends ArrayAdapter {

    private ArrayList<Location> locations;
    private ArrayList<Location> allLocations;

    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();

    public AdapterRoom(Context context, int resource, ArrayList<Location> locations) {
        super(context, resource, locations);
        this.locations = locations;

        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public String getItem(int position) {
        return locations.get(position).toString();
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.timetable_textView_Room);
        strName.setText(getItem(position));
        return view;
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
            if (allLocations == null) {
                synchronized (lock) {
                    allLocations = new ArrayList<Location>(locations);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = allLocations;
                    results.count = allLocations.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<String> matchValues = new ArrayList<String>();

                for (int i = 0; i < locations.size(); i++) {
                    String room = locations.get(i).room;
                    if (room.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(room);
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
                locations = (ArrayList<Location>)results.values;
            } else {
                locations = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}