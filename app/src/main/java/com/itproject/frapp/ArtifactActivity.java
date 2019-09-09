package com.itproject.frapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArtifactActivity extends AppCompatActivity {

    private String date;
    private String description;
    private String location;
    private String op; // UserID of the original poster
    private String tags;
    private String title;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private String artifactID = "frapp1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact);

        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Get artifact data
        dbRef.child("artifacts").child(artifactID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get ArtifactActivity object and do stuff with it inside onDataChange
                Artifact artifact = dataSnapshot.getValue(Artifact.class);
                TextView date = findViewById(R.id.textView_date);
                date.setText(artifact.getDate());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Something went wrong...
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });

    }
}
