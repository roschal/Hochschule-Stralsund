package teamg.hochschulestralsund;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Meeting;

public class MeetingActivity extends AppCompatActivity implements MeetingItemFragment.OnListFragmentInteractionListener {
    public static final String CODE_MEETING = "CODE_MEETING";
    public static final int CODE_MEETING_ADD = 0;
    public static final int CODE_MEETING_EDIT = 1;
    public static final int CODE_MEETING_DELETE = 2;
    public static final int CODE_MEETING_SHOW_ALL = 3;
    public static final String CODE_MEETING_PARCELABLE = "CODE_MEETING_PARCELABLE";

    /**
     * show all Meetings
     */
    public static void showMeetings(FragmentManager manager, boolean firstTime) {
        MeetingItemFragment meetingItemFragment = new MeetingItemFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (firstTime)
            transaction.add(R.id.meeting_container, meetingItemFragment, null);
        else
            transaction.replace(R.id.meeting_container, meetingItemFragment, null);

        transaction.commit();
    }

    public static void addMeeting(FragmentManager manager) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_MEETING, CODE_MEETING_ADD);
        MeetingAddEditFragment fragment = new MeetingAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        //* add to the back stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.meeting_container, fragment, null);
        transaction.commit();
    }

    public static void editMeeting(FragmentManager manager, Meeting meeting) {
        Bundle bundle = new Bundle();
        bundle.putInt(CODE_MEETING, CODE_MEETING_EDIT);
        bundle.putParcelable(CODE_MEETING_PARCELABLE, meeting);

        MeetingAddEditFragment fragment = new MeetingAddEditFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction;
        transaction = manager.beginTransaction();
        //* add to the back stack
        transaction.addToBackStack(null);
        transaction.replace(R.id.meeting_container, fragment, null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        parseBundle();
    }

    @Override
    public void onListFragmentInteraction(Meeting meeting) {
        editMeeting(getFragmentManager(), meeting);
    }

    private void parseBundle() {
        if (getIntent().hasExtra(CODE_MEETING)) {
            int code = getIntent().getIntExtra(CODE_MEETING, CODE_MEETING_SHOW_ALL);

            switch (code) {
                case CODE_MEETING_SHOW_ALL:
                    showMeetings(getFragmentManager(), true);
                    break;
            }

        }
    }

    private void deleteMeeting() {

    }
}
