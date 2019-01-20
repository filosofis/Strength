package com.lundqvist.oscar.strength.ui;


import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab1home extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1home, container, false);
        Button button = rootView.findViewById(R.id.button2);
        final Calendar cal = Calendar.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click");
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                long from = cal.getTimeInMillis();
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                long to = cal.getTimeInMillis();
                System.out.println("From: " + from + " to: " + to);
                getWorkout(from, to);
            }
        });
        Button button2 = rootView.findViewById(R.id.button3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.roll(Calendar.DAY_OF_YEAR, -1);
                System.out.println("Date: " + cal.get(Calendar.DAY_OF_MONTH));
            }
        });
        return rootView;
    }
    public void getWorkout(long from, long to){
        Cursor cursor = getContext().getContentResolver().query(
                Contract.makeUriForCompleted(),
                null,
                null,
                new String[]{Long.toString(from), Long.toString(to)},
                null
        );
        cursor.moveToFirst();
        do{
            System.out.println("Name :" + cursor.getString(Contract.ExerciseEntry.INDEX_EXERCISE_NAME));
            System.out.println("Sets :" + cursor.getInt(Contract.ExerciseEntry.INDEX_SETS));
            System.out.println("Reps :" + cursor.getInt(Contract.ExerciseEntry.INDEX_REPS));
        }while(cursor.moveToNext());
        cursor.close();
    }
}
