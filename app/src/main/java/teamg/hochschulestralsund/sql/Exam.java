package teamg.hochschulestralsund.sql;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Exam implements Parcelable {

    public long exam_id;

    public String exam_title;
    public Calendar exam_begin = Calendar.getInstance();
    public Calendar exam_end = Calendar.getInstance();

    public Location exam_location;
    public Person exam_person;

    public String exam_type = "Prüfung";

    public int exam_default_location = 1;
    public int exam_default_person = 1;

    public Exam() {

    }

    protected Exam(Parcel in) {
        exam_id = in.readLong();
        exam_title = in.readString();
        exam_begin = (Calendar) in.readValue(Calendar.class.getClassLoader());
        exam_end = (Calendar) in.readValue(Calendar.class.getClassLoader());
        exam_location = (Location) in.readValue(Location.class.getClassLoader());
        exam_person = (Person) in.readValue(Person.class.getClassLoader());
        exam_type = in.readString();
        exam_default_location = in.readInt();
        exam_default_person = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(exam_id);
        dest.writeString(exam_title);
        dest.writeValue(exam_begin);
        dest.writeValue(exam_end);
        dest.writeValue(exam_location);
        dest.writeValue(exam_person);
        dest.writeString(exam_type);
        dest.writeInt(exam_default_location);
        dest.writeInt(exam_default_person);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Exam> CREATOR = new Parcelable.Creator<Exam>() {
        @Override
        public Exam createFromParcel(Parcel in) {
            return new Exam(in);
        }

        @Override
        public Exam[] newArray(int size) {
            return new Exam[size];
        }
    };
}