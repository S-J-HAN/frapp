package com.itproject.frapp.Upload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.os.Bundle;

import com.itproject.frapp.R;
import com.itproject.frapp.Schema.Artifact;

public class ArtifactDescriptionActivity extends AppCompatActivity {

    private Artifact artifact;
    private ImageButton nextButton;
    private String description;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_description);

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
