package teamg.hochschulestralsund.sql;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import teamg.hochschulestralsund.R;

/**
 * Created by Stas Roschal on 12.05.2018.
 */

public class CustomSQL extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "hochschule.db";

    private static final String SQL_CREATE_TABLE_TIMETABLE =
            "CREATE TABLE IF NOT EXISTS " + Tables.TIMETABLE.TABLE_NAME + " (" +
                    Tables.TIMETABLE._ID + " INTEGER PRIMARY KEY," +
                    Tables.TIMETABLE.COLUMN_ID_LECTURE + " TEXT," +
                    Tables.TIMETABLE.COLUMN_ID_LECTURER + " TEXT," +
                    Tables.TIMETABLE.COLUMN_ID_LOCATION + " TEXT," +
                    Tables.TIMETABLE.COLUMN_ID_TIME + " TEXT)";
    private static final String SQL_CREATE_TABLE_LECTURE =
            "CREATE TABLE IF NOT EXISTS " + Tables.LECTURE.TABLE_NAME + " (" +
                    Tables.LECTURE._ID + " INTEGER PRIMARY KEY," +
                    Tables.LECTURE.COLUMN_TITLE + " TEXT," +
                    Tables.LECTURE.COLUMN_LOCATION_ID + " INTEGER," +
                    Tables.LECTURE.COLUMN_LECTURER_ID + " INTEGER," +
                    Tables.LECTURE.COLUMN_DAY_OF_WEEK + " INTEGER," +
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
                    Tables.LOCATION.COLUMN_ROOM + " TEXT)";

    private static final String SQL_CREATE_TABLE_TIME =
            "CREATE TABLE IF NOT EXISTS " + Tables.TIME.TABLE_NAME + " (" +
                    Tables.TIME._ID + " INTEGER PRIMARY KEY," +
                    Tables.TIME.COLUMN_BEGIN_HOUR + " TEXT," +
                    Tables.TIME.COLUMN_BEGIN_MINUTE + " TEXT," +
                    Tables.TIME.COLUMN_END_HOUR + " TEXT," +
                    Tables.TIME.COLUMN_END_MINUTE + " TEXT)";

    /* SQL delete */
    private static final String SQL_DELETE_TABLE_TIMETABLE =
            "DROP TABLE IF EXISTS " + Tables.TIMETABLE.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LECTURE =
            "DROP TABLE IF EXISTS " + Tables.LECTURE.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LECTURER =
            "DROP TABLE IF EXISTS " + Tables.LECTURER.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LECTURE_TIME =
            "DROP TABLE IF EXISTS " + Tables.LECTURE_TIME.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_LOCATION =
            "DROP TABLE IF EXISTS " + Tables.LOCATION.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_TIME =
            "DROP TABLE IF EXISTS " + Tables.TIME.TABLE_NAME;

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

    public long addLecture(Lecture lecture) {
        try {
            ContentValues values = new ContentValues();
            values.put(Tables.LECTURE.COLUMN_TITLE, lecture.title);
            values.put(Tables.LECTURE.COLUMN_LOCATION_ID, lecture.location.id);
            values.put(Tables.LECTURE.COLUMN_LECTURER_ID, lecture.lecturer.id);
            values.put(Tables.LECTURE.COLUMN_DAY_OF_WEEK, lecture.DAY_OF_WEEK);
            values.put(Tables.LECTURE.COLUMN_LECTURE_TIME_ID, lecture.lectureTime.id);

            return db.insert(Tables.LECTURE.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /* add a lecture object to sql database without checking if it is already existing */
    public long addLecturer(Lecturer lecturer) {
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
    public void addLecturerIfNotExist(Lecturer lecturer) {
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


    /////////////////////////////////////////////////////////////////////////////////////////
    // create

    public void createTablesIfNotExist() {
        try {
            db.execSQL(SQL_CREATE_TABLE_TIMETABLE);
            db.execSQL(SQL_CREATE_TABLE_LECTURE);
            db.execSQL(SQL_CREATE_TABLE_LECTURER);
            db.execSQL(SQL_CREATE_TABLE_LECTURE_TIME);
            db.execSQL(SQL_CREATE_TABLE_LOCATION);
            db.execSQL(SQL_CREATE_TABLE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // delete

    public long deleteLecture(Lecture lecture) {
        try {
            db.execSQL("delete from " + Tables.LECTURE.TABLE_NAME +
                              " where " + Tables.LECTURE._ID + "='"+ Long.toString(lecture.id) +"'");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void deleteTablesIfExist() {
        try {
            db.execSQL(SQL_DELETE_TABLE_TIMETABLE);
            db.execSQL(SQL_DELETE_TABLE_LECTURE);
            db.execSQL(SQL_DELETE_TABLE_LECTURER);
            db.execSQL(SQL_DELETE_TABLE_LECTURE_TIME);
            db.execSQL(SQL_DELETE_TABLE_LOCATION);
            db.execSQL(SQL_DELETE_TABLE_TIME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // get

    /* get lecturers for a specific day */
    /* should be changed to sql query */
    public ArrayList<Lecture> getLectures(int day) {
        ArrayList<Lecture> lecturesOld = getLectures();
        ArrayList<Lecture> lecturesNew = new ArrayList<>();

        for(int i = 0; i < lecturesOld.size(); i++) {
            if(lecturesOld.get(i).DAY_OF_WEEK == day) {
               lecturesNew.add(lecturesOld.get(i));
            }
        }

        return lecturesNew;
    }

    public ArrayList<Lecture> getLectures() {
        ArrayList<Lecture> lectures = new ArrayList<>();

        try {
            final String QUERY_LECTURE = "SELECT * FROM " + Tables.LECTURE.TABLE_NAME
                    + " a INNER JOIN " + Tables.LOCATION.TABLE_NAME + " b ON a." + Tables.LECTURE.COLUMN_LOCATION_ID + "=b._ID"
                    + " INNER JOIN " + Tables.LECTURER.TABLE_NAME + " c ON a." + Tables.LECTURE.COLUMN_LECTURER_ID + "=c._ID"
                    + " INNER JOIN " + Tables.LECTURE_TIME.TABLE_NAME + " d ON a." + Tables.LECTURE.COLUMN_LECTURE_TIME_ID + "=d._ID";

            Cursor cursor = db.rawQuery(QUERY_LECTURE, null);

            while (cursor.moveToNext()) {
                Lecture lecture = new Lecture();

                lecture.id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE._ID));
                lecture.title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_TITLE));
                lecture.DAY_OF_WEEK = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_DAY_OF_WEEK));

                /* location */
                long location_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_LOCATION_ID));
                String house = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_HOUSE));
                String room = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LOCATION.COLUMN_ROOM));

                lecture.location = new Location(location_id, house, room);

                /* lecturer */
                long lecturer_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_LECTURER_ID));
                String forename = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_FORENAME));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_SURNAME));
                String academic_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_ACADEMIC_TITLE));
                String mail = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_MAIL));
                String telephone = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_TELEPHONE));

                lecture.lecturer = (new Lecturer(lecturer_id, forename, surname, academic_title, mail, telephone));

                /* lecture_time */
                long lecture_time_id = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE.COLUMN_LECTURE_TIME_ID));
                long begin = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE_TIME.COLUMN_BEGIN));
                long end = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.LECTURE_TIME.COLUMN_END));

                lecture.lectureTime = new LectureTime(lecture_time_id, begin, end);

                lectures.add(lecture);
            }

            cursor.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 0; i < lectures.size(); i++)
            Log.e("BLA", lectures.get(i).title);
        return lectures;
    }

    /* check if an Lecturer is already saved in the database */
    public Boolean existsLecturer(Lecturer lecturer) {
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

    public ArrayList<Lecturer> getLecturers() {
        ArrayList lecturers = new ArrayList<Lecturer>();

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
                Log.e("bla2", forename);

                String surname = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_SURNAME));
                String academic_title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_ACADEMIC_TITLE));
                String mail = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_MAIL));
                String telephone = cursor.getString(cursor.getColumnIndexOrThrow(Tables.LECTURER.COLUMN_TELEPHONE));

                lecturers.add(new Lecturer(id, forename, surname, academic_title, mail, telephone));
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
                    Tables.LOCATION.COLUMN_ROOM
            };

            String selection = Tables.LOCATION.COLUMN_HOUSE + " = ? AND " +
                    Tables.LOCATION.COLUMN_ROOM + " = ? ";
            String[] selectionArgs = {location.house, location.room};

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
                    Tables.LOCATION.COLUMN_ROOM
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

                locations.add(new Location(id, house, room));
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

}