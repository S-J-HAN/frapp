package com.itproject.frapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        if (currentUser == null) {
            // First time anyone has logged in on this device

            Button goButton = findViewById(R.id.goButton);
            final TextView nameInput = findViewById(R.id.nameInput);

            goButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            // Check if user account already exists
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                User user = child.getValue(User.class);
                                if (user.getName().equals(nameInput.getText().toString())) {

                                    // User already exists, log them in
                                    mAuth.signInWithEmailAndPassword(user.getName() + "@frapp.com", "123456")
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override

                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Sign in succeeded, move on to home page
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        Log.i("Logged in", "onComplete: " + user.toString());

                                                        openHome();
                                                    } else {
                                                        // Sign in failed, rip.
                                                        Log.w("Sign in failed", "signInWithEmail:failure", task.getException());
                                                    }
                                                }
                                            });
                                    break;
                                } else {
                                    Log.i("NO MATCH", user.getName() + " " + nameInput.getText().toString());
                                }
                            }

                            // This user does not yet exist, make a new account for them
                            if (!nameInput.getText().toString().equals("")) {
                                mAuth.createUserWithEmailAndPassword(nameInput.getText().toString()+"@frapp.com", "123456")
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Successfully created new user
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    // Add new user data to database
                                                    dbRef.child("users").child(user.getUid()).child("email").setValue(nameInput.getText().toString()+"@frapp.com");
                                                    dbRef.child("users").child(user.getUid()).child("name").setValue(nameInput.getText().toString());
                                                    dbRef.child("users").child(user.getUid()).child("location").setValue("Somewhere, Earth");

                                                    // Move on to the settings pages
                                                    openLanguageSettings();
                                                }
                                            }
                                        });
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("ERROR", "onCancelled: ", databaseError.toException());
                        }
                    });
                }
            });
        } else {
            // A log in has occurred before so we'll go straight to the home screen
            openHome();
            finish();
        }
    }

    public void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void openLanguageSettings() {
        Intent intent = new Intent(this, ChooseLanguageActivity.class);
        startActivity(intent);
        finish();
    }

}
