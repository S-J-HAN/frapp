/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Activity for the detailed artifact page
public class ArtifactActivity extends AppCompatActivity {

    // Variable to access the database
    private DatabaseReference dbRef;

    // When the activity is first loaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact);

        // Authenticate current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Get the correct artifactID passed from the gallery page
        final String artifactID = getIntent().getStringExtra("ARTIFACT_ID");

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Create a layout manager for the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (artifactID != null) {
            // Get artifact data
            dbRef.child("artifacts").child(artifactID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Artifact artifact = dataSnapshot.getValue(Artifact.class);

                    // Create an adapter to manager the contents of the recycler view
                    ArtifactAdapter mAdapter = new ArtifactAdapter(artifact, artifactID, currentUser, dbRef, ArtifactActivity.this);

                    recyclerView.setAdapter(mAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Log.w("loadPost:onCancelled", databaseError.toException());
                }
            });
        }

    }




}

