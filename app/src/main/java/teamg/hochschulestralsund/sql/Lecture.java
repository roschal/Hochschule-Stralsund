package teamg.hochschulestralsund.sql;

import java.util.Calendar;

/**
 * Created by ghostgate on 11.05.18.
 */

public class Lecture extends Event {
    public String lecture_type; /* lecture type */
    public int lecture_repeat = 7;      /* 7 oder 14 days */

    public int lecture_default_location = 1;
    public int lecture_default_person = 1;
    public int lecture_default_time = 1;

    public LectureTime lecture_time;

    public Lecture() {

    }

    public Lecture(String event_title, String lecture_type, int lecture_repeat, Calendar event_begin, Calendar event_end, Location event_location, Person lecture_person, LectureTime lecture_time) {
        this.event_title = event_title;
        this.lecture_type = lecture_type;
        this.lecture_repeat = lecture_repeat;
        this.event_begin = Calendar.getInstance();
        this.event_begin.setTime(event_begin.getTime());
        this.event_end = Calendar.getInstance();
        this.event_end.setTime(event_end.getTime());
        this.event_location = event_location;
        this.event_person = lecture_person;
        this.lecture_time = lecture_time;
    }

    public Lecture(long event_id, String event_title, String lecture_type, int lecture_repeat, Calendar event_begin, Calendar event_end, Location event_location, Person lecture_person, LectureTime lecture_time) {
        this.event_id = event_id;
        this.event_title = event_title;
        this.lecture_type = lecture_type;
        this.lecture_repeat = lecture_repeat;
        this.event_begin = Calendar.getInstance();
        this.event_begin.setTime(event_begin.getTime());
        this.event_end = Calendar.getInstance();
        this.event_end.setTime(event_end.getTime());
        this.event_location = event_location;
        this.event_person = lecture_person;
        this.lecture_time = lecture_time;
    }
}

