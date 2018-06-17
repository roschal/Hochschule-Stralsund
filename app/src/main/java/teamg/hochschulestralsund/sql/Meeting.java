package teamg.hochschulestralsund.sql;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Meeting implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {
        @Override
        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };
    public long meeting_id = -1;
    public String meeting_title = "";
    public String meeting_description = "";
    public Calendar meeting_calendar = Calendar.getInstance();

    public Meeting() {

    }

    public Meeting(String meeting_title, String meeting_description, Calendar meeting_calendar) {
        this.meeting_title = meeting_title;
        this.meeting_description = meeting_description;
        this.meeting_calendar.setTime(meeting_calendar.getTime());
    }


    public Meeting(long meeting_id, String meeting_title, String meeting_description, Calendar meeting_calendar) {
        this.meeting_id = meeting_id;
        this.meeting_title = meeting_title;
        this.meeting_description = meeting_description;
        this.meeting_calendar.setTime(meeting_calendar.getTime());
    }

    protected Meeting(Parcel in) {
        meeting_id = in.readLong();
        meeting_title = in.readString();
        meeting_description = in.readString();
        meeting_calendar = (Calendar) in.readValue(Calendar.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(meeting_id);
        dest.writeString(meeting_title);
        dest.writeString(meeting_description);
        dest.writeValue(meeting_calendar);
    }
}