package teamg.hochschulestralsund;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import teamg.hochschulestralsund.adapter.OnSwipeTouchListener;
import teamg.hochschulestralsund.connect.Parser;
import teamg.hochschulestralsund.helper.AlarmHelper;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;

public class MainActivity extends AppCompatActivity implements MainItemFragment.OnListFragmentInteractionListener {

    public static String CODE_SHOW_DAY = "CODE_SHOW_DAY";

    private Calendar calendar;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Intent intent;
    private Bundle bundle;


    public void setLanguage() {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String language = SP.getString("pref_language", "Deutsch");

        CustomLocale.setNewLocale(this, language);
   }

    public static Calendar getNextDay(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTime(calendar.getTime());
        c.add(Calendar.DATE, 1);

        return c;
    }

    public static Calendar getPreviosDay(Calendar calendar) {
        Calendar c = Calendar.getInstance();
        c.setTime(calendar.getTime());
        c.add(Calendar.DATE, -1);

        return c;
    }

    public static String parseDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        return simpleDateFormat.format(calendar.getTime());

    }

    public static String parseTime(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        return simpleDateFormat.format(calendar.getTime());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //* first set the language
        setLanguage();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setAdapter();
        parse();
        showCurrentDay(getFragmentManager(), true);
    }

    @Override
    public void onListFragmentInteraction(Lecture lecture) {

    }

    @Override
    /* create menubar */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        /* set the icon color for 4 menu icons */
        for (int i = 0; i < 4; i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
        }
        return true;
    }

    @Override
    /* override click handler on menu */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_show_contacts:
                intent = new Intent(this, ContactActivity.class);
                bundle = new Bundle();
                bundle.putInt(ContactActivity.CODE_CONTACT, ContactActivity.CODE_CONTACT_SHOW_ALL);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);

                return true;

            case R.id.action_show_mensa:
                intent = new Intent(this, MensaActivity.class);
                startActivityForResult(intent, 0);

                return true;

            case R.id.action_lectures:
                intent = new Intent(this, LectureActivity.class);
                bundle = new Bundle();
                bundle.putInt(LectureActivity.CODE_LECTURE, LectureActivity.CODE_LECTURE_SHOW_ALL);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);

                return true;
            case R.id.action_exams:
                intent = new Intent(this, ExamActivity.class);
                bundle = new Bundle();
                bundle.putInt(ExamActivity.CODE_EXAM, ExamActivity.CODE_EXAM_SHOW_ALL);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);

                return true;

            case R.id.action_meetings:
                intent = new Intent(this, MeetingActivity.class);
                bundle = new Bundle();
                bundle.putInt(MeetingActivity.CODE_MEETING, MeetingActivity.CODE_MEETING_SHOW_ALL);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);

                return true;

            case R.id.action_show_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 0);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* update the list with todos when going back to main */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showCurrentDay(getFragmentManager(), false);
        setLanguage();
    }

    public void init() {
        /* add default sql data */
        CustomSQL customSQL = new CustomSQL(this);
        customSQL.addDefaultData();
        customSQL.close();

        calendar = Calendar.getInstance();
        manager = getFragmentManager();
    }

    /**enable left right swipe in MainActivity
     *
     */
    public void setAdapter() {
        this.findViewById(android.R.id.content).setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                showPreviosDay(getFragmentManager());
            }

            public void onSwipeLeft() {
                showNextDay(getFragmentManager());
            }

            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    /* parse the website for lecturers */
    public void parse() {
        Parser myParser = new Parser(this);
        myParser.execute();
    }

    public void showDay(FragmentManager manager, String direction, Boolean firstTime) {
        Bundle bundle = new Bundle();
        bundle.putLong(CODE_SHOW_DAY, calendar.getTimeInMillis());
        MainItemFragment fragment = new MainItemFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = manager.beginTransaction();

        switch (direction) {
            case "left":
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.timetable_container, fragment, null);
                break;
            case "right":
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.timetable_container, fragment, null);
                break;
            default:
                if (firstTime)
                    transaction.add(R.id.timetable_container, fragment, null);
                else
                    transaction.replace(R.id.timetable_container, fragment, null);
                break;
        }

        transaction.commit();
    }

    /* show the fragment for the current day */
    public  void showCurrentDay(FragmentManager fragmentManager, Boolean firstTime) {
        calendar = Calendar.getInstance();
        showDay(fragmentManager, "none", firstTime);
    }

    public void showPreviosDay(FragmentManager fragmentManager) {
        calendar = getPreviosDay(calendar);
        showDay(fragmentManager,"left", false);
    }

    public void showNextDay(FragmentManager fragmentManager) {
        calendar = getNextDay(calendar);
        showDay(fragmentManager, "right", false);
    }
}
