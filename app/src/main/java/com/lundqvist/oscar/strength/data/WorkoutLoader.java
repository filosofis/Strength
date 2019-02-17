package com.lundqvist.oscar.strength.data;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

public class WorkoutLoader extends CursorLoader {

    private WorkoutLoader(@NonNull Context context,
                          @NonNull Uri uri,
                          @Nullable String[] projection,
                          @Nullable String selection,
                          @Nullable String[] selectionArgs,
                          @Nullable String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
        System.out.println("Adapter uri: " + uri);
    }

    public static WorkoutLoader getWorkout(Context context, int itemId){
        return new WorkoutLoader(context,
                Contract.makeUriForWorkout(itemId),
                null,
                null,
                null,
                null);
    }

    public static WorkoutLoader getCompletedWorkout(Context context, long from, long to){
        return new WorkoutLoader(
                context,
                Contract.makeUriForCompleted(),
                null,
                null,
                new String[]{Long.toString(from), Long.toString(to)}, Contract.ExerciseEntry.COLUMN_COMPLETED);
    }
}
