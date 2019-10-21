/* Team: frapp
 * IT Project Semester 2, 2019
 */

package com.itproject.frapp.InitialSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itproject.frapp.ComputerVision.FacialRecognition;
import com.itproject.frapp.MainGallery.HomeActivity;
import com.itproject.frapp.R;
import com.itproject.frapp.Settings.ChooseDPSetting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


/* allows user to select their DP the very first time they use the app
 * note: camera functionality has ben adapted from https://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity
 */
public class InitialDPSelection extends AppCompatActivity {

    private static final int CAMERA_REQUEST  = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private Uri filepath;
    private String imageUri = "";
    private String currentPhotoPath;
    private String imageFileName = "";
    private File cameraImageFile = null;

    public final static int SELECT_PHOTO_CODE = 1046;
    private ImageView profileImage;
    private Bitmap bitmapImage;
    private Button uploadFromCameraButton;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_dpselection);

        this.profileImage = (ImageView) findViewById(R.id.profileImageView);

        // initial setup from camera upload option
        //https://developer.android.com/training/camera/photobasics
        LinearLayout photoButton = this.findViewById(R.id.uploadFromCameraButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        try {
                            cameraImageFile = createImageFile();
                        } catch (IOException ex) {
                            System.out.println("Error occurred while creating the File");
                        }
                        // continue if file was successfully created
                        if (cameraImageFile != null) {
                            filepath = FileProvider.getUriForFile(getApplicationContext(),"com.itproject.frapp.fileprovider", cameraImageFile);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filepath);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                }
            }
        });

    }


    /* checks whether app has permission to use the camera
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    /* receives the photo from either gallery or camera then displays that photo on screen
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        // handles if uploading photo from camera
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            loadImageFromFile();
            saveProfilePicture();
        }

        // handles if photo selected from gallery
        if ((requestCode == SELECT_PHOTO_CODE) && (data != null)) {
            this.filepath = data.getData();
            this.bitmapImage = null;
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImage.setImageBitmap(cropImage(bitmapImage));
            saveProfilePicture();
        }

    }


    private void loadImageFromFile() {
        if (this.cameraImageFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(this.cameraImageFile.getAbsolutePath());
            profileImage.setImageBitmap(cropImage(myBitmap));
        } else {
            System.out.println("Error: File does not exist");
        }
    }



    // Upload image to Firebase storage and retrieve its URI
    public void uploadImage (final StorageReference imageRef) {
        Toast.makeText(this, "Saving image ... this may take a few moments", Toast.LENGTH_LONG).show();
        new Thread (new Runnable () {
            @Override
            public void run() {
                if(filepath != null) {
                    // Get data from ImageView as bytes
                    profileImage.setDrawingCacheEnabled(true);
                    profileImage.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
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
                                        System.out.println("Image URI = " + imageUri);

                                        // Authenticate current user
                                        mAuth = FirebaseAuth.getInstance();
                                        final FirebaseUser currentUser = mAuth.getCurrentUser();

                                        // Connect to database
                                        dbRef = FirebaseDatabase.getInstance().getReference();
                                        dbRef.child("users").child(currentUser.getUid()).child("url").setValue(imageUri);

                                        Toast.makeText(InitialDPSelection.this, "Profile image saved", Toast.LENGTH_LONG).show();

                                        // Add face as a reference photo for facial recognition
                                        dbRef.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Map<String, String> data = (Map<String, String>) dataSnapshot.getValue();
                                                FacialRecognition.addPersonReferenceFace(getApplicationContext(), data.get("azurePersonID"), imageUri);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

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



    /* on select, beings the intent for selecting a photo from the phones gallery
     */
    public void selectPhotoFromGallery(View view) {
        Intent selectPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (selectPhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(selectPhotoIntent, SELECT_PHOTO_CODE);
        }
    }



    /* crops the image to a square
     */
    private Bitmap cropImage(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;

        int cropWidth = (width - height) / 2;
        cropWidth = (cropWidth < 0) ? 0 : cropWidth;
        int cropHeight = (height - width) / 2;
        cropHeight = (cropHeight < 0) ? 0 : cropHeight;

        return Bitmap.createBitmap(image, cropWidth, cropHeight, newWidth, newHeight);
    }


    // https://developer.android.com/training/camera/photobasics
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void saveProfilePicture() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create storage reference from the app
        StorageReference storageRef = storage.getReference();
        // Create reference to new image
        final StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().
                toString());
        // Upload image to database
        uploadImage(imageRef);
    }


    /* Move the app to HomeActivity
     */
    public void openHomePage(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
