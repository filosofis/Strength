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
    public static final String PATH_COMPLETED = "completed";
    public static final String MATCH_WORKOUT = "workout/*";
    public static final String MATCH_EXERCISE = "exercise/*";
    public static final String MATCH_COMPLETED = "completed/*";
    public static final String REST_DAY = "rest";

    public static final String SHARED_REPFS = "prefs";
    public static final String CURRENT_WORKOUT = "currentWorkout";
    public static final String CURRENT_PROGRAM = "currentProgram";
    public static final String CURRENT_LENGTH = "currentLength";
    public static final String RM_SQUAT = "squatRM";
    public static final String RM__BENCH = "benchRM";
    public static final String RM__DEAD = "deadRM";
    public static final String RM__PRESS = "pressRM";


    public static final class ExerciseEntry implements BaseColumns {
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

    static String getExerciseFromUri(Uri queryUri){return queryUri.getLastPathSegment();
    }
    static String getCompletedFromUri(Uri queryUri){return queryUri.getLastPathSegment();
    }

    public static Uri makeUriForWorkout(int workout){
        Uri URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORKOUT).build();
        return URI.buildUpon().appendPath(Integer.toString(workout)).build();
    }

    public static Uri makeUriForExercise(int workout){
        Uri URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXERCISE).build();
        return URI.buildUpon().appendPath(Integer.toString(workout)).build();
    }

    public static Uri makeUriForCompleted(){
        return BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMPLETED).build();
    }

}
