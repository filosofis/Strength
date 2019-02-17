package com.lundqvist.oscar.strength.ui;

import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;
import com.lundqvist.oscar.strength.data.WorkoutLoader;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static java.text.DateFormat.getTimeInstance;


/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab1home extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private LineChart chart;
    private List<Entry> bodyWeightEntries = new ArrayList<>();
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;

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
        cursor.moveToFirst();
        int counter=0;
        int reps=0;
        int sets;
        int weight=0;
        long date;
        Calendar cal = Calendar.getInstance();
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
                System.out.println("Counter: "+ counter + " reps: " + reps);
                System.out.println("Weight : " + weight);
                System.out.println("Date : " + date);
                counter++;
                cal.setTimeInMillis(date);
                secondEntries.add(new Entry(date, weight * 2));
                entries.add(new Entry(date, reps));
                reps = 0;
            } while (cursor.moveToNext());

            LineDataSet dataSet = new LineDataSet(entries, "Volume");
            LineData lineData = new LineData(dataSet);
            lineData.setDrawValues(false);

            LineDataSet dataSet2 = new LineDataSet(secondEntries, "Intensity");
            LineData lineData2 = new LineData(dataSet2);
            lineData2.setDrawValues(false);

            LineDataSet dataSet3 = new LineDataSet(bodyWeightEntries, "Body Weight");
            LineData lineData3 = new LineData(dataSet3);
            lineData3.setDrawValues(false);

            dataSet.setFillColor(R.color.secondaryColor);
            dataSet.setColor(ContextCompat.getColor(getContext(), R.color.secondaryLightColor));
            dataSet.setLineWidth(2f);
            dataSet.setDrawCircles(false);

            dataSet3.setColor(ContextCompat.getColor(getContext(), R.color.primaryDarkColor));
            dataSet3.setLineWidth(2f);
            dataSet3.setDrawCircles(false);

            chart.getXAxis().setDrawGridLines(false);
            chart.getXAxis().setDrawLabels(false);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisLeft().setDrawLabels(true);
            chart.getAxisLeft().setDrawZeroLine(true);
            chart.getAxisRight().setDrawGridLines(false);
            chart.getAxisRight().setDrawLabels(false);
            chart.getDescription().setEnabled(false);
            chart.getLegend().setEnabled(true);
            chart.getLegend().setTextSize(16f);

            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
            lineDataSets.add(dataSet);
            lineDataSets.add(dataSet2);
            lineDataSets.add(dataSet3);
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
                cal.add(Calendar.DAY_OF_YEAR, -7);
                long from = cal.getTimeInMillis();
                getWorkout(from, to, 4);
            }
        });
        return rootView;
    }

    private void getWorkout(long from, long to, int id){
        System.out.println("From " + from + " to" + to);
        Bundle args= new Bundle();
        args.putLong("from", from);
        args.putLong("to", to);
        getBodyWeightFromFit(from, to);
        getLoaderManager().initLoader(id, args, this);
    }

    public void getBodyWeightFromFit(long from, long to){
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(getContext()),
                    fitnessOptions);
        } else {
            System.out.println("Accessing Google Fit....");
            accessGoogleFit(from, to);
        }
    }

    private void accessGoogleFit(long from, long to){
        /*DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        */
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_WEIGHT)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(from, to, TimeUnit.MILLISECONDS)
                .build();
        Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getContext()))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        System.out.println("onSucess()");
                        printFitHistory(dataReadResponse);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("onFailure()" + e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        System.out.println("onComplete()");
                    }
                });
    }

    private void printFitHistory(DataReadResponse response){
        DateFormat dateFormat = getTimeInstance();
        List<Entry> entries = new ArrayList<>();
        if (response.getBuckets().size() > 0) {
            System.out.println("Nr of Buckets: " + response.getBuckets().size());
            int counter = 0;
            for (Bucket bucket : response.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                counter++;
                for(DataSet dataSet : dataSets){
                    for (DataPoint dp : dataSet.getDataPoints()) {
                        System.out.println("Type : " + dp.getDataType());
                        System.out.println("Start : " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                        System.out.println("End : " + dp.getEndTime(TimeUnit.MILLISECONDS));
                        System.out.println("Type : " + dp.getDataType());
                        for (Field field : dp.getDataType().getFields()) {
                            System.out.println(
                                    "Field " + field.getName()
                                    + " Value: " + dp.getValue(field)
                                            + "Counter: "  + counter);
                            entries.add(new Entry(dp.getStartTime(TimeUnit.MILLISECONDS), dp.getValue(field).asFloat()));
                        }
                    }
                }
            }
            this.bodyWeightEntries = entries;

        }else if(response.getDataSets().size() > 0){
            System.out.println("Single Bucket");
            for (DataSet dataSet : response.getDataSets()) {
                for (DataPoint dp : dataSet.getDataPoints()) {
                    System.out.println("Type : " + dp.getDataType());
                    System.out.println("Start : " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                    System.out.println("End : " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                    for (Field field : dp.getDataType().getFields()) {
                        //System.out.println("Field " + field.getName() + " Value: " + dp.getValue(field));
                        //dp.getValue(field).asFloat();
                    }
                }
            }
        }
    }
}