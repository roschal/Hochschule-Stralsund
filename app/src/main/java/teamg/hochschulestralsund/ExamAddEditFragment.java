package teamg.hochschulestralsund;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import teamg.hochschulestralsund.adapter.AdapterLecturer;
import teamg.hochschulestralsund.adapter.AdapterRoom;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Exam;
import teamg.hochschulestralsund.sql.Location;
import teamg.hochschulestralsund.sql.Meeting;
import teamg.hochschulestralsund.sql.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeetingAddEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeetingAddEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamAddEditFragment extends Fragment {
    private EditText editText_exam_title;
    private AutoCompleteTextView editText_exam_person;
    private AutoCompleteTextView editText_exam_location;
    private DatePicker datePicker_exam;
    private TimePicker timePicker_exam;
    private Button button_exam_submit;

    private OnFragmentInteractionListener mListener;
    private Exam exam = new Exam();

    public ExamAddEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    /**creates the menu
     *
     * @return boolean
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getArguments() != null) {
            int code = getArguments().getInt(ExamActivity.CODE_EXAM, ExamActivity.CODE_EXAM_ADD);

            switch (code) {
                case ExamActivity.CODE_EXAM_ADD:
                    inflater.inflate(R.menu.exam_add, menu);

                    break;


                case ExamActivity.CODE_EXAM_EDIT:
                    inflater.inflate(R.menu.exam_edit, menu);

                    break;
            }

            //* set the icon color for 2 menu icons
            for (int i = 0; i < 2; i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
            }
        }
    }

    @Override
    /**overrides the click handler on menu
     *
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /* show activity to add a new lecture */
            case R.id.action_save_edited_exam:
                submit();

                return true;

            case R.id.action_delete_exam:
                CustomSQL customSQL = new CustomSQL(getActivity());

                // TODO
                // customSQL.deleteExam(Exam exam);
                customSQL.close();
                goBack();

                return true;

            case R.id.action_abort_edit_exam:
                goBack();

                return true;

            case R.id.action_save_new_exam:
                submit();

                return true;

            case R.id.action_abort_add_exam:
                goBack();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam_add, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**init the view elements
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        editText_exam_title = getView().findViewById(R.id.editText_exam_title);
        editText_exam_person = getView().findViewById(R.id.editText_exam_person);
        editText_exam_location = getView().findViewById(R.id.editText_exam_location);
        timePicker_exam = getView().findViewById(R.id.timePicker_exam);
        datePicker_exam = getView().findViewById(R.id.datePicker_exam);
        button_exam_submit = getView().findViewById(R.id.button_exam_submit);

        setAdapter();
        parseBundle();
    }

    /**set the adapters
     *
     */
    public void setAdapter() {
        //*  override the click handler */
        editText_exam_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                exam.exam_title = editText_exam_title.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        CustomSQL customSQL = new CustomSQL(getActivity());

        editText_exam_person.setAdapter(new AdapterLecturer(getActivity(),
                android.R.layout.simple_dropdown_item_1line, customSQL.getLecturers()));
        editText_exam_location.setAdapter(new AdapterRoom(getActivity(),
                android.R.layout.simple_dropdown_item_1line, customSQL.getLocations()));

        editText_exam_person.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exam.exam_person = (Person) parent.getItemAtPosition(position);
            }
        });

        editText_exam_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exam.exam_location = (Location) parent.getItemAtPosition(position);
            }
        });

        customSQL.close();

        button_exam_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

    }

    /**determine if add or edit meeting
     *
     */
    private void parseBundle() {
        if (getArguments() != null) {
            int code = getArguments().getInt(ExamActivity.CODE_EXAM, ExamActivity.CODE_EXAM_ADD);

            switch (code) {
                case ExamActivity.CODE_EXAM_ADD:
                    //* keep the views clear
                    break;
                case ExamActivity.CODE_EXAM_EDIT:
                    exam = getArguments().getParcelable(ExamActivity.CODE_EXAM_PARCELABLE);

                    editText_exam_title.setText(exam.exam_title);
                    editText_exam_person.setText(exam.exam_person.toString());
                    editText_exam_location.setText(exam.exam_location.toString());
                    datePicker_exam.updateDate(exam.exam_begin.get(Calendar.YEAR), exam.exam_begin.get(Calendar.MONTH), exam.exam_begin.get(Calendar.DAY_OF_MONTH));
                    timePicker_exam.setCurrentHour(exam.exam_begin.get(Calendar.HOUR));
                    timePicker_exam.setCurrentMinute(exam.exam_begin.get(Calendar.MINUTE));

                    break;
            }
        }
    }

    /**
     *
     * @return Calendar
     */
    private Calendar getDateAndTime(){
        int minute = timePicker_exam.getCurrentMinute();
        int hour = timePicker_exam.getCurrentHour();
        int day = datePicker_exam.getDayOfMonth();
        int month = datePicker_exam.getMonth();
        int year =  datePicker_exam.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return calendar;
    }

    /**add or edit the meeting
     *
     */
    private void submit() {
        exam.exam_begin = getDateAndTime();

        CustomSQL customSQL = new CustomSQL(getActivity());
        customSQL.addExam(exam);
        customSQL.close();

        goBack();
    }

    /**go back to the list with exams
     *
     */
    private void goBack() {
        ExamActivity.showExams(getFragmentManager(), false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
