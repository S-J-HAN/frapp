package com.itproject.frapp.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;

import com.itproject.frapp.MainActivity;
import com.itproject.frapp.MainGallery.HomeActivity;
import com.itproject.frapp.R;

public class SettingsActivity extends AppCompatActivity {

    ImageView dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set profile image
        dp = findViewById(R.id.profileImageView);
    }




    public void logoutOfApp(View view) {


        openMainActivity();
    }

    public void openLanguageSetting(View view) {
        Intent intent = new Intent(this, ChooseLanguageActivity.class);
        startActivity(intent);
        finish();
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void openDPSetting(View view) {
        Intent intent = new Intent(this, ChooseDPSetting.class);
        startActivity(intent);
        finish();
    }

    public void openHomeSetting(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
