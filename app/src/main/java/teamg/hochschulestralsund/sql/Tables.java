package teamg.hochschulestralsund.sql;

import android.provider.BaseColumns;

/**
 * Created by Stas Roschal on 12.05.18.
 */

public class Tables {
    public static class EXAM implements BaseColumns {
        public static final String TABLE_NAME = "Exam";
        public static final String COLUMN_TITLE = "exam_title";
        public static final String COLUMN_BEGIN = "exam_begin";
        public static final String COLUMN_END = "exam_end";
        public static final String COLUMN_TYPE = "exam_type";
        public static final String COLUMN_LOCATION_ID = "exam_location_id";
        public static final String COLUMN_LECTURER_ID = "exam_person_id";
        public static final String COLUMN_DEFAULT_LOCATION = "exam_default_location";
        public static final String COLUMN_DEFAULT_PERSON = "exam_default_person";
    }

    public static class LECTURER implements BaseColumns {
        public static final String TABLE_NAME = "Lecturer";
        public static final String COLUMN_FORENAME = "forename";
        public static final String COLUMN_SURNAME = "surname";
        public static final String COLUMN_ACADEMIC_TITLE = "academic_title";
        public static final String COLUMN_TELEPHONE = "telephone";
        public static final String COLUMN_MAIL = "mail";
        public static final String COLUMN_PICTURE1_PATH = "picture1_path";
        public static final String COLUMN_PICTURE2_PATH = "picture2_path";
        public static final String COLUMN_PICTURE3_PATH = "picture3_path";
    }

    public static class LECTURE implements BaseColumns {
        public static final String TABLE_NAME = "Lecture";
        public static final String COLUMN_TITLE = "lecture_title";
        public static final String COLUMN_TYPE = "lecture_type";
        public static final String COLUMN_REPEAT = "lecture_repeat";
        public static final String COLUMN_BEGIN = "lecture_begin";
        public static final String COLUMN_END = "lecture_end";
        public static final String COLUMN_DEFAULT_LOCATION = "lecture_default_location";
        public static final String COLUMN_DEFAULT_PERSON = "lecture_default_person";
        public static final String COLUMN_DEFAULT_TIME = "lecture_default_time";
        public static final String COLUMN_LOCATION_ID = "lecture_location_id";
        public static final String COLUMN_LECTURER_ID = "lecture_person_id";
        public static final String COLUMN_LECTURE_TIME_ID = "lecture_time_id";
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

    public static class MEETING implements BaseColumns {
        public static final String TABLE_NAME = "Meeting";
        public static final String COLUMN_TITLE = "meeting_title";
        public static final String COLUMN_DESCRIPTION = "meeting_description";
        public static final String COLUMN_CALENDAR = "meeting_calendar";
    }
}