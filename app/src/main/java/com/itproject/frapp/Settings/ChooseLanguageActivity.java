/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;

import android.content.Context;
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

import java.util.Locale;


/* allows the user to select a language (either english or chinese)
 */
public class ChooseLanguageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);

        // check what current locale is
        Locale current = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0);
        String language = current.getDisplayLanguage();

        System.out.println(language);

        if (language.equals("English")) {
            // change tick position
            ImageView englishTick =(ImageView)findViewById(R.id.englishSelected);
            englishTick.setVisibility(View.VISIBLE);

            ImageView chineseTick =(ImageView)findViewById(R.id.chineseSelected);
            chineseTick.setVisibility(View.INVISIBLE);
        } else {
            // change tick position
            ImageView englishTick =(ImageView)findViewById(R.id.englishSelected);
            englishTick.setVisibility(View.INVISIBLE);

            ImageView chineseTick =(ImageView)findViewById(R.id.chineseSelected);
            chineseTick.setVisibility(View.VISIBLE);
        }


    }


    /* sets the language of the app to chinese upon user selection
     */
    public void setLanguageToEnglish(View view) {

        String language = "en";

        // change tick position
        ImageView englishTick =(ImageView)findViewById(R.id.englishSelected);
        englishTick.setVisibility(View.VISIBLE);

        ImageView chineseTick =(ImageView)findViewById(R.id.chineseSelected);
        chineseTick.setVisibility(View.INVISIBLE);


        // set locale to required language
        SetLanguage.setLocale(this, language);


    }


    /* sets the language of the app to english upon user selection
     */
    public void setLanguageToChinese(View view) {

        String language = "zh";

        // change tick position
        ImageView englishTick =(ImageView)findViewById(R.id.englishSelected);
        englishTick.setVisibility(View.INVISIBLE);

        ImageView chineseTick =(ImageView)findViewById(R.id.chineseSelected);
        chineseTick.setVisibility(View.VISIBLE);


        // set locale to required language
        SetLanguage.setLocale(this, language);

    }



    /* go to SettingsActivity
     */
    public void openSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

}
