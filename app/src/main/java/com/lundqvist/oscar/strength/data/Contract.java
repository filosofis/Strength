package com.lundqvist.oscar.strength.data;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by Oscar on 2018-02-05.
 */

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.lundqvist.oscar.strength";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WORKOUT = "exercise/*";
    public static final String PATH_EXERCISE = "exercise";
    public static final String REST_DAY = "rest";


    public static final class ExerciseEntry implements BaseColumns {
        public static final Uri URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();
        public static final String TABLE_NAME = "exercise";
        public static final String COLUMN_EXERCISE_NAME = "exerciseName";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_WORKOUT = "workout_ID";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_COMPLETED = "completed";
        public static final int INDEX_EXERCISE_NAME = 1;
        public static final int INDEX_WEIGHT = 2;
        public static final int INDEX_SETS = 3;
        public static final int INDEX_REPS = 4;
        public static final int INDEX_TIME = 5;
        public static final int INDEX_NOTE = 6;
        public static final int INDEX_COMPLETED = 7;
        public static final int INDEX_WORKOUT = 8;
    }

    static String getWorkoutFromUri(Uri queryUri){
        return queryUri.getLastPathSegment();
    }

    public static Uri makeUriForWorkout(int workout){
        return ExerciseEntry.URI.buildUpon().appendPath(Integer.toString(workout)).build();
    }

}
