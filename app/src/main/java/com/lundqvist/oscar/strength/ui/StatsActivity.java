package com.lundqvist.oscar.strength.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.Utility;
import com.lundqvist.oscar.strength.data.Contract;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        final Utility utility = new Utility();
        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        final Button saveSquat = findViewById(R.id.button_save_squat);
        final Button saveBench = findViewById(R.id.button_save_bench);
        final Button saveDead = findViewById(R.id.button_save_dead);
        final Button savePress = findViewById(R.id.button_save_press);

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
                    System.out.println("Squat " + "Reps: " + reps + ", Weight: " + weight);
                    int oneRepMax = utility.oneRepMaxCalc(
                            Integer.parseInt(reps),
                            Integer.parseInt(weight)
                    );
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(Contract.RM_SQUAT, oneRepMax);
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
                    System.out.println("Squat " + "Reps: " + reps + ", Weight: " + weight);
                    int oneRepMax = utility.oneRepMaxCalc(
                            Integer.parseInt(reps),
                            Integer.parseInt(weight)
                    );
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
                    System.out.println("Squat " + "Reps: " + reps + ", Weight: " + weight);
                    int oneRepMax = utility.oneRepMaxCalc(
                            Integer.parseInt(reps),
                            Integer.parseInt(weight)
                    );
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
                    System.out.println("Squat " + "Reps: " + reps + ", Weight: " + weight);
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
    }
}
