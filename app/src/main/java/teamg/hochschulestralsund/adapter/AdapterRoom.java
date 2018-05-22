package teamg.hochschulestralsund.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teamg.hochschulestralsund.R;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Location;

public class AdapterRoom extends ArrayAdapter<Location> {

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
    public Location getItem(int position) {
        Location location = locations.get(position);
        return location;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(itemLayout, parent, false);
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

                ArrayList<Location> matchValues = new ArrayList<Location>();

                for (int i = 0; i < locations.size(); i++) {
                    Location room = locations.get(i);
                    if (room.room.toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(room);
                    }
                    else if (room.toString().toLowerCase().startsWith(searchStrLowerCase)) {
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