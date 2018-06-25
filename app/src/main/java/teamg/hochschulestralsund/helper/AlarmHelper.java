package teamg.hochschulestralsund.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import teamg.hochschulestralsund.AlarmActivity;

/**
 * Ein Helper zum Erzeugen eines Alarms.
 * @author Paul Schindler
 *
 */
public class AlarmHelper {
    private Context context;
    private AlarmManager alarmManager;

    /**
     * Konstruktor des Alarmhelpers
     *
     * @param context {Context}
     * @param alarmManager {AlarmManager}
     */
    public AlarmHelper(Context context, AlarmManager alarmManager) {
        this.context = context;
        this.alarmManager = alarmManager;
    }

    /**
     * Erzeugt einen Alarm am übergebenen Zeitpunkt, mit der AlarmId und dem Alarmtext
     *
     * @param timeInMills {long}
     * @param alarmText {String}
     * @param alarmId {int}
     */
    public void createAlarm(long timeInMills, String alarmText, int alarmId) {
        PendingIntent alarmIntent = createAlarmIntent(alarmText, alarmId);
        this.alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMills, alarmIntent);
    }

    /**
     * Erzeugt ein PendingIntent mit dem der Alarm gesetzt bzw. abgebrochen werden kann
     *
     * @return {PendingIntent}
     */
    private PendingIntent createAlarmIntent(String alarmText, int alarmId) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("ALARM_TEXT", alarmText);
        return PendingIntent.getActivity(context, alarmId, intent, 0);
    }

    /**
     * Bricht den Alarm mit der übergebenene AlarmId und Alarmtext ab
     *
     * @param alarmText {String}
     * @param alarmId {int}
     */
    public void cancelAlarm(String alarmText, int alarmId){
        PendingIntent alarmIntent = createAlarmIntent(alarmText, alarmId);
        this.alarmManager.cancel(alarmIntent);
    }

    /**
     * Create a AlarmId for a Alarm
     *
     * @return {Int}
     */
    public static int createAlarmId(){
        Calendar calendar = Calendar.getInstance();
        return (int) calendar.getTimeInMillis();
    }
}
