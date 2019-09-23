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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

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

    private ArrayList<Artifact> allArtifacts;
    private ArrayList<Artifact> currentArtifacts;

    private TextView jsonView;
    private ImageButton settingsButton;
    private ImageButton uploadButton;
    private EditText searchBar;

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

        // Set up settings button
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open settings page
                openSettingsActivity();
            }
        });

        // Set up upload button
        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open upload page
                openUpload();
            }
        });

        // Set up search bar
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    // Search for keywords
                    ArrayList<Artifact> relevant = search(searchBar.getText().toString());
                    currentArtifacts = relevant;

                    // Show only relevant artifacts
                    GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), currentArtifacts);
                    gallery.setAdapter(galleryAdapter);

                    // Defocus search bar
//                    searchBar.clearFocus();

                    return true;
                }
                return false;
            }
        });


//
//
//        // Get list of artifacts
//        jsonView = findViewById(R.id.jsonText);

//        imageView = findViewById(R.id.imageView);
        gallery = findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), GALLERYWIDTH);
        gallery.setLayoutManager(gridLayoutManager);

        ref.child("artifacts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allArtifacts = new ArrayList<Artifact>();
                int c = 0;
                for (DataSnapshot arti : dataSnapshot.getChildren()) {
                    Artifact a = arti.getValue(Artifact.class);
                    a.setID(arti.getKey());
                    allArtifacts.add(a);
                }
                currentArtifacts = allArtifacts;
//                jsonView.setText(urls[0]);

                GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), currentArtifacts);
                gallery.setAdapter(galleryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase problem", "realtime db cancelled", databaseError.toException());
            }
        });


//        Button artifactButton = findViewById(R.id.button_artifact);
//
//        artifactButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                goToArtifact(view);
//            }
//        });

    }

    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openUpload() {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);

//         Button artifactButton = findViewById(R.id.button_artifact);
    }

//    public void goToArtifact(View view) {
//         Intent intent = new Intent(this, ArtifactActivity.class);
//         startActivity(intent);
//    }

    public ArrayList<Artifact> search(String input) {
        String[] terms = input.split(" ");
        if (terms.length == 0) {
            return allArtifacts;
        }
        ArrayList<Artifact> relevant = new ArrayList<Artifact>();
        for (Artifact a : allArtifacts) {
            for (String term : terms) {
                if (a.getDescription().toLowerCase().contains(term.toLowerCase()) ||
                    a.getLocation().toLowerCase().contains(term.toLowerCase()) ||
                    a.getTags().toLowerCase().contains(term.toLowerCase()) ||
                    a.getDate().toLowerCase().contains(term.toLowerCase())) {
                    relevant.add(a);
                }
            }
        }

        return relevant;
    }


}
