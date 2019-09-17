package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
        setContentView(R.layout.activity_choose_font_setting);

        // Authenticate current user
        //mAuth = FirebaseAuth.getInstance();
        //final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        //dbRef = FirebaseDatabase.getInstance().getReference();

        //Button next = findViewById(R.id.nextButton2);
        //next.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {

                // Set the location value for the user
        //        String fontSize = "S"; // Replace this with whatever the user input is
        //        dbRef.child("users").child(currentUser.getUid()).child("fontSize").setValue(fontSize);

                // Move on the the next page - font settings
        //        openBirthdaySetting();
        //    }
        //});
    }


    public void onClick(View view) {
        String fontSize = "M";


        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();
/*
        switch (view.getId()) {
            case R.id.smallButton:
                fontSize = "S";
                setImage(R.id.smallTickImage);
                break;
            case R.id.mediumButton:
                fontSize = "M";
                setImage(R.id.mediumTickImage);
                break;
            case R.id.largeButton:
                fontSize = "L";
                setImage(R.id.largeTickImage);
                break;
        }*/

        dbRef.child("users").child(currentUser.getUid()).child("fontSize").setValue(fontSize);

    }


    private void setImage(int imageId) {
        final ImageView imageView = (ImageView)findViewById(imageId);
        //imageView.setImageResource(R.drawable.tick);
    }

    public void openBirthdaySetting(View view) {

        Intent intent = new Intent(this, ChooseBirthdaySetting.class);
        startActivity(intent);
        finish();
    }
}
