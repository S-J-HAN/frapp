package com.itproject.frapp.InitialSignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itproject.frapp.MainGallery.HomeActivity;
import com.itproject.frapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InitialDPSelection extends AppCompatActivity {

    public final static int REQUEST_CAPTURE_IMAGE  = 100;
    public final static int SELECT_PHOTO_CODE = 1046;

    private Uri file;

    private String currentPhotoPath;

    private ImageView profileImage;
    private Button uploadFromCameraButton;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_dpselection);

        // Authenticate current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Connect to database
        dbRef = FirebaseDatabase.getInstance().getReference();

        uploadFromCameraButton = (Button) findViewById(R.id.uploadFromCameraButton);
        profileImage = (ImageView) findViewById(R.id.profileImageView);


        // check have permission to use camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            uploadFromCameraButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        //Button next = findViewById(R.id.nextButton4);
        //next.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {

        // Insert stuff for uploading photo here

        // Move on the the next page - font settings
        //        openHomePage();
        //    }
        //});
    }

//    //@Override
//    public void onRequestPermissionsResults(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == 0) {
//            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
//                uploadFromCameraButton.setEnabled(true);
//            }
//        }
//    }


    public void takePhoto(View view) {
        openCameraIntent();
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }




//    public void dispatchTakePictureIntent(View view) {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
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


    public void selectPhotoFromGallery(View view) {
        Intent selectPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (selectPhotoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(selectPhotoIntent, SELECT_PHOTO_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handles if photo taken from camera
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                ImageView mImageView = new ImageView(this);
                mImageView.setImageBitmap(imageBitmap);
            }
        }

        // handles if photo selected from gallery
        if ((requestCode == SELECT_PHOTO_CODE) && (data != null)) {
            Uri selectedPhotoUri = data.getData();
            Bitmap selectedPhoto = null;
            try {
                selectedPhoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedPhotoUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImage.setImageBitmap(cropImage(selectedPhoto));

//            // Upload photo onto firebase storage
//            UploadTask uploadTask = mountainsRef.putBytes(data);
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                    // ...
//                }
//            });
        }


    }


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


//    private static File getOutputMediaFile() {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");
//
//        if ((!mediaStorageDir.exists()) && (!mediaStorageDir.mkdirs())) {
//            return null;
//        }
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
//    }


    public void openHomePage(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
