package teamg.hochschulestralsund.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import teamg.hochschulestralsund.AlarmActivity;

public class AlarmHelper {
    private Context context;
    private AlarmManager alarmManager;

    public AlarmHelper(Context context, AlarmManager alarmManager) {
        this.context = context;
        this.alarmManager = alarmManager;
    }

    public void createAlarm(int time, String alarmText) {
        PendingIntent alarmIntent = createAlarmIntent(alarmText);
        this.alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, alarmIntent);
    }

    /**
     * Erzeugt ein PendingIntent mit dem der Alarm gesetzt bzw. abgebrochen werden kann
     *
     * @return {PendingIntent}
     */
    private PendingIntent createAlarmIntent(String alarmText) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra("ALARM_TEXT", alarmText);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
