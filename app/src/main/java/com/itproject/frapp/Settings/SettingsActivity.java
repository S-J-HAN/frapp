package com.itproject.frapp.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itproject.frapp.MainActivity;
import com.itproject.frapp.MainGallery.HomeActivity;
import com.itproject.frapp.R;
import com.itproject.frapp.Schema.User;

import java.io.InputStream;

public class SettingsActivity extends AppCompatActivity {

    ImageView dp;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set profile image
        dp = findViewById(R.id.profileImageView);


        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get User object and do stuff with it inside onDataChange
                User user = dataSnapshot.getValue(User.class);

                Glide.with(getApplicationContext())
                        .load(user.getUrl())
                        .fitCenter()
                        .into(dp);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Something went wrong...
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }



    // https://www.viralandroid.com/2015/11/load-image-from-url-internet-in-android.html
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }


    public void logoutOfApp(View view) {

        FirebaseAuth.getInstance().signOut();

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
