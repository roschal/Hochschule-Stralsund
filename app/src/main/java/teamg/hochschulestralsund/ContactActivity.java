package teamg.hochschulestralsund;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import teamg.hochschulestralsund.sql.Person;

public class ContactActivity extends AppCompatActivity implements ContactItemFragment.OnListFragmentInteractionListener {
    public static final String CODE_CONTACT = "CODE_CONTACT";
    public static final int CODE_CONTACT_ADD = 0;
    public static final int CODE_CONTACT_EDIT = 1;
    public static final int CODE_CONTACT_DELETE = 2;
    public static final int CODE_CONTACT_SHOW_ALL = 3;
    public static final String CODE_CONTACT_PARCELABLE = "CODE_CONTACT_PARCELABLE";

    private ContactItemFragment itemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        parseBundle();
    }

    @Override
    /**create the menu
     *
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact, menu);

        /* set the icon color for 2 menu icons */
        for (int i = 0; i < 1; i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
        }

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search_contact).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    /**override click handler on menu
     *
     * @return boolean
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_contact:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListFragmentInteraction(Person person) {

    }

    private void parseBundle() {
        if (getIntent().hasExtra(CODE_CONTACT)) {
            int code = getIntent().getIntExtra(CODE_CONTACT, CODE_CONTACT_SHOW_ALL);

            switch (code) {
                case CODE_CONTACT_SHOW_ALL:
                    showContacts(getFragmentManager(), true);
                    break;
            }

        }
    }

    /**
     * show all Contacts
     */
    public static void showContacts(FragmentManager manager, boolean firstTime) {
        ContactItemFragment contactItemFragment = new ContactItemFragment();
        FragmentTransaction transaction;

        transaction = manager.beginTransaction();

        if (firstTime)
            transaction.add(R.id.contact_container, contactItemFragment, null);
        else
            transaction.replace(R.id.contact_container, contactItemFragment, null);

        transaction.commit();
    }
}
