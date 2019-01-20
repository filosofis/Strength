package com.lundqvist.oscar.strength.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lundqvist.oscar.strength.R;
import com.lundqvist.oscar.strength.data.Contract;
import com.lundqvist.oscar.strength.model.Exercise;
import com.lundqvist.oscar.strength.model.Program;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab3programs extends Fragment{
    private DatabaseReference rootRef;
    private DatabaseReference programsRef;
    private DatabaseReference programDataRef;
    private ValueEventListener mPostListener;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3programs, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        rootRef = FirebaseDatabase.getInstance().getReference();
        programsRef = rootRef.child("programs-menu");
        System.out.println("Create View");
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        // TODO: 2018-08-05 Change the names  
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("On Data Change");
                Program program;
                final ArrayList<Program> programArrayList = new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    program = ds.getValue(Program.class);
                    programArrayList.add(program);
                }

                adapter = new ProgramsAdapter(programArrayList, new ItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String title = programArrayList.get(position).title;
                        System.out.println(title);
                        getContext().getContentResolver().call(
                                Contract.BASE_CONTENT_URI,
                                "clearProgram",
                                null,
                                null
                        );
                        insertProgramData(title);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        programsRef.addValueEventListener(postListener);
        mPostListener = postListener;
    }

    private void insertProgramData(String title){
        programDataRef = rootRef.child("programs-data").child(title);
        final ArrayList<ContentValues> exerciseCVs = new ArrayList<>();
        programDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Exercise exercise;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    exercise = ds.getValue(Exercise.class);
                    if(exercise.getNote() == null){
                        exercise.setNote("0");
                    }
                    ContentValues cv = new ContentValues();
                    cv.put(Contract.ExerciseEntry.COLUMN_WORKOUT, exercise.getWorkout());
                    cv.put(Contract.ExerciseEntry.COLUMN_EXERCISE_NAME, exercise.getName());
                    cv.put(Contract.ExerciseEntry.COLUMN_WEIGHT, exercise.getWeight());
                    cv.put(Contract.ExerciseEntry.COLUMN_SETS, exercise.getSets());
                    cv.put(Contract.ExerciseEntry.COLUMN_REPS, exercise.getReps());
                    cv.put(Contract.ExerciseEntry.COLUMN_TIME, exercise.getTime());
                    cv.put(Contract.ExerciseEntry.COLUMN_NOTE, exercise.getNote());
                    exerciseCVs.add(cv);
                }
                getContext().getContentResolver().call(Contract.BASE_CONTENT_URI,
                        "clearProgram",
                        null,
                        null);
                int entries = getContext().getContentResolver().bulkInsert(Contract.BASE_CONTENT_URI,
                        exerciseCVs.toArray(new ContentValues[exerciseCVs.size()]));
                System.out.println("Inserted rows: " + entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Timber.w(databaseError.toException(), "loadPost:onCancelled");
                // [START_EXCLUDE]
                System.out.println("Failed to load post");
                // [END_EXCLUDE]
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        // Remove post value event listener
        if (mPostListener != null) {
            rootRef.removeEventListener(mPostListener);
        }
    }
}