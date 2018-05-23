package teamg.hochschulestralsund;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import teamg.hochschulestralsund.connect.Parser;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.LectureTime;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    public static String CODE_SHOW_DAY = "CODE_SHOW_DAY";

    private Toolbar toolbar;
    private Calendar calendar;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ItemFragment fragment;

    private int currentDay = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        parse();
        showCurrentDay();
    }

    @Override
    public void onListFragmentInteraction(Lecture lecture) {

    }

    @Override
    /* create menubar */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timetable, menu);

        /* set the icon color for 3 menu icons */
        for (int i = 0; i < 3; i++) {
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
            /* show activity to add a new lecture */
            case R.id.action_add_lecture:
                Intent intent = new Intent(this, AddLectureActivity.class);
                startActivityForResult(intent, 0);

                return true;
            case R.id.action_add_alert:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* update the list with todos when going back to main */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showCurrentDay();
    }

    public void init() {
        /* add default sql data */
        CustomSQL customSQL = new CustomSQL(this);
        customSQL.addDefaultData();
        customSQL.close();

        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        fragment = new ItemFragment();
        manager = getFragmentManager();
    }

    /* parse the website for lecturers */
    public void parse() {
        Parser myParser = new Parser(this);
        myParser.execute();
    }

    public void showDay(int day, boolean init) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_SHOW_DAY, day);
        fragment = new ItemFragment();
        fragment.setArguments(bundle);

        transaction = manager.beginTransaction();

        if(init)
            transaction.add(R.id.timetable_container, fragment, null);
        else
            transaction.replace(R.id.timetable_container, fragment, null);

        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* show the fragment for the current day */
    void showCurrentDay() {
        this.findViewById(android.R.id.content).setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                showDay(getNextDay(), false);
            }
            public void onSwipeLeft() {
                showDay(getPreviosDay(), false);
            }
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        showDay(currentDay, true);
    }

    private int getNextDay() {
        currentDay = (currentDay + 1) % 8;

        /* if next day is saturday show monday */
        if(currentDay == 7)
            currentDay = 1;

        return currentDay;
    }

    private int getPreviosDay() {
        currentDay = (currentDay - 1) % 8;

        /* if next day is sunday show friday */
        if(currentDay == 1)
            currentDay = 6;

        return currentDay;
    }

    private void setConstraint() {

    }
}
