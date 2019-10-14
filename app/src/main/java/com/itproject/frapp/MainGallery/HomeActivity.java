package com.itproject.frapp.MainGallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.itproject.frapp.ArtifactPages.ArtifactAdapter;
import com.itproject.frapp.Upload.ArtifactUploadActivity;
import com.itproject.frapp.R;
import com.itproject.frapp.Schema.Artifact;
import com.itproject.frapp.Settings.SettingsActivity;

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
                if (searchBar.getText().toString().length() <= 1) {
                    // All artifacts
                    currentArtifacts = allArtifacts;
                } else {
                    // Search for keywords
                    ArrayList<Artifact> relevant = search(searchBar.getText().toString());
                    currentArtifacts = relevant;
                }

                // Show only relevant artifacts
                GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), currentArtifacts, HomeActivity.this);
                gallery.setAdapter(galleryAdapter);

                return true;

            }
        });

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

                GalleryAdapter galleryAdapter = new GalleryAdapter(getApplicationContext(), currentArtifacts, HomeActivity.this);
                gallery.setAdapter(galleryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase problem", "realtime db cancelled", databaseError.toException());
            }
        });

    }

    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openUpload() {
        Intent intent = new Intent(this, ArtifactUploadActivity.class);
        startActivity(intent);
    }

    public ArrayList<Artifact> search(String input) {
        String[] terms = input.split(" ");
        if (terms.length == 0) {
            return allArtifacts;
        }
        ArrayList<Artifact> relevant = new ArrayList<Artifact>();
        for (Artifact a : allArtifacts) {
            for (String term : terms) {
                if (a.getDescription().toLowerCase().contains(term.toLowerCase()) ||
                    a.getTags().toLowerCase().contains(term.toLowerCase()) ||
                    a.getDate().toLowerCase().contains(term.toLowerCase())) {
                    relevant.add(a);
                }
            }
        }

        return relevant;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GalleryAdapter.REQUEST_DELETE && resultCode == RESULT_OK) {
            String artifactID = data.getExtras().getString("Delete");
            if (artifactID != null) {
                ref.child("artifacts").child(artifactID).removeValue();
            }
        }
    }

}
