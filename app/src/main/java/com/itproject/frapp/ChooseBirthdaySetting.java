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

public class ChooseBirthdaySetting extends AppCompatActivity {

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

        Button next = findViewById(R.id.nextButton3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set the user's birthday
                String birthday = "DD/MM/YYY"; // Replace this with user input
                dbRef.child("users").child(currentUser.getUid()).child("birthday").setValue(birthday);

                // Move on the the next page - font settings
                openDPSetting();
            }
        });
    }

    public void openDPSetting() {
        Intent intent = new Intent(this, ChooseDPSetting.class);
        startActivity(intent);
        finish();
    }
}
