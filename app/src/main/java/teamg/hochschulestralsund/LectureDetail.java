package teamg.hochschulestralsund;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;

import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;

public class LectureDetail extends AppCompatActivity {

    private Lecture lecture;
    private CustomSQL customSQL;
    private ArrayList<Lecture> lectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        /* get the id of the lecture to show */
        if (getIntent().hasExtra("ID")) {
            long id = getIntent().getLongExtra("ID", -1);

            lectures = customSQL.getLectures();

            for (int i = 0; i < lectures.size(); i++) {
                if(lectures.get(i).id == id) {
                    lecture = lectures.get(i);

                    break;
                }
            }
        }
        else {
            finish();
        }

        setContentView(R.layout.activity_lecture_detail);
    }

    @Override
    /* create menubar */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lecture, menu);

        /* set the icon color for 3 menu icons */
        for (int i = 0; i < 2; i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
        }
        return true;
    }

    @Override
    /* override click handler on menu */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /* show activity to add a new lecture */
            case R.id.action_edit_lecture:
                Intent intent = new Intent(this, AddLectureActivity.class);
                startActivityForResult(intent, 0);

                return true;
            case R.id.action_delete_lecture:
                customSQL.deleteLecture(lecture);
                customSQL.close();

                setResult(0);
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        lecture = new Lecture();
        lectures =  new ArrayList<>();
        customSQL = new CustomSQL(this);
    }
}