package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;

public class ArtifactUploadActivity extends AppCompatActivity {

    private ImageView profileImage;

    private ImageButton nextButton;
    private Button uploadFromCameraButton;

    public final static int REQUEST_IMAGE_CAPTURE = 100;
    public final static int SELECT_PHOTO_CODE = 1046;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_upload);

        //--------------------------- next button ---------------------------
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open next page
                openArtifactDate();
            }
        });

        //----------------------- upload from camera button -------------------------
        uploadFromCameraButton = findViewById(R.id.uploadFromCameraButton);
    }

    //==================================== HELPER FUNCTIONS =======================================
    public void openArtifactDate() {
        Intent intent = new Intent(this, ArtifactDateActivity.class);
        startActivity(intent);
    }

    public void selectPhotoFromGallery(View view) {
        Intent selectPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (selectPhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(selectPhotoIntent, SELECT_PHOTO_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handles if photo selected from gallery
        if ((requestCode == SELECT_PHOTO_CODE) && (data != null)) {
            Uri selectedPhotoUri = data.getData();
            Bitmap selectedPhoto = null;
            try {
                selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImage.setImageBitmap(selectedPhoto);
        }
    }

}