package com.lundqvist.oscar.strength.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Oscar on 2018-02-05.
 */

public class WorkoutProvider extends ContentProvider {

    private WorkoutDbHelper wDbHelper;
    private static final int EXERCISE = 100;
    private static final int WORKOUT = 200;
    static final String DBNAME = "workout.db";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.MATCH_EXERCISE, EXERCISE);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.MATCH_WORKOUT, WORKOUT);
    }

    @Override
    public boolean onCreate() {
        wDbHelper = new WorkoutDbHelper(
                getContext(),
                DBNAME,
                null,
                10
        );
        return true;
    }

    @Nullable
    @Override
    public Cursor query(
            @NonNull Uri uri,
            @Nullable String[] projection,
            @Nullable String selection,
            @Nullable String[] selectionArgs,
            @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db = wDbHelper.getReadableDatabase();
        switch (sUriMatcher.match(uri)){
            case WORKOUT:
                cursor = db.query(
                        Contract.ExerciseEntry.TABLE_NAME,
                        projection,
                        Contract.ExerciseEntry.COLUMN_WORKOUT + " = ?",
                        new String[]{Contract.getWorkoutFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }
        Context context = getContext();
        if (context != null){
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = wDbHelper.getReadableDatabase();
        db.insertOrThrow(Contract.ExerciseEntry.TABLE_NAME, null, values);
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = wDbHelper.getReadableDatabase();
        clearProgram();
        db.beginTransaction();
        int returnCount = 0;
        try{
            for(ContentValues value : values){
                System.out.println(value.toString());
                db.insert(Contract.ExerciseEntry.TABLE_NAME, null, value);
                returnCount++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        switch(method){
            case "clearProgram":
                System.out.println("deleted rows: " + clearProgram());
                break;
        }
        return null;
    }

    /**
     * Deletes all uncompleted workouts and resets currentWorkout to 1
     * @return deleted rows
     */
    private int clearProgram(){
        final SQLiteDatabase db = wDbHelper.getWritableDatabase();
        SharedPreferences preferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentWorkout", 1);
        editor.apply();
        return db.delete(
                Contract.ExerciseEntry.TABLE_NAME,
                Contract.ExerciseEntry.COLUMN_WORKOUT +">" + 0,
                null);
    }


    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }


    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = wDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case WORKOUT:
                Calendar cal = Calendar.getInstance();
                long time = cal.getTimeInMillis();
                System.out.println(time);
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.ExerciseEntry.COLUMN_COMPLETED, time);
                //Date date = new Date(time);
                //String formattedDate = DateFormat.getDateInstance().format(date);
                //System.out.println("Completed on " + formattedDate);
                //System.out.println("Uri + " + Contract.getWorkoutFromUri(uri));

                return db.update(
                        Contract.ExerciseEntry.TABLE_NAME,
                        contentValues,
                        Contract.ExerciseEntry.COLUMN_WORKOUT + " = ?",
                        new String[]{Contract.getWorkoutFromUri(uri)}
                );
            case EXERCISE:
                return db.update(
                        Contract.ExerciseEntry.TABLE_NAME,
                        values,
                        Contract.ExerciseEntry.COLUMN_WORKOUT + " = ?"
                        + " AND " + Contract.ExerciseEntry.COLUMN_REPS + " = ?",
                        new String[]{Contract.getExerciseFromUri(uri),"-1"}
                        );
        }
        return 0;
    }
}
