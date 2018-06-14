package teamg.hochschulestralsund;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Meeting;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeetingAddEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeetingAddEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingAddEditFragment extends Fragment {
    private EditText editText_meeting_title;
    private EditText editText_meeting_description;
    private DatePicker datePicker_meeting;
    private TimePicker timePicker_meeting;
    private Button button_meeting_submit;

    private OnFragmentInteractionListener mListener;
    private Meeting meeting = new Meeting();

    public MeetingAddEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meeting_add, container, false);
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
        editText_meeting_title = getView().findViewById(R.id.editText_exam_title);
        editText_meeting_description = getView().findViewById(R.id.editText_exam_location);
        timePicker_meeting = getView().findViewById(R.id.timePicker_exam);
        datePicker_meeting = getView().findViewById(R.id.datePicker_exam);
        button_meeting_submit = getView().findViewById(R.id.button_meeting_submit);

        setAdapter();
        parseBundle();
    }

    /**set the adapters
     *
     */
    public void setAdapter() {
        //*  override the click handler */
        editText_meeting_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                meeting.meeting_title = editText_meeting_title.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_meeting_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                meeting.meeting_description = editText_meeting_description.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_meeting_submit.setOnClickListener(new View.OnClickListener() {
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
            int code = getArguments().getInt(MeetingActivity.CODE_MEETING, MeetingActivity.CODE_MEETING_ADD);

            switch (code) {
                case MeetingActivity.CODE_MEETING_ADD:
                    //* keep the views clear
                    break;
                case MeetingActivity.CODE_MEETING_EDIT:
                    meeting = getArguments().getParcelable(MeetingActivity.CODE_MEETING_PARCELABLE);

                    editText_meeting_title.setText(meeting.meeting_title);
                    editText_meeting_description.setText(meeting.meeting_description);
                    datePicker_meeting.updateDate(meeting.meeting_calendar.get(Calendar.YEAR), meeting.meeting_calendar.get(Calendar.MONTH), meeting.meeting_calendar.get(Calendar.DAY_OF_MONTH));
                    timePicker_meeting.setCurrentHour(meeting.meeting_calendar.get(Calendar.HOUR));
                    timePicker_meeting.setCurrentMinute(meeting.meeting_calendar.get(Calendar.MINUTE));

                    break;
            }
        }
    }

    /**
     *
     * @return Calendar
     */
    private Calendar getDateAndTime(){
        int minute = timePicker_meeting.getCurrentMinute();
        int hour = timePicker_meeting.getCurrentHour();
        int day = datePicker_meeting.getDayOfMonth();
        int month = datePicker_meeting.getMonth();
        int year =  datePicker_meeting.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return calendar;
    }

    /**add or edit the meeting
     *
     */
    private void submit() {
        meeting.meeting_calendar = getDateAndTime();

        CustomSQL customSQL = new CustomSQL(getActivity());
        customSQL.addMeeting(meeting);
        customSQL.close();

        //* go back to all meetings
        MeetingActivity.showMeetings(getFragmentManager(), false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Meeting meeting);
    }
}
