package teamg.hochschulestralsund;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;

public class LectureActivity extends AppCompatActivity implements LectureItemFragment.OnListFragmentInteractionListener {
    public static final String CODE_LECTURE = "CODE_LECTURE";
    public static final int CODE_LECTURE_ADD = 0;
    public static final int CODE_LECTURE_EDIT = 1;
    public static final int CODE_LECTURE_DELETE = 2;
    public static final int CODE_LECTURE_SHOW_ALL = 3;
    public static final String CODE_LECTURE_PARCELABLE = "CODE_LECTURE_PARCELABLE";


    private LectureItemFragment itemFragment;

    /**
     * show all Lectures
     */
    public static void showLectures(FragmentManager manager, boolean firstTime) {
        LectureItemFragment lectureItemFragment = new LectureItemFragment();
        FragmentTransaction transaction;

        transaction = manager.beginTransaction();

        if (firstTime)
            transaction.add(R.id.lecture_container, lectureItemFragment, null);
        else
            transaction.replace(R.id.lecture_container, lectureItemFragment, null);

        transaction.commit();
    }

    public static void addLecture(FragmentManager manager) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_LECTURE, CODE_LECTURE_ADD);
        LectureAddEditFragment fragment = new LectureAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        transaction.replace(R.id.lecture_container, fragment, null);
        transaction.commit();
    }

    public static void editLecture(FragmentManager manager, Lecture lecture) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_LECTURE, CODE_LECTURE_EDIT);
        bundle.putParcelable(CODE_LECTURE_PARCELABLE, lecture);

        LectureAddEditFragment fragment = new LectureAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        transaction.replace(R.id.lecture_container, fragment, null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        parseBundle();
    }

    @Override
    public void onListFragmentInteraction(Lecture lecture) {
        editLecture(getFragmentManager(), lecture);
    }

    private void parseBundle() {

        if (getIntent().hasExtra(CODE_LECTURE)) {
            int code = getIntent().getIntExtra(CODE_LECTURE, CODE_LECTURE_SHOW_ALL);

            switch (code) {
                case CODE_LECTURE_SHOW_ALL:
                    showLectures(getFragmentManager(), true);
                    break;
            }

        }
    }

    private void deleteLecture() {

    }
}
