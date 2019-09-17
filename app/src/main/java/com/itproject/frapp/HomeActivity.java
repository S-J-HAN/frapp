package com.itproject.frapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    private TextView jsonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currUser = mAuth.getCurrentUser();
        ref = database.getReference();

        jsonView = findViewById(R.id.jsonText);

        ref.child("users").child(currUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                jsonView.setText(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase problem", "realtime db cancelled", databaseError.toException());
            }
        });

        Button artifactButton = findViewById(R.id.button_artifact);

        artifactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goToArtifact(view);
            }
        });

    }

    public void goToArtifact(View view) {
        Intent intent = new Intent(this, ArtifactActivity.class);
        startActivity(intent);
        finish();
    }

}
