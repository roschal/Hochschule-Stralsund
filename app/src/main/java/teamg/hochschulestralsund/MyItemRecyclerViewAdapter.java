package teamg.hochschulestralsund;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import teamg.hochschulestralsund.ItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.CustomSQL;

import java.util.ArrayList;

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

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do delete Stuff
                holder.customSQL.deleteLecture(holder.lecture);
            }
        });

        holder.button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do edit stuff
            }
        });

        holder.view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(holder.button_edit.getVisibility() == View.GONE
                            && holder.button_delete.getVisibility() == View.GONE){
                        holder.button_delete.setVisibility(View.VISIBLE);
                        holder.button_edit.setVisibility(View.VISIBLE);
                    } else {
                        holder.button_delete.setVisibility(View.GONE);
                        holder.button_edit.setVisibility(View.GONE);
                    }

                }
                return true;
            }
        });

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
        public final Button button_delete;
        public final Button button_edit;
        public Lecture lecture;
        public CustomSQL customSQL;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            textView_time = view.findViewById(R.id.timetable_texView_Time);
            textView_title = view.findViewById(R.id.timetable_textView_title);
            textView_room = view.findViewById(R.id.timetable_textView_Room);
            textView_lecturer = view.findViewById(R.id.timetable_textView_Lecturer);
            button_delete = view.findViewById(R.id.timetable_button_delete);
            button_edit = view.findViewById(R.id.timetable_button_edit);
            customSQL = new CustomSQL(view.getContext());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView_lecturer.getText() + "'";
        }
    }
}
