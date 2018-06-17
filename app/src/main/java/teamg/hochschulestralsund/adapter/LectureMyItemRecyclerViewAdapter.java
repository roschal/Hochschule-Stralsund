package teamg.hochschulestralsund.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teamg.hochschulestralsund.LectureActivity;
import teamg.hochschulestralsund.LectureItemFragment;
import teamg.hochschulestralsund.LectureItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.R;
import teamg.hochschulestralsund.sql.Lecture;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class LectureMyItemRecyclerViewAdapter extends RecyclerView.Adapter<LectureMyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Lecture> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final LectureItemFragment itemFragment;

    public LectureMyItemRecyclerViewAdapter(ArrayList<Lecture> items, OnListFragmentInteractionListener listener, LectureItemFragment itemFragment) {
        mValues = items;
        mListener = listener;
        this.itemFragment = itemFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lecture_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.lecture = mValues.get(position);
        holder.textView_lecture_title.setText(holder.lecture.event_title + " - " + holder.lecture.lecture_type);
        holder.textView_lecture_location.setText(holder.lecture.event_location.toString());
        holder.textView_lecture_person.setText(holder.lecture.event_person.toString());
        holder.textView_lecture_time.setText(holder.lecture.lecture_time.toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LectureActivity.editLecture(itemFragment.getFragmentManager(), holder.lecture);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_lecture_title;
        public final TextView textView_lecture_location;
        public final TextView textView_lecture_person;
        public final TextView textView_lecture_time;

        public Lecture lecture;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView_lecture_title = view.findViewById(R.id.textView_lecture_title);
            textView_lecture_location = view.findViewById(R.id.textView_lecture_location);
            textView_lecture_person = view.findViewById(R.id.textView_lecture_person);
            textView_lecture_time = view.findViewById(R.id.textView_lecture_time);
        }
    }
}
