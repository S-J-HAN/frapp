package com.itproject.frapp.Upload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;

import com.itproject.frapp.R;

public class ArtifactDescriptionActivity extends AppCompatActivity {

    private Button nextButton;
  
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
