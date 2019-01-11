package com.lundqvist.oscar.strength.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;
import com.lundqvist.oscar.strength.data.WorkoutLoader;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab2workouts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private int workoutId;
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private SharedPreferences sharedPref;

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        System.out.println("Load Started");
        return WorkoutLoader.getWorkout(getContext(), id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        //System.out.println("Load Finished");
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(cursor);
        recyclerView.setAdapter(workoutAdapter);
        //System.out.println("Load cursor " + cursor.getCount());
        MenuItem completeButton = menu.getItem(1);
        MenuItem backButton = menu.getItem(0);
        //MenuItem forwardButton = menu.getItem(2);
        int currentWorkout = sharedPref.getInt("currentWorkout", 1);
        System.out.println("Workout id: " + workoutId + " Current Workout: " + currentWorkout);
        if(workoutId == currentWorkout){
            completeButton.setEnabled(true);
        }else{
            completeButton.setEnabled(false);
        }
        if(workoutId == 1){
            backButton.setEnabled(false);
        }else{
            backButton.setEnabled(true);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        System.out.println("Load Reset");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2workouts, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Context context = getContext();
        sharedPref = context.getSharedPreferences(
                "prefs", Context.MODE_PRIVATE);
        workoutId = sharedPref.getInt("currentWorkout", 1);
        System.out.println("Creating view with workoutID " + workoutId);
        initLoader(1);

        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
        menu = bottomNavigationView.getMenu();
        menu.getItem(2).setCheckable(false);
        menu.getItem(0).setCheckable(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.button_back:
                                if(workoutId > 1) {
                                    workoutId--;
                                    System.out.println("Back " + workoutId);
                                    initLoader(workoutId);
                                }else{
                                    System.out.println("Back wall");
                                }
                                break;
                            case R.id.button_complete:
                                complete();
                                break;
                            case R.id.button_forward:
                                workoutId++;
                                System.out.println("Forward " + workoutId);
                                initLoader(workoutId);
                                break;
                            }
                            return true;
                        }
                    }
        );
        return rootView;
    }
    private void complete(){
        System.out.println("Completeing  " + workoutId);
        SharedPreferences preferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        int currentWorkout = preferences.getInt("currentWorkout", 1);
        System.out.println(" Lines Completed " + getContext().getContentResolver().update(
                Contract.makeUriForWorkout(workoutId),
                null, null, null));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentWorkout", currentWorkout+1);
        editor.apply();
    }
    public void initLoader(int workoutId){
        getLoaderManager().initLoader(workoutId, null, this);
    }
}