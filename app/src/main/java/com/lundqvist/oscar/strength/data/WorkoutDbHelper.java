package com.lundqvist.oscar.strength.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.lundqvist.oscar.strength.data.Contract.ExerciseEntry;

/**
 * Created by Oscar on 2018-01-29.
 */

public class WorkoutDbHelper extends SQLiteOpenHelper {


    public WorkoutDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_EXERCISE_TABLE = "CREATE TABLE " +
                ExerciseEntry.TABLE_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ExerciseEntry.COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
                ExerciseEntry.COLUMN_WEIGHT + " INTEGER, " +
                ExerciseEntry.COLUMN_SETS + " INTEGER NOT NULL, " +
                ExerciseEntry.COLUMN_REPS + " INTEGER NOT NULL, " +
                ExerciseEntry.COLUMN_WORKOUT + "INTEGER NOT NULL)";
        db.execSQL(SQL_CREATE_EXERCISE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + WorkoutEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME);
        onCreate(db);
    }
}
