package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import android.os.Bundle;

public class ArtifactDescriptionActivity extends AppCompatActivity {

    private ImageButton nextButton;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_description);
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open settings page
                openArtifactTags();
            }
        });
    }

    public void openArtifactTags() {
        Intent intent = new Intent(this, ArtifactTagsActivity.class);
        startActivity(intent);

    }
}
