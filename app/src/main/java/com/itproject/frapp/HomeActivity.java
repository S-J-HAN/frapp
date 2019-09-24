package com.itproject.frapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private TextView jsonView;
    private Button settingsButton;
    private Button uploadButton;

    private ImageView imageView;
    private RecyclerView gallery;
    GridLayoutManager gridLayoutManager;

    final static int GALLERYWIDTH = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currUser = mAuth.getCurrentUser();
        ref = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

//        // Set up settings button
//        settingsButton = findViewById(R.id.settingsButton);
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Open settings page
//                openSettingsActivity();
//            }
//        });

        // Set up upload button
        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open upload page
                openUpload();
            }
        });
//
//
//        // Get list of artifacts
//        jsonView = findViewById(R.id.jsonText);

        imageView = findViewById(R.id.imageView);
        gallery = findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), GALLERYWIDTH);
        gallery.setLayoutManager(gridLayoutManager);

        ref.child("artifacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
                int c = 0;
                for (DataSnapshot arti : dataSnapshot.getChildren()) {
                    Artifact a = arti.getValue(Artifact.class);
                    artifacts.add(a);
                }
//                jsonView.setText(urls[0]);

                GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), artifacts);
                gallery.setAdapter(galleryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase problem", "realtime db cancelled", databaseError.toException());
            }
        });


        Button artifactButton = findViewById(R.id.button_artifact);

        artifactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goToArtifact(view);
            }
        });

    }

    public void openSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void openUpload() {
<<<<<<< Updated upstream
        Intent intent = new Intent(this, ArtifactUpload.class);
=======
        Intent intent = new Intent(this, ArtifactUploadActivity.class);
>>>>>>> Stashed changes
        startActivity(intent);

//         Button artifactButton = findViewById(R.id.button_artifact);


     }

     public void goToArtifact(View view) {
         Intent intent = new Intent(this, ArtifactActivity.class);
         startActivity(intent);
         finish();
    }

}
