package com.lundqvist.oscar.strength.data;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.loader.content.CursorLoader;

public class WorkoutLoader extends CursorLoader {

    private WorkoutLoader(@NonNull Context context,
                         @NonNull Uri uri) {
        super(context, uri, null, null, null, null);
        System.out.println("Adapter uri: " + uri);
    }

    public static WorkoutLoader getWorkout(Context context, int itemId){
        return new WorkoutLoader(context, Contract.makeUriForWorkout(itemId));
    }

}
