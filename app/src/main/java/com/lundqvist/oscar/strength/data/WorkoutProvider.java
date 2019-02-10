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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lundqvist.oscar.strength.model.Exercise;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.function.ToDoubleBiFunction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;

/**
 * Created by Oscar on 2018-02-05.
 */

public class WorkoutProvider extends ContentProvider {

    private WorkoutDbHelper wDbHelper;
    private static final int EXERCISE = 100;
    private static final int WORKOUT = 200;
    private static final int COMPLETED = 300;
    static final String DBNAME = "workout.db";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static{
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.MATCH_EXERCISE, EXERCISE);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.MATCH_WORKOUT, WORKOUT);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_COMPLETED, COMPLETED);
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
            case COMPLETED:
                cursor = db.query(
                        Contract.ExerciseEntry.TABLE_NAME,
                        projection,
                        Contract.ExerciseEntry.COLUMN_COMPLETED +
                        " BETWEEN ? AND ?",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
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
        //clearProgram();
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
            case "insertTestData":
                System.out.println("Completed workouts: " + insertTestData());
                break;
        }
        return null;
    }

    /**
     * Fills database with a completed juggernaut program ending yesterday for testing purposes
     * @return number of completed workouts
     */
    private int insertTestData(){
        System.out.println("Attempting to insert Test data....");
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference()
                .child("programs-data").child("juggernaut");
        final ArrayList<ContentValues> exerciseCVs = new ArrayList<>();
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("On Data Changed");
                Exercise exercise;
                int dateDelta;
                Calendar cal = Calendar.getInstance();
                long now = cal.getTimeInMillis();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    exercise = ds.getValue(Exercise.class);
                    if(exercise.getNote() == null){
                        exercise.setNote("0");
                    }
                    ContentValues cv = new ContentValues();
                    //Set workout to -1 to mark as not in the current program
                    cv.put(Contract.ExerciseEntry.COLUMN_WORKOUT, -1);
                    cv.put(Contract.ExerciseEntry.COLUMN_EXERCISE_NAME, exercise.getName());
                    cv.put(Contract.ExerciseEntry.COLUMN_WEIGHT, exercise.getWeight());
                    cv.put(Contract.ExerciseEntry.COLUMN_SETS, exercise.getSets());
                    cv.put(Contract.ExerciseEntry.COLUMN_TIME, exercise.getTime());
                    cv.put(Contract.ExerciseEntry.COLUMN_NOTE, exercise.getNote());

                    //AMRAP result
                    if(exercise.getReps() == -1){
                        exercise.setReps(3);
                    }
                    cv.put(Contract.ExerciseEntry.COLUMN_REPS, exercise.getReps());

                    cal.setTimeInMillis(now);
                    dateDelta = -113+exercise.getWorkout();
                    cal.add(Calendar.DAY_OF_YEAR, dateDelta);
                    cv.put(Contract.ExerciseEntry.COLUMN_COMPLETED, cal.getTimeInMillis());
                    exerciseCVs.add(cv);
                    System.out.println(cal.getDisplayName(Calendar.DAY_OF_YEAR, Calendar.SHORT, Locale.getDefault()));
                }
                int entries = getContext().getContentResolver().bulkInsert(Contract.BASE_CONTENT_URI,
                        exerciseCVs.toArray(new ContentValues[exerciseCVs.size()]));
                System.out.println("Inserted rows: " + entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Timber.w(databaseError.toException(), "loadPost:onCancelled");
                // [START_EXCLUDE]
                System.out.println("Failed to load post");
                // [END_EXCLUDE]
            }

        });
        return 0;
    }

    /**
     * Deletes all uncompleted workouts and resets currentWorkout to 1
     * also changes their COLUMN_WORKOUT to -1
     * @return deleted rows
     */
    private int clearProgram(){
        final SQLiteDatabase db = wDbHelper.getWritableDatabase();
        SharedPreferences preferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentWorkout", 1);
        editor.apply();

        //Change all completed workouts of the current program to workout=-1
        ContentValues values = new ContentValues();
        values.put(Contract.ExerciseEntry.COLUMN_WORKOUT, -1);
        db.update(
                Contract.ExerciseEntry.TABLE_NAME,
                values,
                Contract.ExerciseEntry.COLUMN_TIME + " NOT LIKE ?"
                + " AND " + Contract.ExerciseEntry.COLUMN_WORKOUT +" > ? ",
                new String[]{"0", "0"}
                );
        return db.delete(
                Contract.ExerciseEntry.TABLE_NAME,
                Contract.ExerciseEntry.COLUMN_WORKOUT +" > ?" ,
                new String[]{"0"});
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
