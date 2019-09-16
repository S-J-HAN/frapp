package com.itproject.frapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private String artifactID = "frapp5";
    private String commentID = "comment1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact);

        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();


        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // Get artifact data
        dbRef.child("artifacts").child(artifactID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.println(Log.DEBUG, "hello", dataSnapshot.getChildren().toString());
//                System.out.println(dataSnapshot.getChildren());
//                // Get ArtifactActivity object and do stuff with it inside onDataChange
                Artifact artifact = dataSnapshot.getValue(Artifact.class);
//                TextView date = findViewById(R.id.textView_date);
//                date.setText(artifact.getDate());
//                ImageView image = findViewById(R.id.imageView_artifact);
//                // image.setImage..............
//                TextView description = findViewById(R.id.textView_description);
//                description.setText(artifact.getDescription());
//
//                TextView tags = findViewById(R.id.textView_tags);
//                tags.setText(artifact.getComments().toString());
                // specify an adapter (see also next example)
                ArtifactAdapter mAdapter = new ArtifactAdapter(artifact, artifactID, currentUser, dbRef);
                recyclerView.setAdapter(mAdapter);




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

