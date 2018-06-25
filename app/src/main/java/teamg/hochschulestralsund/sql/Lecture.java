package teamg.hochschulestralsund.sql;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Klasse für die DB-Objekte der Vorlesungen.
 */

public class Lecture implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Lecture> CREATOR = new Parcelable.Creator<Lecture>() {
        @Override
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };
    public long event_id = -1;
    public String event_title;
    public Calendar event_begin = Calendar.getInstance();
    public Calendar event_end = Calendar.getInstance();
    public Location event_location = new Location();
    public Person event_person = new Person();
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

    protected Lecture(Parcel in) {
        lecture_type = in.readString();
        lecture_repeat = in.readInt();
        lecture_default_location = in.readInt();
        lecture_default_person = in.readInt();
        lecture_default_time = in.readInt();
        lecture_time = (LectureTime) in.readValue(LectureTime.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lecture_type);
        dest.writeInt(lecture_repeat);
        dest.writeInt(lecture_default_location);
        dest.writeInt(lecture_default_person);
        dest.writeInt(lecture_default_time);
        dest.writeValue(lecture_time);
    }
}