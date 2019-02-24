package com.lundqvist.oscar.strength.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lundqvist.oscar.strength.NewAppWidget;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.Utility;
import com.lundqvist.oscar.strength.data.Contract;
import com.lundqvist.oscar.strength.data.WorkoutAdapter;
import com.lundqvist.oscar.strength.data.WorkoutLoader;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab2workouts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        WorkoutAdapter.AdapterCallback {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private int workoutId;
    private BottomNavigationView bottomNavigationView;
    private Menu menu;
    private SharedPreferences sharedPref;
    private String amrap;
    private String name;
    private int weight;

    @Override
    public void textInputValue(String amrap, int weight, String name) {
        this.amrap = amrap;
        int cw = sharedPref.getInt("currentWorkout", 1);
        System.out.println("textInputValue  " + amrap + " WorkoutID : " +workoutId + " "+ cw);
        if(amrap.equals("disable")) {
            System.out.println("Amrap, disabling complete button");
            this.weight = weight;
            this.name = name;
            menu.getItem(1).setEnabled(false);
        }else if(amrap.length()>0 && !amrap.equals("notAmrap") && cw == workoutId){
            System.out.println("Amrap > 1, enabling complete button");
            menu.getItem(1).setEnabled(true);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        System.out.println("Load Started");
        return WorkoutLoader.getWorkout(getContext(), id);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        //System.out.println("Load Finished");
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(cursor, getContext(), this);
        recyclerView.invalidate();
        recyclerView.setAdapter(workoutAdapter);
        System.out.println("Load cursor " + cursor.getCount());
        MenuItem completeButton = menu.getItem(1);
        MenuItem backButton = menu.getItem(0);
        MenuItem forwardButton = menu.getItem(2);
        int currentWorkout = sharedPref.getInt("currentWorkout", 1);
        System.out.println("Workout id: " + workoutId + " Current Workout: " + currentWorkout);

        /*if(workoutAdapter.hasAmrap){
            System.out.println("Yup it has an AMRAP");
        }*/
        if(workoutId == currentWorkout && cursor.getCount() != 0){
            completeButton.setEnabled(true);
        }else{
            completeButton.setEnabled(false);
        }
        if(workoutId == 1){
            backButton.setEnabled(false);
        }else{
            backButton.setEnabled(true);
        }
        if(cursor.getCount()<1){
            forwardButton.setEnabled(false);
        }else{
            forwardButton.setEnabled(true);
        }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView, boolean right) {
        //System.out.println("Attempting to animate...");
        final Context context = recyclerView.getContext();
        int animationId;
        if(right){
            //System.out.println("Slide right");
            animationId = R.anim.layout_animation_slide_right;
        }else{
            //System.out.println("Slide left");
            animationId = R.anim.layout_animation_slide_left;
        }
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, animationId);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        recyclerView.setAdapter(null);
        //System.out.println("Load Reset");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2workouts, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        Context context = getContext();
        sharedPref = context.getSharedPreferences(Contract.SHARED_REPFS, Context.MODE_PRIVATE);
        workoutId = sharedPref.getInt("currentWorkout", 1);
        //System.out.println("Creating view with workoutID " + workoutId);
        initLoader(workoutId);
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
                                    runLayoutAnimation(recyclerView, false);
                                }else{
                                    System.out.println("Back wall");
                                }
                                break;
                            case R.id.button_complete:
                                complete();
                                //runLayoutAnimation(recyclerView);
                                break;
                            case R.id.button_forward:
                                workoutId++;
                                System.out.println("Forward " + workoutId);
                                initLoader(workoutId);
                                runLayoutAnimation(recyclerView, true);
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
        System.out.println("AMRAP " + amrap);
        SharedPreferences preferences = getContext().getSharedPreferences(Contract.SHARED_REPFS, Context.MODE_PRIVATE);
        int currentWorkout = preferences.getInt(Contract.CURRENT_WORKOUT, 1);
        System.out.println(" Lines Completed " + getContext().getContentResolver().update(
                Contract.makeUriForWorkout(workoutId),
                null, null, null));

        if(!amrap.equals("notAmrap")) {
            System.out.println("Completeing AMRAP with weight: " + weight +" and name: " + name);
            Utility utility = new Utility();
            int calculatedWeight=0;
            SharedPreferences.Editor editor = preferences.edit();
            switch(name){
                case "Squat":
                    calculatedWeight = (weight*preferences.getInt(Contract.RM_SQUAT, 1))/100;
                    editor.putInt("squatRM", utility.oneRepMaxCalc(Integer.parseInt(amrap), calculatedWeight));
                    break;
                case "Bench":
                    calculatedWeight = (weight*preferences.getInt(Contract.RM__BENCH, 1))/100;
                    utility.oneRepMaxCalc(Integer.parseInt(amrap), calculatedWeight);
                    editor.putInt("squatRM", utility.oneRepMaxCalc(Integer.parseInt(amrap), calculatedWeight));
                    break;
                case "Deadlift":
                    calculatedWeight = (weight*preferences.getInt(Contract.RM__DEAD, 1))/100;
                    utility.oneRepMaxCalc(Integer.parseInt(amrap), calculatedWeight);
                    editor.putInt("squatRM", utility.oneRepMaxCalc(Integer.parseInt(amrap), calculatedWeight));
                    break;
                case "Military Press":
                    calculatedWeight = (weight*preferences.getInt(Contract.RM__PRESS, 1))/100;
                    utility.oneRepMaxCalc(Integer.parseInt(amrap), calculatedWeight);
                    editor.putInt("squatRM", utility.oneRepMaxCalc(Integer.parseInt(amrap), calculatedWeight));
                    break;
            }
            editor.apply();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.ExerciseEntry.COLUMN_REPS, amrap);
            //Change weight to the actual weight used instead of programmed % to track progression
            //contentValues.put(Contract.ExerciseEntry.COLUMN_WEIGHT, calculatedWeight);
            System.out.println(" Lines Updated " + getContext().getContentResolver().update(
                    Contract.makeUriForExercise(workoutId),
                    contentValues, null, null));
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentWorkout", currentWorkout+1);
        editor.apply();
        updateWidget();
        getLoaderManager().restartLoader(workoutId, null, this);
    }
    private void initLoader(int workoutId){
        getLoaderManager().initLoader(workoutId, null, this);
    }

    private void updateWidget(){
        Intent intent = new Intent(getContext(), NewAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(
                new ComponentName(getActivity().getApplication(), NewAppWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);
    }
}