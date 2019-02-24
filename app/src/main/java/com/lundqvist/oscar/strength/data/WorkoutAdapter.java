package com.lundqvist.oscar.strength.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lundqvist.oscar.strength.R;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private Cursor cursor;
    private String amrapResult;
    private AdapterCallback adapterCallback;
    private Context context;

    public WorkoutAdapter(Cursor cursor, Context context, AdapterCallback adapterCallback) {
        this.cursor = cursor;
        this.adapterCallback = adapterCallback;
        this.context = context;
    }

    public interface AdapterCallback{
        void textInputValue(String amrap, int weight, String name);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameView;
        private TextView setsView;
        private TextView repsView;
        private TextView weightView;
        private TextView noteView;
        private TextView completedView;
        private TextView timeView;
        private TextView setsTitle;
        private TextView repsTitle;
        private TextView weightTitle;
        private TextView timeTitle;
        private TextView completeTitle;
        private TextInputEditText inputText;
        private TextInputLayout inputLayout;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameView);
            setsView = itemView.findViewById(R.id.setsView);
            repsView = itemView.findViewById(R.id.repsView);
            weightView = itemView.findViewById(R.id.weightView);
            noteView = itemView.findViewById(R.id.noteView);
            completedView = itemView.findViewById(R.id.completeView);
            timeView = itemView.findViewById(R.id.timeView);
            setsTitle = itemView.findViewById(R.id.setsTitle);
            repsTitle = itemView.findViewById(R.id.repsTitle);
            weightTitle = itemView.findViewById(R.id.weightTitle);
            timeTitle = itemView.findViewById(R.id.timeTitle);
            inputText = itemView.findViewById(R.id.inputText);
            inputLayout = itemView.findViewById(R.id.inputLayout);
            completeTitle = itemView.findViewById(R.id.completeTitle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.workout_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String name = cursor.getString(Contract.ExerciseEntry.INDEX_EXERCISE_NAME);
        String sets = Integer.toString(cursor.getInt(Contract.ExerciseEntry.INDEX_SETS));
        int reps = cursor.getInt(Contract.ExerciseEntry.INDEX_REPS);
        String repsString = Integer.toString(reps);
        int weight = cursor.getInt(Contract.ExerciseEntry.INDEX_WEIGHT);
        String note = cursor.getString(Contract.ExerciseEntry.INDEX_NOTE);
        long complete = cursor.getLong(Contract.ExerciseEntry.INDEX_COMPLETED);
        int time = cursor.getInt(Contract.ExerciseEntry.INDEX_TIME);
        holder.inputText.setVisibility(View.GONE);

        if(name.equals(Contract.REST_DAY)){
            System.out.println("Name was equal to rest");
            holder.nameView.setText(name);
            holder.noteView.setText(note);
            holder.noteView.setVisibility(View.VISIBLE);
            holder.setsView.setVisibility(View.GONE);
            holder.setsView.setVisibility(View.GONE);
            holder.weightView.setVisibility(View.GONE);
            holder.setsTitle.setVisibility(View.GONE);
            holder.repsTitle.setVisibility(View.GONE);
            holder.weightTitle.setVisibility(View.GONE);
            holder.timeView.setVisibility(View.GONE);
            holder.timeTitle.setVisibility(View.GONE);
            adapterCallback.textInputValue("notAmrap", 0, null);
        }else {
            holder.nameView.setText(name);
            holder.setsView.setText(sets);
            /*
                Reps -1 indicating a AMRAP set
             */
            if(reps == -1){
                holder.inputText.setVisibility(View.VISIBLE);
                holder.repsView.setText(R.string.amrap);
                amrapResult = holder.inputText.getText().toString();
                adapterCallback.textInputValue("disable", weight, name);
                //adapterCallback.textInputValue(amrapResult, weight);
                holder.inputText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        System.out.println("AMRAP result: " + s.toString());
                        amrapResult = s.toString();
                        adapterCallback.textInputValue(amrapResult, 0, null);
                    }

                });
            }else {
                holder.repsView.setText(repsString);
                adapterCallback.textInputValue("notAmrap", 0, null);
            }
            if(note.length() > 2){
                holder.noteView.setText(note);
                holder.noteView.setVisibility(View.VISIBLE);
                System.out.println("note " + note);
            }else{holder.noteView.setVisibility(View.GONE);}
            if(weight != 0) {
                SharedPreferences prefs = context.getSharedPreferences(Contract.SHARED_REPFS, Context.MODE_PRIVATE);
                int calculatedWeight = 1;
                switch(name){
                    case "Squat":
                        calculatedWeight = (weight*prefs.getInt(Contract.RM_SQUAT, 1))/100;
                        break;
                    case "Bench":
                        calculatedWeight = (weight*prefs.getInt(Contract.RM__BENCH, 1))/100;
                        break;
                    case "Deadlift":
                        calculatedWeight = (weight*prefs.getInt(Contract.RM__DEAD, 1))/100;
                        break;
                    case "Military Press":
                        calculatedWeight = (weight*prefs.getInt(Contract.RM__PRESS, 1))/100;
                        break;
                }
                holder.weightView.setText(Integer.toString(calculatedWeight));
            }else{
                holder.weightTitle.setVisibility(View.GONE);
                holder.weightView.setVisibility(View.GONE);
            }
            if(time != 0){
                holder.timeView.setVisibility(View.VISIBLE);
                holder.timeTitle.setVisibility(View.VISIBLE);
                holder.timeView.setText(Integer.toString(time));
            }else{
                holder.timeView.setVisibility(View.GONE);
                holder.timeTitle.setVisibility(View.GONE);
            }
        }
        if(complete != 0){
            Date date = new Date(complete);
            String fDate = DateFormat.getDateInstance().format(date);
            holder.completedView.setText(fDate);
            holder.completedView.setVisibility(View.VISIBLE);
            holder.completeTitle.setVisibility(View.VISIBLE);
        }else{
            holder.completedView.setVisibility(View.GONE);
            holder.completeTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

}
