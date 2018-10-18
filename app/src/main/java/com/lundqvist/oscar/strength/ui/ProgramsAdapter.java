package com.lundqvist.oscar.strength.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.model.Program;

import java.util.ArrayList;

/**
 * Created by Oscar on 2018-03-24.
 */

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.ViewHolder> {
    private ArrayList<Program> programArrayList;

    public ProgramsAdapter(ArrayList<Program> programArrayList) {
        this.programArrayList = programArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleView;
        private TextView durationView;
        private TextView authorView;
        private TextView descriptionView;
        private ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            durationView = itemView.findViewById(R.id.duration);
            authorView = itemView.findViewById(R.id.author);
            descriptionView = itemView.findViewById(R.id.description);
        }
    }
    @Override
    public void onBindViewHolder(ProgramsAdapter.ViewHolder holder, int position) {
        Program program = programArrayList.get(position);
        holder.titleView.setText(program.getTitle());
        holder.authorView.setText(program.getAuthor());
        holder.durationView.setText(String.valueOf(program.getDuration()));
        holder.descriptionView.setText(program.getDescription());
    }

    @Override
    public ProgramsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.program_list_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return programArrayList.size();
    }
}
