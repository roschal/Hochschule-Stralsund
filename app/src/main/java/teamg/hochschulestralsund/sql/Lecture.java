package teamg.hochschulestralsund.sql;

/**
 * Created by ghostgate on 11.05.18.
 */

public class Lecture {
    public long id;
    public String title;
    public Location location;
    public Lecturer lecturer;
    public int DAY_OF_WEEK;
    public LectureTime lectureTime;
    public String lectureType;

    public Lecture() {

    }

    public Lecture(String title, Location location, Lecturer lecturer, int DAY_OF_WEEK, LectureTime lectureTime, String lectureType) {
        this.title = title;
        this.location = location;
        this.lecturer = lecturer;
        this.DAY_OF_WEEK = DAY_OF_WEEK;
        this.lectureTime = lectureTime;
        this.lectureType = lectureType;
    }

    public Lecture(long id, String title, Location location, Lecturer lecturer, int DAY_OF_WEEK, LectureTime lectureTime, String lectureType) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.lecturer = lecturer;
        this.DAY_OF_WEEK = DAY_OF_WEEK;
        this.lectureTime = lectureTime;
        this.lectureType = lectureType;
    }
}
