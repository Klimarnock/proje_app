package com.example.fitnesstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness_tracker.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAthleteTable =
                "CREATE TABLE athletes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "age INTEGER, " +
                        "gender TEXT, " +
                        "weight REAL, " +
                        "height REAL, " +
                        "primary_sport TEXT)";

        String createActivityTable =
                "CREATE TABLE activities (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "athlete_name TEXT, " +
                        "type TEXT, " +
                        "date TEXT, " +
                        "duration INTEGER, " +
                        "calories INTEGER, " +
                        "distance REAL, " +
                        "laps INTEGER, " +
                        "avg_speed REAL)";

        db.execSQL(createAthleteTable);
        db.execSQL(createActivityTable);

        db.execSQL("INSERT INTO athletes (name, age, gender, weight, height, primary_sport) VALUES ('Kenan Güzel',22,'Male',74.5,180,'Running')");
        db.execSQL("INSERT INTO athletes (name, age, gender, weight, height, primary_sport) VALUES ('Sare Yılmaz',21,'Female',60,168,'Swimming')");
        db.execSQL("INSERT INTO athletes (name, age, gender, weight, height, primary_sport) VALUES ('Alex Carter',22,'Male',74.5,180,'Running')");
        db.execSQL("INSERT INTO athletes (name, age, gender, weight, height, primary_sport) VALUES ('Emma Johnson',25,'Female',60,168,'Swimming')");
        db.execSQL("INSERT INTO athletes (name, age, gender, weight, height, primary_sport) VALUES ('Michael Brown',28,'Male',82,185,'Cycling')");

        db.execSQL("INSERT INTO activities (athlete_name,type,date,duration,calories,distance,laps,avg_speed) VALUES ('Kenan Güzel','Running','2026-05-01',45,410,5.2,0,0)");
        db.execSQL("INSERT INTO activities (athlete_name,type,date,duration,calories,distance,laps,avg_speed) VALUES ('Kenan Güzel','Cycling','2026-05-02',75,630,18.5,0,24.7)");
        db.execSQL("INSERT INTO activities (athlete_name,type,date,duration,calories,distance,laps,avg_speed) VALUES ('Kenan Güzel','Running','2026-05-04',35,320,4.1,0,0)");

        db.execSQL("INSERT INTO activities (athlete_name,type,date,duration,calories,distance,laps,avg_speed) VALUES ('Sare Yılmaz','Swimming','2026-05-03',50,520,0,30,0)");
        db.execSQL("INSERT INTO activities (athlete_name,type,date,duration,calories,distance,laps,avg_speed) VALUES ('Sare Yılmaz','Swimming','2026-05-06',60,600,0,40,0)");
        db.execSQL("INSERT INTO activities (athlete_name,type,date,duration,calories,distance,laps,avg_speed) VALUES ('Sare Yılmaz','Running','2026-05-07',55,500,6.8,0,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS athletes");
        db.execSQL("DROP TABLE IF EXISTS activities");
        onCreate(db);
    }

    public boolean insertAthlete(String name, int age, String gender,
                                 double weight, double height, String primarySport) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        values.put("gender", gender);
        values.put("weight", weight);
        values.put("height", height);
        values.put("primary_sport", primarySport);

        long result = db.insert("athletes", null, values);

        return result != -1;
    }

    public boolean insertActivity(String athleteName, String type, String date, int duration,
                                  int calories, double distance,
                                  int laps, double avgSpeed) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("athlete_name", athleteName);
        values.put("type", type);
        values.put("date", date);
        values.put("duration", duration);
        values.put("calories", calories);
        values.put("distance", distance);
        values.put("laps", laps);
        values.put("avg_speed", avgSpeed);

        long result = db.insert("activities", null, values);

        return result != -1;
    }

    public boolean athleteExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM athletes WHERE name = ?",
                new String[]{name}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

    public Cursor getActivitiesByAthlete(String athleteName) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM activities WHERE athlete_name = ? ORDER BY id DESC",
                new String[]{athleteName}
        );
    }

    public int getTotalWorkoutCount(String athleteName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM activities WHERE athlete_name = ?",
                new String[]{athleteName}
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }

    public int getTotalCalories(String athleteName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(calories) FROM activities WHERE athlete_name = ?",
                new String[]{athleteName}
        );

        int total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }

        cursor.close();

        return total;
    }

    public int getTotalDuration(String athleteName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(duration) FROM activities WHERE athlete_name = ?",
                new String[]{athleteName}
        );

        int total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }

        cursor.close();

        return total;
    }

    public void deleteActivitiesByAthlete(String athleteName) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(
                "activities",
                "athlete_name = ?",
                new String[]{athleteName}
        );
    }
    public Cursor getGlobalActivityFeed() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM activities ORDER BY id DESC",
                null
        );
    }
}