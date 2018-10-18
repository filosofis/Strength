package com.lundqvist.oscar.strength.ui;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lundqvist.oscar.strength.R;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab2workouts extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2workouts, container, false);
        return rootView;
    }
}
