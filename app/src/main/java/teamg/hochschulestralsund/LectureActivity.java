package teamg.hochschulestralsund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import teamg.hochschulestralsund.sql.Person;
import teamg.hochschulestralsund.sql.Location;

public class LectureActivity extends AppCompatActivity {
    public EditText editText_title;
    public AutoCompleteTextView editText_location;
    public AutoCompleteTextView editText_lecturer;
    public RadioGroup radioGroup;
    public Spinner spinner;
    public Spinner spinner_type;

    public CustomSQL customSQL;
    public Lecture lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        init();
        setDay();
        setAdapter();
    }

    /* init the view */
    public void init()
    {
        editText_title = findViewById(R.id.editText_add_Lecture_title);
        editText_location = findViewById(R.id.editText_add_lecture_location);
        editText_lecturer = findViewById(R.id.editText_add_lecture_lecturer);
        radioGroup = findViewById(R.id.radioGroup_add_lecture_day);
        spinner = findViewById(R.id.spinner_add_lecture_time);
        spinner_type = findViewById(R.id.spinner_add_lecture_type);

        lecture = new Lecture();
    }

    /* set the current day on the radiogroup */
    public void setDay() {
        Calendar calendar = Calendar.getInstance();
        int DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK);

        lecture.event_begin = Calendar.getInstance();
        lecture.event_begin.setTime(calendar.getTime());

        switch (DAY_OF_WEEK) {
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

    /* set the adapter */
    public void setAdapter() {
        customSQL = new CustomSQL(this);

        editText_location.setAdapter(new AdapterRoom(this,
                android.R.layout.simple_dropdown_item_1line, customSQL.getLocations()));

        editText_lecturer.setAdapter(new AdapterLecturer(this,
                android.R.layout.simple_dropdown_item_1line, customSQL.getLecturers()));

        final ArrayList<LectureTime> lectureTimes = customSQL.getLectureTimes();
        ArrayAdapter<LectureTime> adapterLectureTime = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lectureTimes);
        spinner.setAdapter(adapterLectureTime);

        String[] types = getResources().getStringArray(R.array.lecture_type);
        ArrayAdapter<String> adapterLectureType = new ArrayAdapter<>(this,
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

    customSQL.close();
    }

    public void submit(View view) {
        customSQL = new CustomSQL(this);
        customSQL.addLecture(lecture);
        customSQL.close();

        setResult(0);
        finish();
    }
}
