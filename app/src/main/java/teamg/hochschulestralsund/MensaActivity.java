package teamg.hochschulestralsund;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import teamg.hochschulestralsund.sql.Meal;

public class MensaActivity extends AppCompatActivity implements MensaItemFragment.OnListFragmentInteractionListener {
    public static String CODE_SHOW_MENSA = "CODE_SHOW_MENSA";

    public static void showMensas(FragmentManager manager, boolean firstTime, Calendar calendar) {
        Bundle bundle = new Bundle();
        bundle.putLong(CODE_SHOW_MENSA, calendar.getTimeInMillis());

        MensaItemFragment mensaItemFragment = new MensaItemFragment();
        FragmentTransaction transaction;
        mensaItemFragment.setArguments(bundle);

        transaction = manager.beginTransaction();
        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (firstTime)
            transaction.add(R.id.mensa_container, mensaItemFragment, null);
        else
            transaction.replace(R.id.mensa_container, mensaItemFragment, null);

        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa);

        showMensas(getFragmentManager(), true, Calendar.getInstance());
    }

    @Override
    public void onListFragmentInteraction(Meal meal) {

    }
}
