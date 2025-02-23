package com.itproject.frapp.Upload;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itproject.frapp.R;
import com.itproject.frapp.Schema.Artifact;

public class ArtifactDescriptionActivity extends AppCompatActivity {

    public static Activity descActivity;
    private Artifact artifact;
    private ImageButton nextButton;
    private String description;
    private ImageView artifactImage;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_description);
        descActivity = this;
        artifactImage = findViewById(R.id.artifactImageView);

        // Get artifact from ArtifactUploadActivity
        artifact = (Artifact) getIntent().getSerializableExtra("Artifact");
        System.out.println(artifact.getUrl());
        Glide.with(this)
                .load(artifact.getUrl())
                .fitCenter()
                .into(artifactImage);

        // Get artifact from ArtifactUploadActivity
        artifact = (Artifact) getIntent().getSerializableExtra("Artifact");

        //---------------------------------- next button -------------------------------------
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get description from user
                EditText newDesc = findViewById(R.id.descInput);
                description = newDesc.getText().toString();
                artifact.setDescription(description);
                //Open settings page
                openArtifactTags();
            }
        });
    }

    //================================== HELPER FUNCTIONS =======================================
    public void openArtifactTags() {
        Intent intent = new Intent(this, ArtifactTagsActivity.class);
        intent.putExtra("Artifact", artifact);
        startActivity(intent);

    }
}
