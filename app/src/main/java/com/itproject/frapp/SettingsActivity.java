package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {

    ImageView dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set profile image
        dp = findViewById(R.id.profileImageView);
    }
}
