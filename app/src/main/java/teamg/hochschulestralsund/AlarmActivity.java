package teamg.hochschulestralsund;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AlarmActivity extends AppCompatActivity {

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Bundle b = getIntent().getExtras();

        if (b != null) {
            String alarmText = (String) b.get("ALARM_TEXT");
            setTitle(alarmText);
            TextView textView = findViewById(R.id.textView_alarm_alarmText);
            textView.setText(alarmText);
            createNotification(alarmText);
        }

        player = MediaPlayer.create(getApplicationContext(), R.raw.nice_wake_up_call);

        player.setLooping(true);

        player.start();

    }

    public void onStopAlarmBtnClicked(View view) {
        player.stop();
        finish();
    }

    private void createNotification(String alarmText) {
        String notificationsText = "Erinnerung: " + alarmText;

        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        builder.setContentTitle("Terminerinnerung")
                .setContentText(notificationsText)
                .setSmallIcon(R.mipmap.ic_launcher);

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setVisibility(Notification.VISIBILITY_PUBLIC);

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }
}
