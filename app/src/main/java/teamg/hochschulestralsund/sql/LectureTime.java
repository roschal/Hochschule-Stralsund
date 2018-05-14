package teamg.hochschulestralsund.sql;

import java.util.Calendar;

/**
 * Created by ghostgate on 11.05.18.
 */

public class LectureTime {
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
}
