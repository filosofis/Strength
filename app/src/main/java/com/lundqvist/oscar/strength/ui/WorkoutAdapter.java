package com.lundqvist.oscar.strength.ui;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;

import java.text.DateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private Cursor cursor;

    public WorkoutAdapter(Cursor cursor) {
        this.cursor = cursor;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nameView;
        public TextView setsView;
        public TextView repsView;
        public TextView weightView;
        public TextView noteView;
        public TextView completedView;
        public TextView timeView;
        public TextView setsTitle;
        public TextView repsTitle;
        public TextView weightTitle;
        public TextView timeTitle;

        public ViewHolder(@NonNull View itemView) {
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
        String reps = Integer.toString(cursor.getInt(Contract.ExerciseEntry.INDEX_REPS));
        int weight = cursor.getInt(Contract.ExerciseEntry.INDEX_WEIGHT);
        String note = cursor.getString(Contract.ExerciseEntry.INDEX_NOTE);
        long complete = cursor.getLong(Contract.ExerciseEntry.INDEX_COMPLETED);
        int time = cursor.getInt(Contract.ExerciseEntry.INDEX_TIME);

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
        }else {
            holder.nameView.setText(name);
            holder.setsView.setText(sets);
            holder.repsView.setText(reps);
            if(note.length()>2){
                holder.noteView.setText(note);
                holder.noteView.setVisibility(View.VISIBLE);
                System.out.println("note " + note);
            }else{holder.noteView.setVisibility(View.GONE);}
            if(weight != 0) {
                holder.weightView.setText(Integer.toString(weight));
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
        }else{
            holder.completedView.setVisibility(View.GONE);
        }
        System.out.println("Time: " + time);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
