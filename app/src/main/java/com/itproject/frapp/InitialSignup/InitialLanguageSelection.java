/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.InitialSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itproject.frapp.R;
import com.itproject.frapp.SetLanguage;


/* allows user to select a language the very first time they use the app
 */
public class InitialLanguageSelection extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private final String[] languages = {"Select language: ", "English", "汉语"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_language_selection);

        // create language spinner
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
    }


    /* sets the language of the app upon user selection
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        String languageSelected = (String) parent.getItemAtPosition(position);
        String language = "en";


        if (languageSelected.equals("English")) {
            language = "en";
        }

        if (languageSelected.equals("汉语")){
            language = "zh";
        }

        // set locale to required language
        SetLanguage.setLocale(this, language);

        // add to data base
        dbRef.child("users").child(currentUser.getUid()).child("language").setValue(language);

        // add language preference to local data storage
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(InitialLanguageSelection.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.putString("LANGUAGE", language);
        myEditor.commit();

    }


    /* do nothing if no information selected from spinner
     */
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /* move app to InitialFontSelection
     */
    public void openInitialDPSelection(View view) {
        Intent intent = new Intent(this, InitialDPSelection.class);
        startActivity(intent);
        finish();
    }


}
