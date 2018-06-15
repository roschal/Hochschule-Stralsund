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

import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.Meeting;

public class MeetingActivity extends AppCompatActivity implements MeetingItemFragment.OnListFragmentInteractionListener{
    public static final String CODE_MEETING = "CODE_MEETING";
    public static final int CODE_MEETING_ADD = 0;
    public static final int CODE_MEETING_EDIT = 1;
    public static final int CODE_MEETING_DELETE = 2;
    public static final int CODE_MEETING_SHOW_ALL = 3;
    public static final String CODE_MEETING_PARCELABLE = "CODE_MEETING_PARCELABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        parseBundle();
    }

    @Override
    /**create the menu
     *
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meeting, menu);

        //* set the icon color for 2 menu icons
        for (int i = 0; i < 2; i++) {
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
            case R.id.action_add_meeting:
                addMeeting(getFragmentManager());

                return true;

            case R.id.action_delete_meetings:
                CustomSQL customSQL = new CustomSQL(this);
                customSQL.deleteMeetings();
                customSQL.close();

                showMeetings(getFragmentManager(), false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListFragmentInteraction(Meeting meeting) {
        editMeeting(getFragmentManager(), meeting);
    }

    private void parseBundle() {
        if(getIntent().hasExtra(CODE_MEETING)) {
            int code = getIntent().getIntExtra(CODE_MEETING, CODE_MEETING_SHOW_ALL);

            switch (code) {
                case CODE_MEETING_SHOW_ALL:
                    showMeetings(getFragmentManager(), true);
                    break;
            }

        }
    }

    /**show all Meetings
     *
     */
    public static void showMeetings(FragmentManager manager, boolean firstTime) {
        MeetingItemFragment meetingItemFragment = new MeetingItemFragment();
        FragmentTransaction transaction;

        transaction = manager.beginTransaction();

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
        transaction.replace(R.id.meeting_container, fragment, null);
        transaction.commit();
    }

    private void deleteMeeting() {

    }
}
