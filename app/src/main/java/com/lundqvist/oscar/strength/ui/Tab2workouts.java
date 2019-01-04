package com.lundqvist.oscar.strength.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;
import com.lundqvist.oscar.strength.data.WorkoutLoader;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab2workouts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        System.out.println("Load Started");
        return WorkoutLoader.getWorkout(getContext(), id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        System.out.println("Load Finished");
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(cursor);
        recyclerView.setAdapter(workoutAdapter);
        System.out.println("Load cursor " + cursor.getCount());
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        System.out.println("Load Reset");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2workouts, container, false);
        initLoader();
        recyclerView = rootView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return rootView;
    }
    public void initLoader(){
        getLoaderManager().initLoader(1, null, this);
    }
}
