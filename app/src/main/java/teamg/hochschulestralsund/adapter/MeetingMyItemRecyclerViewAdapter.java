package teamg.hochschulestralsund.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import teamg.hochschulestralsund.MainActivity;
import teamg.hochschulestralsund.MeetingItemFragment;
import teamg.hochschulestralsund.MeetingItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.R;
import teamg.hochschulestralsund.sql.Meeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MeetingMyItemRecyclerViewAdapter extends RecyclerView.Adapter<MeetingMyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Meeting> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final MeetingItemFragment itemFragment;

    public MeetingMyItemRecyclerViewAdapter(ArrayList<Meeting> items, OnListFragmentInteractionListener listener, MeetingItemFragment itemFragment) {
        mValues = items;
        mListener = listener;
        this.itemFragment = itemFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meeting_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.meeting = mValues.get(position);
        holder.textView_meeting_title.setText(mValues.get(position).meeting_title);
        holder.textView_meeting_description.setText(mValues.get(position).meeting_description);
        holder.textView_meeting_date.setText(MainActivity.parseDate(mValues.get(position).meeting_calendar));
        holder.textView_meeting_time.setText(MainActivity.parseTime(mValues.get(position).meeting_calendar));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hi", "hi");
                if (null != mListener) {
                    Log.e("hi", "hi");
                    mListener.onListFragmentInteraction(holder.meeting);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_meeting_title;
        public final TextView textView_meeting_description;
        public final TextView textView_meeting_date;
        public final TextView textView_meeting_time;

        public Meeting meeting;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView_meeting_title = view.findViewById(R.id.textView_meeting_title);
            textView_meeting_description = view.findViewById(R.id.textView_meeting_description);
            textView_meeting_date = view.findViewById(R.id.textView_meeting_date);
            textView_meeting_time = view.findViewById(R.id.textView_meeting_time);
        }
    }
}
