package teamg.hochschulestralsund;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import teamg.hochschulestralsund.ItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.LectureTime;

import java.util.ArrayList;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Lecture> lectures;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(ArrayList<Lecture> lectures, OnListFragmentInteractionListener listener) {
        this.lectures = lectures;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.lecture = lectures.get(position);
        holder.textView_time.setText(lectures.get(position).lectureTime.toString());
        holder.textView_title.setText(lectures.get(position).title);
        holder.textView_room.setText(lectures.get(position).location.toString());
        holder.textView_lecturer.setText(lectures.get(position).lecturer.toString());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.lecture);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textView_time;
        public final TextView textView_title;
        public final TextView textView_room;
        public final TextView textView_lecturer;
        public Lecture lecture;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            textView_time = view.findViewById(R.id.timetable_texView_Time);
            textView_title = view.findViewById(R.id.timetable_textView_title);
            textView_room = view.findViewById(R.id.timetable_textView_Room);
            textView_lecturer = view.findViewById(R.id.timetable_textView_Lecturer);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView_lecturer.getText() + "'";
        }
    }
}
