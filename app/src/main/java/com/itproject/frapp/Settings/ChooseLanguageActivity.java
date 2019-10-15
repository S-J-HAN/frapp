/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itproject.frapp.R;
import com.itproject.frapp.SetLanguage;
import com.itproject.frapp.Settings.SettingsActivity;


/* allows the user to select a language (either english or chinese)
 */
public class ChooseLanguageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    private final String[] languages = {"Select language: ", "English", "汉语"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
    }


    /* sets the language of the app to chinese upon user selection
     */
    public void setLanguageToEnglish(View view) {
        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        String language = "en";

        // change tick position
        ImageView englishTick =(ImageView)findViewById(R.id.englishSelected);
        englishTick.setVisibility(View.VISIBLE);

        ImageView chineseTick =(ImageView)findViewById(R.id.chineseSelected);
        chineseTick.setVisibility(View.INVISIBLE);



        // set locale to required language
        SetLanguage.setLocale(this, language);

        // add to data base
        dbRef.child("users").child(currentUser.getUid()).child("language").setValue(language);
    }


    /* sets the language of the app to english upon user selection
     */
    public void setLanguageToChinese(View view) {
        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        String language = "zh";

        // change tick position
        ImageView englishTick =(ImageView)findViewById(R.id.englishSelected);
        englishTick.setVisibility(View.INVISIBLE);

        ImageView chineseTick =(ImageView)findViewById(R.id.chineseSelected);
        chineseTick.setVisibility(View.VISIBLE);


        // set locale to required language
        SetLanguage.setLocale(this, language);

        // add to data base
        dbRef.child("users").child(currentUser.getUid()).child("language").setValue(language);
    }



    /* move app to SettingsActivity
     */
    public void openSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

}
