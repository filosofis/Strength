package com.lundqvist.oscar.strength.ui;

import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab2workouts extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2workouts, container, false);
        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button click");
                System.out.println("Uri: " + Contract.makeUriForWorkout("2"));
                Cursor cursor = getContext().getContentResolver().query(
                        Contract.makeUriForWorkout("1"),
                        null,
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();
                System.out.println(" " + cursor.getString(1));
                cursor.close();
            }
        });
        return rootView;
    }
}
