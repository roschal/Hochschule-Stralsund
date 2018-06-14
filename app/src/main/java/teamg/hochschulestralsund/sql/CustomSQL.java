package teamg.hochschulestralsund.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import teamg.hochschulestralsund.R;

/**
 * Created by Stas Roschal on 12.05.2018.
 */

public class CustomSQL extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "hochschule.db";

    private static final String SQL_CREATE_TABLE_EXAM =
            "CREATE TABLE IF NOT EXISTS " + Tables.EXAM.TABLE_NAME + " (" +
                    Tables.EXAM._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    Tables.EXAM.COLUMN_TITLE + " TEXT," +
                    Tables.EXAM.COLUMN_BEGIN + " BIGINT," +
                    Tables.EXAM.COLUMN_END + " BIGINT," +
                    Tables.EXAM.COLUMN_LOCATION_ID + " INTEGER," +
                    Tables.EXAM.COLUMN_LECTURER_ID + " INTEGER," +
                    Tables.EXAM.COLUMN_DEFAULT_LOCATION + " INTEGER," +
                    Tables.EXAM.COLUMN_DEFAULT_PERSON + " INTEGER," +
                    Tables.EXAM.COLUMN_TYPE + " TEXT)";

    private static final String SQL_CREATE_TABLE_LECTURE =
            "CREATE TABLE IF NOT EXISTS " + Tables.LECTURE.TABLE_NAME + " (" +
                    Tables.LECTURE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    Tables.LECTURE.COLUMN_TITLE + " TEXT," +
                    Tables.LECTURE.COLUMN_TYPE + " TEXT," +
                    Tables.LECTURE.COLUMN_REPEAT + " INTEGER," +
                    Tables.LECTURE.COLUMN_BEGIN + " BIGINT," +
                    Tables.LECTURE.COLUMN_END + " BIGINT," +
                    Tables.LECTURE.COLUMN_DEFAULT_LOCATION + " INTEGER," +
                    Tables.LECTURE.COLUMN_DEFAULT_PERSON + " INTEGER," +
                    Tables.LECTURE.COLUMN_DEFAULT_TIME + " INTEGER," +
                    Tables.LECTURE.COLUMN_LOCATION_ID + " INTEGER," +
                    Tables.LECTURE.COLUMN_LECTURER_ID + " INTEGER," +
                    Tables.LECTURE.COLUMN_LECTURE_TIME_ID + " INTEGER)";

    private static final String SQL_CREATE_TABLE_LECTURER =
            "CREATE TABLE IF NOT EXISTS " + Tables.LECTURER.TABLE_NAME + " (" +
                    Tables.LECTURER._ID + " INTEGER PRIMARY KEY," +
                    Tables.LECTURER.COLUMN_FORENAME + " TEXT," +
                    Tables.LECTURER.COLUMN_SURNAME + " TEXT," +
                    Tables.LECTURER.COLUMN_ACADEMIC_TITLE + " TEXT," +
                    Tables.LECTURER.COLUMN_TELEPHONE + " TEXT," +
                    Tables.LECTURER.COLUMN_MAIL + " TEXT)";

    private static final String SQL_CREATE_TABLE_LECTURE_TIME =
            "CREATE TABLE IF NOT EXISTS " + Tables.LECTURE_TIME.TABLE_NAME + " (" +
                    Tables.LECTURE_TIME._ID + " INTEGER PRIMARY KEY," +
                    Tables.LECTURE_TIME.COLUMN_BEGIN + " BIGINT," +
                    Tables.LECTURE_TIME.COLUMN_END + " BIGINT)";

    private static final String SQL_CREATE_TABLE_LOCATION =
            "CREATE TABLE IF NOT EXISTS " + Tables.LOCATION.TABLE_NAME + " (" +
                    Tables.LOCATION._ID + " INTEGER PRIMARY KEY," +
                    Tables.LOCATION.COLUMN_HOUSE + " TEXT," +
                    Tables.LOCATION.COLUMN_ROOM + " TEXT," +
                    Tables.LOCATION.COLUMN_NAME + " TEXT)";

    private static final String SQL_CREATE_TABLE_MEETING =
            "CREATE TABLE IF NOT EXISTS " + Tables.MEETING.TABLE_NAME + " (" +
                    Tables.MEETING._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    Tables.MEETING.COLUMN_TITLE + " TEXT," +
                    Tables.MEETING.COLUMN_DESCRIPTION + " TEXT," +
                    Tables.MEETING.COLUMN_CALENDAR + " BIGINT)";

    /* SQL delete */
    private static final String SQL_DELETE_TABLE_EXAM =
            "DROP TABLE IF EXISTS " + Tables.EXAM.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LECTURE =
            "DROP TABLE IF EXISTS " + Tables.LECTURE.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LECTURER =
            "DROP TABLE IF EXISTS " + Tables.LECTURER.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LECTURE_TIME =
            "DROP TABLE IF EXISTS " + Tables.LECTURE_TIME.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LOCATION =
            "DROP TABLE IF EXISTS " + Tables.LOCATION.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_MEETING =
            "DROP TABLE IF EXISTS " + Tables.MEETING.TABLE_NAME;

    public ArrayList<Lecture> lecturesOld;
    public ArrayList<Lecture> lecturesNew;

    public Context context;
    public SQLiteDatabase db;

    /* create database */
    public CustomSQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        try {
            this.context = context;
            db = getWritableDatabase();

            createTablesIfNotExist();
            addDefaultLectureTimes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDefaultData() {
        DefaultSQLData defaultSQLData = new DefaultSQLData(context);
    }

    /* create tables on create if not already exist */
    public void onCreate(SQLiteDatabase db) {

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTablesIfExist();
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /* add default lecture times */
    public void addDefaultLectureTimes() {
        /* get the array from array.xml */
        String[] defaultLectureTimes = context.getResources().getStringArray(R.array.defaultLectureTimes);

        for (int i = 0; i < defaultLectureTimes.length; i = i + 2) {
            long begin = formatStringToTime(defaultLectureTimes[i]);
            long end = formatStringToTime(defaultLectureTimes[i + 1]);

            addLectureTimeIfNotExist(new LectureTime(begin, end));
        }
    }

    public long formatStringToTime(String timeAsString) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        long millis = 0;

        try {
            Date mDate = sdf.parse(timeAsString);
            millis = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return millis;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /* add */

    /**Adds a Exam object to the database
     *
     * @param exam
     * @return id
     */
    public long addExam(Exam exam) {
        long id = -1;

        try {
            db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Tables.EXAM.COLUMN_TITLE, exam.exam_title);
            values.put(Tables.EXAM.COLUMN_BEGIN, exam.exam_begin.getTimeInMillis());
            values.put(Tables.EXAM.COLUMN_END, exam.exam_end.getTimeInMillis());
            values.put(Tables.EXAM.COLUMN_TYPE, exam.exam_type);
            values.put(Tables.EXAM.COLUMN_LOCATION_ID, exam.exam_location.id);
            values.put(Tables.EXAM.COLUMN_LECTURER_ID, exam.exam_person.id);
            values.put(Tables.EXAM.COLUMN_DEFAULT_LOCATION, exam.exam_default_location);
            values.put(Tables.EXAM.COLUMN_DEFAULT_PERSON, exam.exam_default_person);

            Log.d("Adding new exam", "...");

            Log.i(Tables.EXAM.COLUMN_TITLE, exam.exam_title);
            Log.i(Tables.EXAM.COLUMN_BEGIN, Long.toString(exam.exam_begin.getTimeInMillis()));
            Log.i(Tables.EXAM.COLUMN_END, Long.toString(exam.exam_end.getTimeInMillis()));
            Log.i(Tables.EXAM.COLUMN_TYPE, exam.exam_type);
            Log.i(Tables.EXAM.COLUMN_LOCATION_ID, Long.toString(exam.exam_location.id));
            Log.i(Tables.EXAM.COLUMN_LECTURER_ID, Long.toString(exam.exam_person.id));
            Log.i(Tables.EXAM.COLUMN_DEFAULT_LOCATION, Integer.toString(exam.exam_default_location));
            Log.i(Tables.EXAM.COLUMN_DEFAULT_PERSON, Integer.toString(exam.exam_default_person));

            id = db.insert(Tables.EXAM.TABLE_NAME, null, values);

            /* if everything is fine */
            if(id > 0)
                Log.d("Adding new exam", "OK - id " + String.valueOf(id));
            else
                Log.e("Adding new exam", "FAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    /**Adds a Lecture object to the database
     *
     * @param lecture
     * @return id
     */
    public long addLecture(Lecture lecture) {
        long id = -1;

        try {
            db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Tables.LECTURE.COLUMN_TITLE, lecture.event_title);
            values.put(Tables.LECTURE.COLUMN_TYPE, lecture.lecture_type);
            values.put(Tables.LECTURE.COLUMN_REPEAT, lecture.lecture_repeat);
            values.put(Tables.LECTURE.COLUMN_BEGIN, lecture.event_begin.getTimeInMillis());
            values.put(Tables.LECTURE.COLUMN_END, lecture.event_end.getTimeInMillis());

            values.put(Tables.LECTURE.COLUMN_DEFAULT_LOCATION, lecture.lecture_default_location);
            values.put(Tables.LECTURE.COLUMN_DEFAULT_PERSON, lecture.lecture_default_person);
            values.put(Tables.LECTURE.COLUMN_DEFAULT_TIME, lecture.lecture_default_time);

            values.put(Tables.LECTURE.COLUMN_LOCATION_ID, lecture.event_location.id);
            values.put(Tables.LECTURE.COLUMN_LECTURER_ID, lecture.event_person.id);
            values.put(Tables.LECTURE.COLUMN_LECTURE_TIME_ID, lecture.lecture_time.id);

            Log.d("Adding new Lecture", "...");

            Log.i(Tables.LECTURE.COLUMN_TITLE, lecture.event_title);
            Log.i(Tables.LECTURE.COLUMN_TYPE, lecture.lecture_type);
            Log.i(Tables.LECTURE.COLUMN_REPEAT, Integer.toString(lecture.lecture_repeat));
            Log.i(Tables.LECTURE.COLUMN_BEGIN, Long.toString(lecture.event_begin.getTimeInMillis()));
            Log.i(Tables.LECTURE.COLUMN_END, Long.toString(lecture.event_end.getTimeInMillis()));

            Log.i(Tables.LECTURE.COLUMN_DEFAULT_LOCATION, Integer.toString(lecture.lecture_default_location));
            Log.i(Tables.LECTURE.COLUMN_DEFAULT_PERSON, Integer.toString(lecture.lecture_default_person));
            Log.i(Tables.LECTURE.COLUMN_DEFAULT_TIME, Integer.toString(lecture.lecture_default_time));

            Log.i(Tables.LECTURE.COLUMN_LOCATION_ID, Long.toString(lecture.event_location.id));
            Log.i(Tables.LECTURE.COLUMN_LECTURER_ID, Long.toString(lecture.event_person.id));
            Log.i(Tables.LECTURE.COLUMN_LECTURE_TIME_ID, Long.toString(lecture.lecture_time.id));

            id = db.insert(Tables.LECTURE.TABLE_NAME, null, values);

            /* if everything is fine */
            if(id > 0)
                Log.d("Adding new lecture", "OK - id " + String.valueOf(id));
            else
                Log.e("Adding new lecture", "FAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    /* add a lecture object to sql database without checking if it is already existing */
    public long addLecturer(Person lecturer) {
       try {
           ContentValues values = new ContentValues();
           values.put(Tables.LECTURER.COLUMN_FORENAME, lecturer.forename);
           values.put(Tables.LECTURER.COLUMN_SURNAME, lecturer.surname);
           values.put(Tables.LECTURER.COLUMN_ACADEMIC_TITLE, lecturer.academic_title);
           values.put(Tables.LECTURER.COLUMN_MAIL, lecturer.mail);
           values.put(Tables.LECTURER.COLUMN_TELEPHONE, lecturer.telephone);

           return db.insert(Tables.LECTURER.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /* add a lecture object to sql database but only if it is not already existing */
    public void addLecturerIfNotExist(Person lecturer) {
        try {
            Boolean exists = existsLecturer(lecturer);

            /* if not exists then add */
            if (!exists)
                addLecturer(lecturer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* add a new lecture time to sql database */
    public long addLectureTime(LectureTime lectureTime) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.LECTURE_TIME.COLUMN_BEGIN, lectureTime.begin.getTimeInMillis());
            values.put(Tables.LECTURE_TIME.COLUMN_END, lectureTime.end.getTimeInMillis());

            return db.insert(Tables.LECTURE_TIME.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /* add a lecture time if it is not already existing */
    public void addLectureTimeIfNotExist(LectureTime lectureTime) {
        try {
            Boolean exists = existsLectureTime(lectureTime);

            /* if not exists then add */
            if (!exists)
                addLectureTime(lectureTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLocation(Location location) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.LOCATION.COLUMN_HOUSE, location.house);
            values.put(Tables.LOCATION.COLUMN_ROOM, location.room);
            values.put(Tables.LOCATION.COLUMN_NAME, location.name);

            db.insert(Tables.LOCATION.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLocationIfNotExist(Location location) {
        try {
            Boolean exists = existsLocation(location);

            /* if not exists then add */
            if (!exists)
                addLocation(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Adds a Lecture object to the database
     *
     * @param meeting
     * @return id
     */
    public long addMeeting(Meeting meeting) {
        long id = -1;

        try {
            db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Tables.MEETING.COLUMN_TITLE, meeting.meeting_title);
            values.put(Tables.MEETING.COLUMN_DESCRIPTION, meeting.meeting_description);
            values.put(Tables.MEETING.COLUMN_CALENDAR, meeting.meeting_calendar.getTimeInMillis());

            Log.d("Adding new Meeting", "...");

            Log.i(Tables.MEETING.COLUMN_TITLE, meeting.meeting_title);
            Log.i(Tables.MEETING.COLUMN_DESCRIPTION, meeting.meeting_description);
            Log.i(Tables.MEETING.COLUMN_CALENDAR, Long.toString(meeting.meeting_calendar.getTimeInMillis()));

            id = db.insert(Tables.MEETING.TABLE_NAME, null, values);

            /* if everything is fine */
            if(id > 0)
                Log.d("Adding new Meeting", "OK - id " + String.valueOf(id));
            else
                Log.e("Adding new Meeting", "FAIL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // create

    public void createTablesIfNotExist() {
        try {
            db.execSQL(SQL_CREATE_TABLE_EXAM);
            db.execSQL(SQL_CREATE_TABLE_LECTURE);
            db.execSQL(SQL_CREATE_TABLE_LECTURER);
            db.execSQL(SQL_CREATE_TABLE_LECTURE_TIME);
            db.execSQL(SQL_CREATE_TABLE_LOCATION);
            db.execSQL(SQL_CREATE_TABLE_MEETING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // delete

    public void deleteLecture(Lecture lecture) {
        try {
            Log.d("Deleting Lecture", "...");
            Log.d("Lecture id", String.valueOf(lecture.event_id));

            int count = db.delete(Tables.LECTURE.TABLE_NAME, Tables.LECTURE._ID + "=?", new String[] {Long.toString(lecture.event_id)});
            Log.d("Deleted", Integer.toString(count) + " times");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTablesIfExist() {
        try {
            db.execSQL(SQL_DELETE_TABLE_LECTURE);
            db.execSQL(SQL_DELETE_TABLE_LECTURER);
            db.execSQL(SQL_DELETE_TABLE_LECTURE_TIME);
            db.execSQL(SQL_DELETE_TABLE_LOCATION);
            db.execSQL(SQL_DELETE_TABLE_MEETING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // get

    public ArrayList<Exam> getExams() {
        ArrayList<Exam> exams = new ArrayList<>();

        try {
            Log.d("Getting exams", "...");

            final String QUERY_EXAM = "SELECT * FROM " + Tables.EXAM.TABLE_NAME
                    + " a INNER JOIN " + Tables.LOCATION.TABLE_NAME + " b ON a." + Tables.EXAM.COLUMN_LOCATION_ID + "=b._ID"
                    + " INNER JOIN " + Tables.LECTURER.TABLE_NAME + " c ON a." + Tables.EXAM.COLUMN_LECTURER_ID + "=c._ID";

            Cursor cursor = db.rawQuery(QUERY_EXAM, null);

            while (cursor.moveToNext()) {
                Exam exam = new Exam();

                exam.exam_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.EXAM._ID));
                exam.exam_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_TITLE));
                exam.exam_begin.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_BEGIN)));
                exam.exam_end.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_END)));
                exam.exam_type = cursor.getString(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_TYPE));
                exam.exam_default_location = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_DEFAULT_LOCATION));
                exam.exam_default_person = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_DEFAULT_PERSON));

                /* location */
                long location_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_LOCATION_ID));
                String house = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_HOUSE));
                String room = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_ROOM));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_NAME));

                exam.exam_location = new Location(location_id, house, room, name);

                /* lecturer */
                long lecturer_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.EXAM.COLUMN_LECTURER_ID));
                String forename = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_FORENAME));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_SURNAME));
                String academic_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_ACADEMIC_TITLE));
                String mail = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_MAIL));
                String telephone = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_TELEPHONE));

                exam.exam_person = (new Person(lecturer_id, forename, surname, academic_title, mail, telephone));

                exams.add(exam);
            }

            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < exams.size(); i++)
            Log.e("BLA", exams.get(i).exam_title);

        return exams;
    }

    /* get lecturers for a specific day */
    /* should be changed to sql query */
    public ArrayList<Lecture> getLectures(int day) {
        lecturesOld = new ArrayList<>();
        lecturesNew = new ArrayList<>();

        lecturesOld.clear();
        lecturesNew.clear();

        lecturesOld = getLectures();

        for(int i = 0; i < lecturesOld.size(); i++) {
            if(lecturesOld.get(i).event_begin.get(Calendar.DAY_OF_WEEK) == day) {
               lecturesNew.add(lecturesOld.get(i));
            }
        }

        return lecturesNew;
    }

    public ArrayList<Lecture> getLectures() {
        ArrayList<Lecture> lectures = new ArrayList<>();

        try {
            Log.d("Getting lectures", "...");

            final String QUERY_LECTURE = "SELECT * FROM " + Tables.LECTURE.TABLE_NAME
                    + " a INNER JOIN " + Tables.LOCATION.TABLE_NAME + " b ON a." + Tables.LECTURE.COLUMN_LOCATION_ID + "=b._ID"
                    + " INNER JOIN " + Tables.LECTURER.TABLE_NAME + " c ON a." + Tables.LECTURE.COLUMN_LECTURER_ID + "=c._ID"
                    + " INNER JOIN " + Tables.LECTURE_TIME.TABLE_NAME + " d ON a." + Tables.LECTURE.COLUMN_LECTURE_TIME_ID + "=d._ID";

            Cursor cursor = db.rawQuery(QUERY_LECTURE, null);

            while (cursor.moveToNext()) {
                Lecture lecture = new Lecture();

                lecture.event_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE._ID));
                lecture.event_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_TITLE));
                lecture.lecture_type = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_TYPE));
                lecture.lecture_repeat = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_REPEAT));
                lecture.event_begin.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_BEGIN)));
                lecture.event_end.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_END)));
                lecture.lecture_default_location = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_DEFAULT_LOCATION));
                lecture.lecture_default_person = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_DEFAULT_PERSON));
                lecture.lecture_default_time = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_DEFAULT_TIME));

                /* location */
                long location_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_LOCATION_ID));
                String house = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_HOUSE));
                String room = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_ROOM));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_NAME));

                lecture.event_location = new Location(location_id, house, room, name);

                /* lecturer */
                long lecturer_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_LECTURER_ID));
                String forename = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_FORENAME));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_SURNAME));
                String academic_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_ACADEMIC_TITLE));
                String mail = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_MAIL));
                String telephone = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_TELEPHONE));

                lecture.event_person = (new Person(lecturer_id, forename, surname, academic_title, mail, telephone));

                /* lecture_time */
                long lecture_time_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_LECTURE_TIME_ID));
                long begin = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE_TIME.COLUMN_BEGIN));
                long end = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE_TIME.COLUMN_END));

                lecture.lecture_time = new LectureTime(lecture_time_id, begin, end);

                lectures.add(lecture);
            }

            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < lectures.size(); i++)
            Log.e("BLA", lectures.get(i).event_title);
        return lectures;
    }

    /* check if an Lecturer is already saved in the database */
    public Boolean existsLecturer(Person lecturer) {
        try {
            String[] projection = {
                    Tables.LECTURER._ID,
                    Tables.LECTURER.COLUMN_FORENAME,
                    Tables.LECTURER.COLUMN_SURNAME,
                    Tables.LECTURER.COLUMN_ACADEMIC_TITLE,
                    Tables.LECTURER.COLUMN_MAIL,
                    Tables.LECTURER.COLUMN_TELEPHONE
            };

            String selection = Tables.LECTURER.COLUMN_FORENAME + " = ? AND " +
                    Tables.LECTURER.COLUMN_SURNAME + " = ? ";
            String[] selectionArgs = {lecturer.forename, lecturer.surname};

            Cursor cursor = db.query(
                    Tables.LECTURER.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            int result = cursor.getCount();

            if (result > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Person> getLecturers() {
        ArrayList lecturers = new ArrayList<Person>();

        try {
            String[] projection = {
                    Tables.LECTURER._ID,
                    Tables.LECTURER.COLUMN_FORENAME,
                    Tables.LECTURER.COLUMN_SURNAME,
                    Tables.LECTURER.COLUMN_ACADEMIC_TITLE,
                    Tables.LECTURER.COLUMN_MAIL,
                    Tables.LECTURER.COLUMN_TELEPHONE
            };

            String sortOrder = Tables.LECTURER.COLUMN_SURNAME + " DESC";

            Cursor cursor = db.query(
                    Tables.LECTURER.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURER._ID));
                String forename = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_FORENAME));

                String surname = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_SURNAME));
                String academic_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_ACADEMIC_TITLE));
                String mail = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_MAIL));
                String telephone = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_TELEPHONE));

                lecturers.add(new Person(id, forename, surname, academic_title, mail, telephone));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lecturers;
    }

    public Boolean existsLectureTime(LectureTime lectureTime) {
        try {
            String[] projection = {
                    Tables.LECTURE_TIME._ID,
                    Tables.LECTURE_TIME.COLUMN_BEGIN,
                    Tables.LECTURE_TIME.COLUMN_END
            };


            String selection = Tables.LECTURE_TIME.COLUMN_BEGIN + " = ? AND " +
                    Tables.LECTURE_TIME.COLUMN_END + " = ? ";
            String[] selectionArgs = {Long.toString(lectureTime.begin.getTimeInMillis()), Long.toString(lectureTime.end.getTimeInMillis())};

            Cursor cursor = db.query(
                    Tables.LECTURE_TIME.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            int result = cursor.getCount();
            cursor.close();

            if(result > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public ArrayList<LectureTime> getLectureTimes() {
        ArrayList lectureTimes = new ArrayList<LectureTime>();

        try {
            String[] projection = {
                    Tables.LECTURE_TIME._ID,
                    Tables.LECTURE_TIME.COLUMN_BEGIN,
                    Tables.LECTURE_TIME.COLUMN_END
            };

            /* begin with the lowest */
            String sortOrder = Tables.LECTURE_TIME.COLUMN_BEGIN + " ASC";

            Cursor cursor = db.query(
                    Tables.LECTURE_TIME.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE_TIME._ID));
                long begin = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE_TIME.COLUMN_BEGIN));
                long end = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE_TIME.COLUMN_END));

                lectureTimes.add(new LectureTime(id, begin, end));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lectureTimes;
    }

    /* check if a location is already saved in the database */
    public Boolean existsLocation(Location location) {
        try {
            String[] projection = {
                    Tables.LOCATION._ID,
                    Tables.LOCATION.COLUMN_HOUSE,
                    Tables.LOCATION.COLUMN_ROOM,
                    Tables.LOCATION.COLUMN_NAME
            };

            String selection = Tables.LOCATION.COLUMN_HOUSE + " = ? AND " +
                    Tables.LOCATION.COLUMN_ROOM + " = ? AND " +
                    Tables.LOCATION.COLUMN_NAME + " = ? ";
            String[] selectionArgs = {location.house, location.room, location.name};

            Cursor cursor = db.query(
                    Tables.LOCATION.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            int result = cursor.getCount();

            if (result > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Location> getLocations() {
        ArrayList locations = new ArrayList<Location>();

        try {
            String[] projection = {
                    Tables.LOCATION._ID,
                    Tables.LOCATION.COLUMN_HOUSE,
                    Tables.LOCATION.COLUMN_ROOM,
                    Tables.LOCATION.COLUMN_NAME
            };

            String sortOrder = Tables.LOCATION.COLUMN_HOUSE + " DESC";

            Cursor cursor = db.query(
                    Tables.LOCATION.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LOCATION._ID));
                String house = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_HOUSE));
                String room = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_ROOM));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_NAME));

                locations.add(new Location(id, house, room, name));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    /**get a list with all saved meetings
     *
     *
     * @return
     */
    public ArrayList<Meeting> getMeetings() {
        ArrayList meetings = new ArrayList<Meeting>();

        try {
            String[] projection = {
                    Tables.MEETING._ID,
                    Tables.MEETING.COLUMN_TITLE,
                    Tables.MEETING.COLUMN_DESCRIPTION,
                    Tables.MEETING.COLUMN_CALENDAR
            };

            //* get the oldest meeting first
            String sortOrder = Tables.MEETING.COLUMN_CALENDAR + " ASC";

            Cursor cursor = db.query(
                    Tables.MEETING.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder
            );


            while (cursor.moveToNext()) {
                Meeting meeting = new Meeting();

                meeting.meeting_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.MEETING._ID));
                meeting.meeting_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.MEETING.COLUMN_TITLE));
                meeting.meeting_description = cursor.getString(cursor.getColumnIndexOrThrow(Tables.MEETING.COLUMN_DESCRIPTION));
                meeting.meeting_calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(Tables.MEETING.COLUMN_CALENDAR)));

                meetings.add(meeting);
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return meetings;
    }
}