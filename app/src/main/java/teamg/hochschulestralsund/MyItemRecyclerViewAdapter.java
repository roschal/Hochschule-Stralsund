package teamg.hochschulestralsund;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import teamg.hochschulestralsund.ItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Lecture> lectures;
    private final OnListFragmentInteractionListener mListener;
    private final ItemFragment itemFragment;

    public MyItemRecyclerViewAdapter(ArrayList<Lecture> lectures, OnListFragmentInteractionListener listener, ItemFragment itemFragment) {
        this.lectures = lectures;
        mListener = listener;
        this.itemFragment = itemFragment;
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

        /* click on item opens a new activity */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemFragment.getActivity().getApplicationContext(), LectureDetail.class);
                intent.putExtra("ID", holder.lecture.id);

                itemFragment.startActivityForResult(intent, 0);
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
        public CustomSQL customSQL;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            textView_time = view.findViewById(R.id.timetable_texView_Time);
            textView_title = view.findViewById(R.id.timetable_textView_title);
            textView_room = view.findViewById(R.id.timetable_textView_Room);
            textView_lecturer = view.findViewById(R.id.timetable_textView_Lecturer);
            customSQL = new CustomSQL(view.getContext());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView_lecturer.getText() + "'";
        }
    }
}
