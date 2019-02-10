package com.lundqvist.oscar.strength.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Element;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.Utility;
import com.lundqvist.oscar.strength.data.Contract;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getTimeInstance;

public class StatsActivity extends AppCompatActivity {
    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        final Utility utility = new Utility();
        final SharedPreferences prefs = getSharedPreferences(Contract.SHARED_REPFS, MODE_PRIVATE);

        final Button saveSquat = findViewById(R.id.button_save_squat);
        final Button saveBench = findViewById(R.id.button_save_bench);
        final Button saveDead = findViewById(R.id.button_save_dead);
        final Button savePress = findViewById(R.id.button_save_press);
        final Button loadFromFit = findViewById(R.id.button_load_from_fit);

        final TextInputEditText repSquat = findViewById(R.id.repInputSquat);
        final TextInputEditText repBench = findViewById(R.id.repInputBench);
        final TextInputEditText repDead = findViewById(R.id.repInputDead);
        final TextInputEditText repPress = findViewById(R.id.repInputPress);

        final TextInputEditText weightSquat = findViewById(R.id.weightInputSquat);
        final TextInputEditText weightBench = findViewById(R.id.weightInputBench);
        final TextInputEditText weightDead = findViewById(R.id.weightInputDead);
        final TextInputEditText weightPress = findViewById(R.id.weightInputPress);

        final TextInputLayout repSquatLayout = findViewById(R.id.repSquatLayout);
        final TextInputLayout repBenchLayout = findViewById(R.id.repBenchLayout);
        final TextInputLayout repDeadLayout = findViewById(R.id.repDeadLayout);
        final TextInputLayout repPressLayout = findViewById(R.id.repPressLayout);

        final TextInputLayout weightSquatLayout = findViewById(R.id.weightSquatLayout);
        final TextInputLayout weightBenchLayout = findViewById(R.id.weightBenchLayout);
        final TextInputLayout weightDeadLayout = findViewById(R.id.weightDeadLayout);
        final TextInputLayout weightPressLayout = findViewById(R.id.weightPressLayout);

        saveSquat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String reps = repSquat.getText().toString();
                String weight = weightSquat.getText().toString();
                if(reps.isEmpty()){
                    repSquatLayout.setError("not empty");
                }else{
                    repSquatLayout.setError(null);
                }
                if(weight.isEmpty()){
                    weightSquatLayout.setError("not empty");
                }else{
                    weightSquatLayout.setError(null);
                }
                if(!reps.isEmpty() && !weight.isEmpty()){
                    int oneRepMax = utility.oneRepMaxCalc(
                            Integer.parseInt(reps),
                            Integer.parseInt(weight)
                    );
                    System.out.println("oneRepMax " + oneRepMax);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("squatRM", oneRepMax);
                    editor.apply();
                }
            }
        });
        saveBench.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String reps = repBench.getText().toString();
                String weight = weightBench.getText().toString();
                if(reps.isEmpty()){
                    repBenchLayout.setError("not empty");
                }else{
                    repBenchLayout.setError(null);
                }
                if(weight.isEmpty()){
                    weightBenchLayout.setError("not empty");
                }else{
                    weightBenchLayout.setError(null);
                }
                if(!reps.isEmpty() && !weight.isEmpty()){
                    int oneRepMax = utility.oneRepMaxCalc(
                            Integer.parseInt(reps),
                            Integer.parseInt(weight)
                    );
                    System.out.println("oneRepMax " + oneRepMax);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(Contract.RM__BENCH, oneRepMax);
                    editor.apply();
                }
            }
        });
        saveDead.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String reps = repDead.getText().toString();
                String weight = weightDead.getText().toString();
                if(reps.isEmpty()){
                    repDeadLayout.setError("not empty");
                }else{
                    repDeadLayout.setError(null);
                }
                if(weight.isEmpty()){
                    weightDeadLayout.setError("not empty");
                }else{
                    weightDeadLayout.setError(null);
                }
                if(!reps.isEmpty() && !weight.isEmpty()){
                    int oneRepMax = utility.oneRepMaxCalc(
                            Integer.parseInt(reps),
                            Integer.parseInt(weight)
                    );
                    System.out.println("oneRepMax " + oneRepMax);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(Contract.RM__DEAD, oneRepMax);
                    editor.apply();
                }
            }
        });
        savePress.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String reps = repPress.getText().toString();
                String weight = weightPress.getText().toString();
                if(reps.isEmpty()){
                    repPressLayout.setError("not empty");
                }else{
                    repPressLayout.setError(null);
                }
                if(weight.isEmpty()){
                    weightPressLayout.setError("not empty");
                }else{
                    weightPressLayout.setError(null);
                }
                if(!reps.isEmpty() && !weight.isEmpty()){
                    int oneRepMax = utility.oneRepMaxCalc(
                            Integer.parseInt(reps),
                            Integer.parseInt(weight)
                    );
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(Contract.RM__PRESS, oneRepMax);
                    editor.apply();
                }
            }
        });

        loadFromFit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getBodyWeightFromFit();
            }
        });
    }

    public void getBodyWeightFromFit(){
        String bodyweight;
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        } else {
            System.out.println("Accessing Google Fit....");
            accessGoogleFit();
        }
    }

    public void accessGoogleFit(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();
        /*DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        */
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_WEIGHT)
                .setTimeRange(1, endTime, TimeUnit.MILLISECONDS)
                .setLimit(1)
                .build();
        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
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
        if (response.getBuckets().size() > 0) {
            System.out.println("Nr of Buckets: " + response.getBuckets().size());

            for (Bucket bucket : response.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();

                for(DataSet dataSet : dataSets){
                    for (DataPoint dp : dataSet.getDataPoints()) {
                        System.out.println("Type : " + dp.getDataType());
                        System.out.println("Start : " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                        System.out.println("End : " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                        System.out.println("Type : " + dp.getDataType());
                        for (Field field : dp.getDataType().getFields()) {
                            System.out.println("Field " + field.getName() + " Value: " + dp.getValue(field));
                        }
                    }
                }

            }
        }else if(response.getDataSets().size() > 0){
            System.out.println("Single Bucket");
            for (DataSet dataSet : response.getDataSets()) {
                for (DataPoint dp : dataSet.getDataPoints()) {
                    System.out.println("Type : " + dp.getDataType());
                    System.out.println("Start : " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                    System.out.println("End : " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                    System.out.println("Type : " + dp.getDataType());
                    for (Field field : dp.getDataType().getFields()) {
                        System.out.println("Field " + field.getName() + " Value: " + dp.getValue(field));
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                accessGoogleFit();
            }
        }
    }
}
