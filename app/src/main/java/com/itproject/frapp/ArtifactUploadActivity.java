/** Code adapted from
 * https://firebase.google.com/docs/storage/android/upload-files
 * https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
 */

package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import java.util.UUID;

public class ArtifactUploadActivity extends AppCompatActivity implements Serializable {

    private Artifact artifact = new Artifact();
    private ImageButton nextButton;
    private Button uploadFromCameraButton;

    private Uri filepath;
    private String imageUri = "";
    private ImageView artifactImage;

    private FirebaseAuth mAuth;


    public final static int REQUEST_IMAGE_CAPTURE = 100;
    public final static int SELECT_PHOTO_CODE = 1046;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_upload);

        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference dbRef;
        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        uploadFromCameraButton = findViewById(R.id.uploadFromCameraButton);
        artifactImage = findViewById(R.id.artifactImageView);


        //--------------------------------- upload image --------------------------------------

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create storage reference from the app
        StorageReference storageRef = storage.getReference();
        // Create reference to new image
        final StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().
                toString());
        // Upload image to database
        uploadImage(imageRef);
        // Update URL of the artifact
        artifact.setUrl(imageUri);

        //------------------------------- next button ---------------------------------------------
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
        intent.putExtra("Artifact", artifact);
        startActivity(intent);
    }

    public void selectPhotoFromGallery(View view) {
        Intent selectPhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (selectPhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(selectPhotoIntent, SELECT_PHOTO_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handles if photo selected from gallery
        if (requestCode == SELECT_PHOTO_CODE && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            filepath = data.getData();
            Bitmap selectedPhoto = null;
            try {
                selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            artifactImage.setImageBitmap(selectedPhoto);
        }
    }

    // Upload image to Firebase storage and retrieve its URI
    public void uploadImage (final StorageReference imageRef) {
        new Thread (new Runnable () {
            @Override
            public void run() {
                if(filepath != null) {

                    // Get data from ImageView as bytes
                    artifactImage.setDrawingCacheEnabled(true);
                    artifactImage.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) artifactImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    final UploadTask uploadTask = imageRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            System.out.println("DERP upload failed");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle success
                            // taskSnapshot.getMetadata()?
                            Task<Uri> urlTask = uploadTask.continueWithTask
                                    (new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task)
                                        throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // Continue with the task to get the download URL
                                    return imageRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        imageUri = task.getResult().toString();
                                    } else {
                                        System.out.println("DERP URL retrieval failure");
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }
}