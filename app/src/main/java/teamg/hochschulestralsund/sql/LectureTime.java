package teamg.hochschulestralsund.sql;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by ghostgate on 11.05.18.
 */

public class LectureTime implements Parcelable {
    public long id;

    public Calendar begin = Calendar.getInstance();
    public Calendar end = Calendar.getInstance();;

    public LectureTime()
    {

    }

    public LectureTime(long begin, long end)
    {
        this.begin.setTimeInMillis(begin);
        this.end.setTimeInMillis(end);
    }

    public LectureTime(long id, long begin, long end)
    {
        this.id = id;
        this.begin.setTimeInMillis(begin);
        this.end.setTimeInMillis(end);
    }

    public static String parseHour(String hour)
    {
        if(Integer.valueOf(hour).intValue() < 10)
        {
            return "0" + hour;
        }

        return hour;
    }

    public static String parseMinute(String minute)
    {
        if(Integer.valueOf(minute).intValue() == 0)
            return "00";
        else if(Integer.valueOf(minute).intValue() < 10)
        {
            return "0" + minute;
        }

        return minute;
    }

    @Override
    public String toString()
    {
        String beginHour = parseHour(Integer.toString(begin.get(Calendar.HOUR_OF_DAY)));
        String beginMinute = parseMinute(Integer.toString(begin.get(Calendar.MINUTE)));
        String endHour = parseHour(Integer.toString(end.get(Calendar.HOUR_OF_DAY)));
        String endMinute = parseMinute(Integer.toString(end.get(Calendar.MINUTE)));

        return beginHour + ":" + beginMinute + " - " + endHour + ":" + endMinute;
    }

    protected LectureTime(Parcel in) {
        id = in.readLong();
        begin = (Calendar) in.readValue(Calendar.class.getClassLoader());
        end = (Calendar) in.readValue(Calendar.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeValue(begin);
        dest.writeValue(end);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LectureTime> CREATOR = new Parcelable.Creator<LectureTime>() {
        @Override
        public LectureTime createFromParcel(Parcel in) {
            return new LectureTime(in);
        }

        @Override
        public LectureTime[] newArray(int size) {
            return new LectureTime[size];
        }
    };
}