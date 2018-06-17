package teamg.hochschulestralsund;

import android.app.Fragment;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

import teamg.hochschulestralsund.adapter.AdapterLecturer;
import teamg.hochschulestralsund.adapter.AdapterRoom;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.LectureTime;
import teamg.hochschulestralsund.sql.Location;
import teamg.hochschulestralsund.sql.Person;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MeetingAddEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MeetingAddEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LectureAddEditFragment extends Fragment {
    public EditText editText_title;
    public AutoCompleteTextView editText_location;
    public AutoCompleteTextView editText_lecturer;
    public RadioGroup radioGroup;
    public Spinner spinner;
    public Spinner spinner_type;
    public Button button_lecture_submit;

    private int code = -1;
    private Lecture lecture = new Lecture();

    public LectureAddEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
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
            int code = getArguments().getInt(LectureActivity.CODE_LECTURE, LectureActivity.CODE_LECTURE_ADD);
            int itemCount = 0;

            switch (code) {
                case LectureActivity.CODE_LECTURE_ADD:
                    inflater.inflate(R.menu.lecture_add, menu);
                    itemCount = 2;

                    break;


                case LectureActivity.CODE_LECTURE_EDIT:
                    inflater.inflate(R.menu.lecture_edit, menu);
                    itemCount = 3;

                    break;
            }

            //* set the icon color for menu icons
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
            case R.id.action_save_edited_lecture:
                submit();

                return true;

            case R.id.action_delete_lecture:
                CustomSQL customSQL = new CustomSQL(getActivity());
                customSQL.deleteLecture(lecture);
                customSQL.close();

                goBack();

                return true;

            case R.id.action_abort_edit_lecture:
                goBack();

                return true;

            case R.id.action_save_new_lecture:
                submit();

                return true;

            case R.id.action_abort_add_lecture:
                goBack();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lecture_add, container, false);
    }

    /**
     * init the view elements
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        editText_title = getView().findViewById(R.id.editText_add_Lecture_title);
        editText_lecturer = getView().findViewById(R.id.editText_add_lecture_lecturer);
        editText_location = getView().findViewById(R.id.editText_add_lecture_location);
        radioGroup = getView().findViewById(R.id.radioGroup_add_lecture_day);
        spinner = getView().findViewById(R.id.spinner_add_lecture_time);
        spinner_type = getView().findViewById(R.id.spinner_add_lecture_type);
        button_lecture_submit = getView().findViewById(R.id.button_add_lecture_submit);

        setAdapter();
        parseBundle();
    }

    /**
     * set the adapters
     */
    public void setAdapter() {
        CustomSQL customSQL = new CustomSQL(getActivity());

        editText_location.setAdapter(new AdapterRoom(getActivity(),
                android.R.layout.simple_dropdown_item_1line, customSQL.getLocations()));

        editText_lecturer.setAdapter(new AdapterLecturer(getActivity(),
                android.R.layout.simple_dropdown_item_1line, customSQL.getLecturers()));

        final ArrayList<LectureTime> lectureTimes = customSQL.getLectureTimes();
        ArrayAdapter<LectureTime> adapterLectureTime = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, lectureTimes);
        spinner.setAdapter(adapterLectureTime);

        String[] types = getResources().getStringArray(R.array.lecture_type);
        ArrayAdapter<String> adapterLectureType = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, types);
        spinner_type.setAdapter(adapterLectureType);
        spinner_type.setSelection(0);

        /* override the click handler */
        editText_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lecture.event_title = editText_title.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lecture.event_location = (Location) parent.getItemAtPosition(position);
            }
        });

        editText_lecturer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lecture.event_person = (Person) parent.getItemAtPosition(position);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_add_lecture_mo:
                        lecture.event_begin.set(Calendar.DAY_OF_WEEK, 2);
                        break;
                    case R.id.radioButton_add_lecture_di:
                        lecture.event_begin.set(Calendar.DAY_OF_WEEK, 3);
                        break;
                    case R.id.radioButton_add_lecture_mi:
                        lecture.event_begin.set(Calendar.DAY_OF_WEEK, 4);
                        break;
                    case R.id.radioButton_add_lecture_do:
                        lecture.event_begin.set(Calendar.DAY_OF_WEEK, 5);
                        break;
                    case R.id.radioButton_add_lecture_fr:
                        lecture.event_begin.set(Calendar.DAY_OF_WEEK, 6);
                        break;
                    /* just in case */
                    default:
                        lecture.event_begin.set(Calendar.DAY_OF_WEEK, 2);
                        break;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lecture.lecture_time = (LectureTime) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lecture.lecture_type = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setDay();
        button_lecture_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });

        customSQL.close();
    }

    /**
     * determine if add or edit meeting
     */
    private void parseBundle() {
        if (getArguments() != null) {
            code = getArguments().getInt(LectureActivity.CODE_LECTURE, LectureActivity.CODE_LECTURE_ADD);

            switch (code) {
                case LectureActivity.CODE_LECTURE_ADD:
                    //* keep the views clear
                    break;
                case LectureActivity.CODE_LECTURE_EDIT:
                    lecture = getArguments().getParcelable(LectureActivity.CODE_LECTURE_PARCELABLE);

                    editText_title.setText(lecture.event_title);
                    editText_location.setText(lecture.event_location.toString());
                    editText_lecturer.setText(lecture.event_person.toString());

                    if (lecture.lecture_time.toString().isEmpty() == false) {
                        spinner.setSelection(getIndex(spinner, lecture.lecture_time.toString()));
                    }

                    if (lecture.lecture_type.isEmpty() == false) {
                        spinner_type.setSelection(getIndex(spinner_type, lecture.lecture_type));
                    }

                    setDay();
                    setButtonText();

                    break;

                default:

                    break;
            }
        }
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }

        return 0;
    }

    /**
     * sets the day on the radioGroup
     */
    public void setDay() {
        switch (lecture.event_begin.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                radioGroup.check(R.id.radioButton_add_lecture_mo);
                break;
            case 3:
                radioGroup.check(R.id.radioButton_add_lecture_di);
                break;
            case 4:
                radioGroup.check(R.id.radioButton_add_lecture_mi);
                break;
            case 5:
                radioGroup.check(R.id.radioButton_add_lecture_do);
                break;
            case 6:
                radioGroup.check(R.id.radioButton_add_lecture_fr);
                break;
            default:
                radioGroup.check(R.id.radioButton_add_lecture_mo);
                break;
        }
    }

    private void setButtonText() {
        switch (code) {
            case LectureActivity.CODE_LECTURE_EDIT:
                button_lecture_submit.setText(R.string.lecture_save_edited);
                break;

            default:
                button_lecture_submit.setText(R.string.lecture_save_new);

                break;
        }
    }

    public void submit() {
        CustomSQL customSQL = new CustomSQL(getActivity());

        switch (code) {
            case LectureActivity.CODE_LECTURE_ADD:
                customSQL.addLecture(lecture);

                break;

            case LectureActivity.CODE_LECTURE_EDIT:
                customSQL.deleteLecture(lecture);
                customSQL.addLecture(lecture);

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
        LectureActivity.showLectures(getFragmentManager(), false);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
