package com.lundqvist.oscar.strength.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import com.lundqvist.oscar.strength.data.Contract.ExerciseEntry;
import com.lundqvist.oscar.strength.model.Exercise;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder>
implements LoaderManager.LoaderCallbacks<Cursor>{
    private Cursor cursor;

    public WorkoutAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

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
        String exerciseName= cursor.getString(ExerciseEntry.INDEX_EXERCISE_NAME);
        holder.nameView.setText(exerciseName);
        holder.weightView.setText(cursor.getInt(ExerciseEntry.INDEX_WEIGHT));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
