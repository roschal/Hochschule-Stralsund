package teamg.hochschulestralsund;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import teamg.hochschulestralsund.connect.Parser;
import teamg.hochschulestralsund.sql.CustomSQL;
import teamg.hochschulestralsund.sql.Lecture;
import teamg.hochschulestralsund.sql.LectureTime;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{

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
    // Create menubar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timetable, menu);

        for(int i = 0; i < 3; i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
        }
        return true;
    }

    @Override
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
        CustomSQL customSQL = new CustomSQL(this);
        customSQL.addDefaultData();
        customSQL.close();
    }
    /* parse the website for lecturers */
    public void parse()
    {
        Parser myParser = new Parser(this);
        myParser.execute();
    }

    /* show the fragment for the current day */
    void showCurrentDay()
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ItemFragment fragment = new ItemFragment();

        transaction.add(R.id.timetable_container, fragment, "test");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    void showNextDay()
    {

    }

    void showPreviosDay()
    {

    }

}
