package teamg.hochschulestralsund;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import teamg.hochschulestralsund.sql.Exam;

public class ExamActivity extends AppCompatActivity implements ExamItemFragment.OnListFragmentInteractionListener {
    public static final String CODE_EXAM = "CODE_EXAM";
    public static final int CODE_EXAM_ADD = 0;
    public static final int CODE_EXAM_EDIT = 1;
    public static final int CODE_EXAM_DELETE = 2;
    public static final int CODE_EXAM_SHOW_ALL = 3;
    public static final String CODE_EXAM_PARCELABLE = "CODE_EXAM_PARCELABLE";

    /**
     * show all Exams
     */
    public static void showExams(FragmentManager manager, boolean firstTime) {
        ExamItemFragment examItemFragment = new ExamItemFragment();
        FragmentTransaction transaction;

        transaction = manager.beginTransaction();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (firstTime)
            transaction.add(R.id.exam_container, examItemFragment, null);
        else
            transaction.replace(R.id.exam_container, examItemFragment, null);

        transaction.commit();
    }

    public static void addExam(FragmentManager manager) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_EXAM, CODE_EXAM_ADD);
        ExamAddEditFragment fragment = new ExamAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        //* add to the back stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.exam_container, fragment, null);
        transaction.commit();
    }

    public static void editExam(FragmentManager manager, Exam exam) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_EXAM, CODE_EXAM_EDIT);
        bundle.putParcelable(CODE_EXAM_PARCELABLE, exam);

        ExamAddEditFragment fragment = new ExamAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        //* add to the back stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.exam_container, fragment, null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        parseBundle();
    }

    @Override
    public void onListFragmentInteraction(Exam exam) {
        editExam(getFragmentManager(), exam);
    }

    private void parseBundle() {

        if (getIntent().hasExtra(CODE_EXAM)) {
            int code = getIntent().getIntExtra(CODE_EXAM, CODE_EXAM_SHOW_ALL);

            switch (code) {
                case CODE_EXAM_SHOW_ALL:
                    showExams(getFragmentManager(), true);
                    break;
            }

        }
    }
}
