package com.lundqvist.oscar.strength.ui;

import androidx.fragment.app.Fragment;
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
import com.lundqvist.oscar.strength.model.Program;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by Oscar on 2018-01-27.
 */

public class Tab3programs extends Fragment {
    private DatabaseReference rootRef;
    private DatabaseReference programsRef;
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
        programsRef = rootRef.child("programs");

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
                ArrayList<Program> programArrayList = new ArrayList<>();
                Program program;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    program = ds.getValue(Program.class);
                    System.out.println(program.toString());
                    programArrayList.add(program);
                }
                adapter = new ProgramsAdapter(programArrayList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Timber.w(databaseError.toException(), "loadPost:onCancelled");
                // [START_EXCLUDE]
                System.out.println("Failed to load post");
                // [END_EXCLUDE]
            }
        };
        programsRef.addValueEventListener(postListener);
        mPostListener = postListener;

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
