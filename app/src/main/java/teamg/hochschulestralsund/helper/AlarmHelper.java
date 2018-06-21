package teamg.hochschulestralsund.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import teamg.hochschulestralsund.AlarmActivity;

public class AlarmHelper {
    private Context context;
    private AlarmManager alarmManager;

    public AlarmHelper(Context context, AlarmManager alarmManager) {
        this.context = context;
        this.alarmManager = alarmManager;
    }

    public void createAlarm(long timeInMills, String alarmText) {
        PendingIntent alarmIntent = createAlarmIntent(alarmText, timeInMills);
        this.alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMills, alarmIntent);
    }

    /**
     * Erzeugt ein PendingIntent mit dem der Alarm gesetzt bzw. abgebrochen werden kann
     *
     * @return {PendingIntent}
     */
    private PendingIntent createAlarmIntent(String alarmText, long timeInMills) {
        int uuid = (int) timeInMills;
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("ALARM_TEXT", alarmText);
        return PendingIntent.getActivity(context, uuid, intent, 0);
    }

    public void cancelAlarm(String alarmText, long timeInMills){
        PendingIntent alarmIntent = createAlarmIntent(alarmText, timeInMills);
        this.alarmManager.cancel(alarmIntent);
    }
}
