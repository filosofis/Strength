package com.lundqvist.oscar.strength.ui;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameView);
            setsView = itemView.findViewById(R.id.setsView);
            repsView = itemView.findViewById(R.id.repsView);
            weightView = itemView.findViewById(R.id.weightView);
            noteView = itemView.findViewById(R.id.noteView);
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
        String weight = Integer.toString(cursor.getInt(Contract.ExerciseEntry.INDEX_WEIGHT));
        holder.nameView.setText(name);
        holder.setsView.setText(sets);
        holder.repsView.setText(reps);
        holder.weightView.setText(weight);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
