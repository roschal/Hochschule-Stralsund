package teamg.hochschulestralsund;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Exam;

public class ExamActivity extends AppCompatActivity implements ExamItemFragment.OnListFragmentInteractionListener{
    public static final String CODE_EXAM = "CODE_EXAM";
    public static final int CODE_EXAM_ADD = 0;
    public static final int CODE_EXAM_EDIT = 1;
    public static final int CODE_EXAM_DELETE = 2;
    public static final int CODE_EXAM_SHOW_ALL = 3;
    public static final String CODE_EXAM_PARCELABLE = "CODE_EXAM_PARCELABLE";

    private ExamItemFragment itemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        parseBundle();
    }

    @Override
    /**create the menu
     *
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exam, menu);

        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);

        /* set the icon color for 3 menu icons */
        for (int i = 0; i < 3; i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
        }

        return true;
    }

    @Override
    /**override click handler on menu
     *
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /* show activity to add a new lecture */
            case R.id.action_add_exam:
                addExam(getFragmentManager());

                return true;
            case R.id.action_edit_exam:
                editExam(null, null);

                return true;
            case R.id.action_delete_exam:
                deleteExam();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListFragmentInteraction(Exam exam) {
        Log.e("hi", "hi");
        editExam(getFragmentManager(), exam);
    }

    private void parseBundle() {

        if(getIntent().hasExtra(CODE_EXAM)) {
            int code = getIntent().getIntExtra(CODE_EXAM, CODE_EXAM_SHOW_ALL);

            switch (code) {
                case CODE_EXAM_SHOW_ALL:
                    showExams(getFragmentManager(), true);
                    break;
            }

        }
    }

    /**show all Exams
     *
     */
    protected static void showExams(FragmentManager manager, boolean firstTime) {
        ExamItemFragment examItemFragment = new ExamItemFragment();
        FragmentTransaction transaction;

        transaction = manager.beginTransaction();

        if (firstTime)
            transaction.add(R.id.exam_container, examItemFragment, null);
        else
            transaction.replace(R.id.exam_container, examItemFragment, null);

        transaction.commit();
    }

    protected static void addExam(FragmentManager manager) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_EXAM, CODE_EXAM_ADD);
        ExamAddEditFragment fragment = new ExamAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        transaction.replace(R.id.exam_container, fragment, null);
        transaction.commit();
    }

    private static void editExam(FragmentManager manager, Exam exam) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_EXAM, CODE_EXAM_EDIT);
        bundle.putParcelable(CODE_EXAM_PARCELABLE, exam);

        ExamAddEditFragment fragment = new ExamAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        transaction.replace(R.id.exam_container, fragment, null);
        transaction.commit();
    }

    private void deleteExam() {

    }
}
