package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseFontSetting extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        Button next = findViewById(R.id.nextButton2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set the location value for the user
                String fontSize = "S"; // Replace this with whatever the user input is
                dbRef.child("users").child(currentUser.getUid()).child("fontSize").setValue(fontSize);

                // Move on the the next page - font settings
                openBirthdaySetting();
            }
        });
    }

    public void openBirthdaySetting() {
        Intent intent = new Intent(this, ChooseFontSetting.class);
        startActivity(intent);
        finish();
    }
}
