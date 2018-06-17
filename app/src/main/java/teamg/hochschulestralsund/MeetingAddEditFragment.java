package teamg.hochschulestralsund;

import android.app.AlarmManager;
import android.app.Fragment;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import teamg.hochschulestralsund.helper.AlarmHelper;
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
    private Button button_meeting_setReminder;

    private OnFragmentInteractionListener mListener;
    private Meeting meeting = new Meeting();
    private int code = -1;

    private AlarmHelper alarmHelper;

    public MeetingAddEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    /**creates the menu
     *
     * @return boolean
     */
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getArguments() != null) {
            int code = getArguments().getInt(MeetingActivity.CODE_MEETING, MeetingActivity.CODE_MEETING_ADD);
            int itemCount = 0;

            switch (code) {
                case MeetingActivity.CODE_MEETING_ADD:
                    inflater.inflate(R.menu.meeting_add, menu);
                    itemCount = 2;

                    break;


                case MeetingActivity.CODE_MEETING_EDIT:
                    inflater.inflate(R.menu.meeting_edit, menu);
                    itemCount = 3;

                    break;
            }

            //* set the icon color for 2 menu icons
            for (int i = 0; i < itemCount; i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
            }
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    /**overrides the click handler on menu
     *
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /* show activity to add a new lecture */
            case R.id.action_save_edited_meeting:
                submit();

                return true;

            case R.id.action_delete_meeting:
                CustomSQL customSQL = new CustomSQL(getActivity());
                customSQL.deleteMeeting(meeting);
                customSQL.close();

                goBack();

                return true;

            case R.id.action_abort_edit_meeting:
                goBack();

                return true;

            case R.id.action_save_new_meeting:
                submit();

                return true;

            case R.id.action_abort_add_meeting:
                goBack();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meeting_add, container, false);
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * init the view elements
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
        button_meeting_setReminder = getView().findViewById(R.id.button_meeting_setReminder);

        alarmHelper = new AlarmHelper(getActivity(), (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE));

        setAdapter();
        parseBundle();
    }

    /**
     * set the adapters
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

        button_meeting_setReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar alarmDate = getDateAndTime();
                alarmHelper.createAlarm(alarmDate, meeting.meeting_title);
            }
        });

    }

    /**
     * determine if add or edit meeting
     */
    private void parseBundle() {
        if (getArguments() != null) {
            code = getArguments().getInt(MeetingActivity.CODE_MEETING, MeetingActivity.CODE_MEETING_ADD);

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

                    setButtonText();

                    break;
            }
        }
    }

    /**
     * @return Calendar
     */
    private Calendar getDateAndTime() {
        int minute = timePicker_meeting.getCurrentMinute();
        int hour = timePicker_meeting.getCurrentHour();
        int day = datePicker_meeting.getDayOfMonth();
        int month = datePicker_meeting.getMonth();
        int year = datePicker_meeting.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return calendar;
    }

    private void setButtonText() {
        switch (code) {
            case LectureActivity.CODE_LECTURE_EDIT:
                button_meeting_submit.setText(R.string.lecture_save_edited);
                break;

            default:
                button_meeting_submit.setText(R.string.lecture_save_new);

                break;
        }
    }

    /**
     * add or edit the meeting
     */
    private void submit() {
        meeting.meeting_calendar = getDateAndTime();
        CustomSQL customSQL = new CustomSQL(getActivity());

        switch (code) {
            case MeetingActivity.CODE_MEETING_ADD:
                customSQL.addMeeting(meeting);

                break;

            case MeetingActivity.CODE_MEETING_EDIT:
                customSQL.deleteMeeting(meeting);
                customSQL.addMeeting(meeting);

                break;

            default:

                break;
        }

        customSQL.close();
        goBack();
    }

    /**
     * go back to activity
     */
    private void goBack() {
        MeetingActivity.showMeetings(getFragmentManager(), false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Meeting meeting);
    }
}
