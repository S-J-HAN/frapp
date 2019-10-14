package com.itproject.frapp.Upload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;
import com.itproject.frapp.MainGallery.HomeActivity;
import com.itproject.frapp.R;
import com.itproject.frapp.Schema.Artifact;

public class ArtifactTagsActivity extends AppCompatActivity {

    DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private Artifact artifact;
    private ImageButton nextButton;
    private String tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_tags);

        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Get artifact from ArtifactUploadActivity
        artifact = (Artifact) getIntent().getSerializableExtra("Artifact");

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get description from user
                EditText newTags = findViewById(R.id.descInput);
                tags = newTags.getText().toString();
                artifact.setTags(tags);
                //Open settings page
                finishUpload();
            }
        });
    }

    public void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void finishUpload() {
        artifact.setOp("fakeuser");
        dbRef.child("artifacts").push().setValue(artifact);
        ArtifactUploadActivity.uploadActivity.finish();
        ArtifactDateActivity.dateActivity.finish();
        ArtifactDescriptionActivity.descActivity.finish();
        finish();
        openHome();
    }
}
