package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseLanguageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);


        Spinner spinner = (Spinner) findViewById(R.id.languageSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languageOptions, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setPrompt("Select language");
        spinner.setOnItemSelectedListener(this);

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
        String language = "ENG";

        if (languageSelected.equals("English")) {
            language = "ENG";
        } else {
            language = "CHN";
        }
        dbRef.child("users").child(currentUser.getUid()).child("language").setValue(language);
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openFontSizeSetting(View view) {
        Intent intent = new Intent(this, ChooseFontSetting.class);
        startActivity(intent);
    }
}
