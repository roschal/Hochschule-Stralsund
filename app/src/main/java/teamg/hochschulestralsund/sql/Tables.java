package teamg.hochschulestralsund.sql;

import android.provider.BaseColumns;

/**
 * Created by Stas Roschal on 12.05.18.
 */

public class Tables {
    public static class TIMETABLE implements BaseColumns {
        public static final String TABLE_NAME = "Timetable";
        public static final String COLUMN_ID_LECTURE = "id_lecture";
        public static final String COLUMN_ID_LECTURER = "id_lecturer";
        public static final String COLUMN_ID_LOCATION = "id_location";
        public static final String COLUMN_ID_TIME = "id_time";
    }

    public static class LECTURER implements BaseColumns {
        public static final String TABLE_NAME = "Lecturer";
        public static final String COLUMN_FORENAME = "forename";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_ACADEMIC_TITLE = "academic_title";
        public static final String COLUMN_TELEPHONE = "telephone";
        public static final String COLUMN_MAIL = "mail";
    }

    public static class LECTURE implements BaseColumns {
        public static final String TABLE_NAME = "Lecture";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LOCATION_ID = "location_id";
        public static final String COLUMN_LECTURER_ID = "lecturer_id";
        public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
        public static final String COLUMN_LECTURE_TIME_ID = "lecture_time_id";
        public static final String COLUMN_LECTURE_TYPE = "lecture_type";
    }

    public static class LECTURE_TIME implements BaseColumns {
        public static final String TABLE_NAME = "LectureTime";
        public static final String COLUMN_BEGIN = "begin";
        public static final String COLUMN_END = "end";
    }

    public static class LOCATION implements BaseColumns {
        public static final String TABLE_NAME = "Location";
        public static final String COLUMN_HOUSE = "house";
        public static final String COLUMN_ROOM = "room";
        public static final String COLUMN_NAME = "name";
    }

    public static class TIME implements BaseColumns {
        public static final String TABLE_NAME = "Time";
        public static final String COLUMN_BEGIN_HOUR = "begin_hour";
        public static final String COLUMN_BEGIN_MINUTE = "begin_minute";
        public static final String COLUMN_END_HOUR = "end_hour";
        public static final String COLUMN_END_MINUTE = "end_minute";
    }
}