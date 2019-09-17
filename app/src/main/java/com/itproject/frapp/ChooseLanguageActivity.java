package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;


public class ChooseLanguageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private String currentLanguage = "ENG";

    private final String[] languages = {"Select language: ", "English", "Chinese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        // language spinner
        Spinner spinner = (Spinner) findViewById(R.id.languageSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languages) {
            @Override
            public boolean isEnabled(int pos) {
                if (pos == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int pos, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(pos, convertView, parent);
                TextView textview = (TextView) view;
                if (pos == 0) {
                    textview.setTextColor(Color.GRAY);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        //Button next = findViewById(R.id.nextButton);
        //next.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {

                // Set the user's language
        //        String language = "ENG"; // Replace this with user selection
        //        dbRef.child("users").child(currentUser.getUid()).child("language").setValue(language);

                // Move on the the next page - font settings
               // openFontSizeSetting();
        //    }
        //});
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        String languageSelected = (String) parent.getItemAtPosition(position);
        String language = "en";

        Locale locale;

        if (languageSelected.equals("English")) {
            System.out.println("ENGLISH");
            language = "en";
        }

        if (languageSelected.equals("Chinese")){
            System.out.println("CHINESE");
            language = "zh";
        }

        SetLanguage.setLanguage(this, language);

        dbRef.child("users").child(currentUser.getUid()).child("language").setValue(language);

        //dbRef.child("users").child(currentUser.getUid()).child("language").;
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }

/*
    public void setLocale(Locale locale) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }

    }
*/
/*
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
*/
    public void openFontSizeSetting(View view) {
        Intent intent = new Intent(this, ChooseFontSetting.class);
        startActivity(intent);
    }

}
