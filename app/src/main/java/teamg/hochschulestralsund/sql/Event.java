package teamg.hochschulestralsund.sql;

import java.util.Calendar;

public class Event {
    public long event_id;

    public String event_title;
    public Calendar event_begin = Calendar.getInstance();
    public Calendar event_end = Calendar.getInstance();

    public Location event_location;
    public Person event_person;
}
