/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.InitialSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.itproject.frapp.R;
import com.itproject.frapp.SetLanguage;


/* allows user to select a language the very first time they use the app
 */
public class InitialLanguageSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_language_selection);
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


    /* move app to InitialFontSelection
     */
    public void openInitialDPSelection(View view) {
        Intent intent = new Intent(this, InitialDPSelection.class);
        startActivity(intent);
        finish();
    }


}
