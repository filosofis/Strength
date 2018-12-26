package com.lundqvist.oscar.strength.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Oscar on 2018-02-05.
 */

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.lundqvist.oscar.strength";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WORKOUT = "workout";
    public static final String PATH_EXERCISE = "exercise";


    public static final class ExerciseEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();
        public static final String TABLE_NAME = "exercise";
        public static final String COLUMN_EXERCISE_NAME = "exerciseName";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_WORKOUT = "workout";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_NOTE = "note";
    }

    static String getWorkoutFromUri(Uri queryUri){
        return queryUri.getLastPathSegment();
    }
}
