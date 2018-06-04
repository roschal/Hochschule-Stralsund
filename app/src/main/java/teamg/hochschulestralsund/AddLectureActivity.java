package teamg.hochschulestralsund;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import teamg.hochschulestralsund.sql.Lecturer;
import teamg.hochschulestralsund.sql.Location;

public class AddLectureActivity extends AppCompatActivity {
    public EditText editText_title;
    public AutoCompleteTextView editText_location;
    public AutoCompleteTextView editText_lecturer;
    public RadioGroup radioGroup;
    public Spinner spinner;

    public CustomSQL customSQL;
    public Lecture lecture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lecture);

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

        customSQL = new CustomSQL(this);
        lecture = new Lecture();
    }

    /* set the current day on the radiogroup */
    public void setDay() {
        Calendar calendar = Calendar.getInstance();
        int DAY_OF_WEEK = calendar.get(Calendar.DAY_OF_WEEK);
        lecture.DAY_OF_WEEK = DAY_OF_WEEK;

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
        editText_location.setAdapter(new AdapterRoom(this,
                android.R.layout.simple_dropdown_item_1line, customSQL.getLocations()));

        editText_lecturer.setAdapter(new AdapterLecturer(this,
                android.R.layout.simple_dropdown_item_1line, customSQL.getLecturers()));

        final ArrayList<LectureTime> lectureTimes = customSQL.getLectureTimes();
        ArrayAdapter<LectureTime> adapterLectureTime = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lectureTimes);
        spinner.setAdapter(adapterLectureTime);

        /* override the click handler */
        editText_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lecture.title = editText_title.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lecture.location = (Location) parent.getItemAtPosition(position);
            }
        });

        editText_lecturer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lecture.lecturer = (Lecturer) parent.getItemAtPosition(position);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_add_lecture_mo:
                        lecture.DAY_OF_WEEK = 2;
                        break;
                    case R.id.radioButton_add_lecture_di:
                        lecture.DAY_OF_WEEK = 3;
                        break;
                    case R.id.radioButton_add_lecture_mi:
                        lecture.DAY_OF_WEEK = 4;
                        break;
                    case R.id.radioButton_add_lecture_do:
                        lecture.DAY_OF_WEEK = 5;
                        break;
                    case R.id.radioButton_add_lecture_fr:
                        lecture.DAY_OF_WEEK = 6;
                        break;
                    /* just in case */
                    default:
                        lecture.DAY_OF_WEEK = 2;
                        break;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lecture.lectureTime = (LectureTime) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void submit(View view) {
        customSQL.addLecture(lecture);
        finish();
    }
}
