/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itproject.frapp.R;
import com.itproject.frapp.Settings.SettingsActivity;


/* allows the user to select font size
 */
public class ChooseFontSetting extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_font_setting);

    }


    public void onClick(View view) {
        String fontSize = "M";


        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        switch (view.getId()) {
            case R.id.smallButton:
                fontSize = "S";
                changeTextColours(true, false, false);
                break;
            case R.id.mediumButton:
                fontSize = "M";
                changeTextColours(false, true, false);
                break;
            case R.id.largeButton:
                fontSize = "L";
                changeTextColours(false, false, true);
                break;
        }

        dbRef.child("users").child(currentUser.getUid()).child("fontSize").setValue(fontSize);

        // add to local storage
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fontSize", fontSize);
        editor.commit();
    }


    private void changeTextColours(Boolean small, Boolean medium, Boolean large) {
        Button smallButton = (Button) findViewById(R.id.smallButton);
        Button mediumButton = (Button) findViewById(R.id.mediumButton);
        Button largeButton = (Button) findViewById(R.id.largeButton);

        if (small) {
            smallButton.setTextColor(Color.RED);
            mediumButton.setTextColor(Color.BLACK);
            largeButton.setTextColor(Color.BLACK);
        }

        if (medium) {
            smallButton.setTextColor(Color.BLACK);
            mediumButton.setTextColor(Color.RED);
            largeButton.setTextColor(Color.BLACK);
        }

        if (large) {
            smallButton.setTextColor(Color.BLACK);
            mediumButton.setTextColor(Color.BLACK);
            largeButton.setTextColor(Color.RED);
        }

    }

    private void setImage(int imageId) {
        final ImageView imageView = (ImageView)findViewById(imageId);
        //imageView.setImageResource(R.drawable.tick);
    }


    public void openSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
