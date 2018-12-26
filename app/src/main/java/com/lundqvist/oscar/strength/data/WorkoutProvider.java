package com.lundqvist.oscar.strength.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_EXERCISE, EXERCISE);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_WORKOUT, WORKOUT);
    }

    @Override
    public boolean onCreate() {
        wDbHelper = new WorkoutDbHelper(
                getContext(),
                DBNAME,
                null,
                1
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
        db.beginTransaction();
        int returnCount = 0;
        try{
            for(ContentValues value : values){
                db.insert(Contract.ExerciseEntry.TABLE_NAME, null, value);
                returnCount++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return returnCount;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
