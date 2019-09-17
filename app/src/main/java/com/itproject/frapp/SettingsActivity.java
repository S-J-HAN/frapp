package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }






    public void openBirthdaySetting(View view) {
        Intent intent = new Intent(this, ChooseBirthdaySetting.class);
        startActivity(intent);
        finish();
    }

    public void openNameSetting(View view) {
        /*Intent intent = new Intent(this, ChooseNameSetting.class);
        startActivity(intent);
        finish();*/
    }

    public void openLanguageSetting(View view) {
        Intent intent = new Intent(this, ChooseLanguageActivity.class);
        startActivity(intent);
        finish();
    }

    public void openFontSetting(View view) {
        Intent intent = new Intent(this, ChooseFontSetting.class);
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
