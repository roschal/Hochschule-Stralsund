package teamg.hochschulestralsund.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import teamg.hochschulestralsund.ExamActivity;
import teamg.hochschulestralsund.ExamItemFragment;
import teamg.hochschulestralsund.ExamItemFragment.OnListFragmentInteractionListener;
import teamg.hochschulestralsund.MainActivity;
import teamg.hochschulestralsund.R;
import teamg.hochschulestralsund.sql.Exam;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Meeting} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class ExamMyItemRecyclerViewAdapter extends RecyclerView.Adapter<ExamMyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Exam> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final ExamItemFragment itemFragment;

    public ExamMyItemRecyclerViewAdapter(ArrayList<Exam> items, OnListFragmentInteractionListener listener, ExamItemFragment itemFragment) {
        mValues = items;
        mListener = listener;
        this.itemFragment = itemFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.exam = mValues.get(position);
        holder.textView_exam_title.setText(mValues.get(position).exam_title);
        holder.textView_exam_person.setText(mValues.get(position).exam_person.toString());
        holder.textView_exam_location.setText(mValues.get(position).exam_location.toString());
        holder.textView_exam_date.setText(MainActivity.parseDate(mValues.get(position).exam_begin));
        holder.textView_exam_time.setText(MainActivity.parseTime(mValues.get(position).exam_begin));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ExamActivity.editExam(itemFragment.getFragmentManager(), holder.exam);
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
        public final TextView textView_exam_title;
        public final TextView textView_exam_person;
        public final TextView textView_exam_location;
        public final TextView textView_exam_date;
        public final TextView textView_exam_time;

        public Exam exam;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView_exam_title = view.findViewById(R.id.textView_mensa_ingredients);
            textView_exam_person = view.findViewById(R.id.textView_exam_person);
            textView_exam_location = view.findViewById(R.id.textView_lecture_location);
            textView_exam_date = view.findViewById(R.id.textView_mensa_price_guest);
            textView_exam_time = view.findViewById(R.id.textView_lecture_time);
        }
    }
}
