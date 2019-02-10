package com.lundqvist.oscar.strength.ui;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.lundqvist.oscar.strength.NewAppWidget;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;
import com.lundqvist.oscar.strength.data.WorkoutLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab1home extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    LineChart chart;

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        long from = args.getLong("from",0);
        long to = args.getLong("to",0);
        return WorkoutLoader.getCompletedWorkout(getContext(), from, to);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        chart.invalidate();
        List<Entry> entries = new ArrayList<>();
        List<Entry> secondEntries = new ArrayList<>();
        List<Entry> thirdEntries = new ArrayList<>();
        cursor.moveToFirst();
        int counter=0;
        int reps=0;
        int sets;
        int weight=0;
        long date;
        if(cursor.getCount()<2){
            chart.setVisibility(View.INVISIBLE);
        }else {
            chart.setVisibility(View.VISIBLE);
            do {
                date = cursor.getLong(Contract.ExerciseEntry.INDEX_COMPLETED);
                while (date == cursor.getLong(Contract.ExerciseEntry.INDEX_COMPLETED) && !cursor.isLast()) {
                    sets = cursor.getInt(Contract.ExerciseEntry.INDEX_SETS);
                    reps += sets * cursor.getInt(Contract.ExerciseEntry.INDEX_REPS);
                    if (cursor.getInt(Contract.ExerciseEntry.INDEX_WEIGHT) != 0) {
                        weight = cursor.getInt(Contract.ExerciseEntry.INDEX_WEIGHT);
                    }
                    cursor.moveToNext();
                }
                //System.out.println("Counter: "+ counter + " reps: " + reps);
                //System.out.println("Weight : " + weight);
                secondEntries.add(new Entry(counter, weight * 2));
                entries.add(new Entry(counter++, reps));
                reps = 0;
            } while (cursor.moveToNext());

            LineDataSet dataSet = new LineDataSet(entries, "Volume");
            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);

            LineDataSet dataSet2 = new LineDataSet(secondEntries, "Intensity");
            LineData lineData2 = new LineData(dataSet2);
            lineData2.setDrawValues(false);

            //dataSet.setFillColor(R.color.secondaryColor);
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.secondaryLightColor));
            dataSet.setLineWidth(2f);
            dataSet.setDrawCircles(false);

            chart.getXAxis().setDrawGridLines(false);
            chart.getXAxis().setDrawLabels(false);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisLeft().setDrawLabels(false);
            chart.getAxisLeft().setDrawZeroLine(true);
            chart.getAxisRight().setDrawGridLines(false);
            chart.getAxisRight().setDrawLabels(false);
            chart.setData(lineData);
            chart.getDescription().setEnabled(false);
            chart.getLegend().setEnabled(true);
            chart.getLegend().setTextSize(16f);

            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            lineDataSets.add(dataSet);
            lineDataSets.add(dataSet2);
            chart.setData(new LineData(lineDataSets));
            chart.invalidate();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1home, container, false);
        Button button2 = rootView.findViewById(R.id.button2);
        this.chart = rootView.findViewById(R.id.chart);

        TextView currentProgramText = rootView.findViewById(R.id.currentProgramText);
        TextView currentCompletion = rootView.findViewById(R.id.currentCompletion);
        SharedPreferences prefs = getContext().getSharedPreferences(Contract.SHARED_REPFS, MODE_PRIVATE);
        String title = prefs.getString(Contract.CURRENT_PROGRAM, "No Active Program");
        int currentWorkout = prefs.getInt(Contract.CURRENT_WORKOUT, 1);
        int currentLength = prefs.getInt(Contract.CURRENT_LENGTH, 0)*7;
        String completion = String.valueOf(currentWorkout) + " / " + String.valueOf(currentLength);

        currentProgramText.setText(title);
        currentCompletion.setText(completion);

        //Initialize the chart to past 30days
        Calendar cal = Calendar.getInstance();
        long to = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -30);
        long from = cal.getTimeInMillis();
        System.out.println("From: " + from + " to: " + to);
        getWorkout(from, to, 1);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                long to = cal.getTimeInMillis();
                cal.add(Calendar.DAY_OF_YEAR, -30);
                long from = cal.getTimeInMillis();
                System.out.println("From: " + from + " to: " + to);
                getWorkout(from, to, 1);
            }
        });

        Button button3 = rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                long to = cal.getTimeInMillis();
                cal.add(Calendar.DAY_OF_YEAR, -60);
                long from = cal.getTimeInMillis();
                System.out.println("From: " + from + " to: " + to);
                getWorkout(from, to, 2);
            }
        });

        Button button4 = rootView.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                long to = cal.getTimeInMillis();
                cal.add(Calendar.DAY_OF_YEAR, -120);
                long from = cal.getTimeInMillis();
                getWorkout(from, to, 3);
            }
        });

        Button button5 = rootView.findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                long to = cal.getTimeInMillis();
                cal.add(Calendar.YEAR, -1);
                long from = cal.getTimeInMillis();
                getWorkout(from, to, 4);
            }
        });
        return rootView;
    }

    public void getWorkout(long from, long to, int id){
        System.out.println("From " + from + " to" + to);
        Bundle args= new Bundle();
        args.putLong("from", from);
        args.putLong("to", to);
        getLoaderManager().initLoader(id, args, this);

    }
}