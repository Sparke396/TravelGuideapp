package com.example.travelguideapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelguideapp.model.Guide;
import com.example.travelguideapp.model.TourSite;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class NewTourActivity extends AppCompatActivity implements LocationListener {

    ImageView siteImageView;
    EditText siteNameView, siteDescEt, seasonEt, cityEt;
    Button saveBtn;
    String guideId;
    private Uri filePath = null;
    boolean image = false;
    FusedLocationProviderClient client;
    LocationRequest request;
    Location mLocation;
    LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            mLocation = locationResult.getLastLocation();

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tour);

        siteNameView = findViewById(R.id.siteNameET);
        siteDescEt = findViewById(R.id.descEt);
        seasonEt = findViewById(R.id.seasonEt);
        cityEt = findViewById(R.id.cityEt);
        siteImageView = findViewById(R.id.siteImageView);
        saveBtn = findViewById(R.id.saveBtn);

        guideId=getIntent().getStringExtra("guideId");

        requestLocation();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSite();
            }
        });
    }
void saveSite() {
    String siteName=siteNameView.getText().toString();
    String siteDesc=siteDescEt.getText().toString();
    String siteCity=cityEt.getText().toString();
    String siteSeason=seasonEt.getText().toString();
    if (!image) {
        Toast.makeText(NewTourActivity.this, "Select image from gallery first", Toast.LENGTH_SHORT).show();
        return;
    }
    saveDetail(siteName,siteDesc,siteCity,siteSeason);
}
String uploadedImagePath;
    String saveDetail(String siteName, String siteDesc, String siteCity, String siteSeason) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        uploadedImagePath = null;
        FirebaseStorage storage;
        requestLocation();
        StorageReference storageReference;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference ref = storageReference.child("images/tour_site/" + UUID.randomUUID().toString());
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();


                // to getpath of upoaded image
                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(NewTourActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                        // saving guide into database
                        uploadedImagePath = uri.toString();
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("tour_sites");
                        String siteId = mDatabase.push().getKey();

                        TourSite site=new TourSite(siteName,siteDesc,siteCity,mLocation.getLatitude(),mLocation.getLongitude(),guideId,uploadedImagePath,null,siteSeason);
                        mDatabase.child(siteId).setValue(site);
                        Toast.makeText(NewTourActivity.this, "Record saved", Toast.LENGTH_SHORT).show();
showCompleteDialog();
                    }
                });
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(NewTourActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
        return uploadedImagePath;
    }

    private void showCompleteDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Site saved succesfully");
        builder.setMessage("Want to add more?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           dialog.dismiss();
           siteNameView.setText("");
           siteDescEt.setText("");
           seasonEt.setText("");
           cityEt.setText("");
           siteImageView.setImageResource(android.R.drawable.ic_menu_camera);
           image=false;
            }
        });
        builder.setNeutralButton("Go back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           dialog.dismiss();
           finish();
            }
        });
        builder.create().show();
    }


    public void openGallery(View View) {
        Intent intent = new Intent();   //implicit
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 111);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                siteImageView.setImageBitmap(bitmap);
                image = true;
            } catch (IOException e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You didn't select any image", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLocation = location;
    }

    void requestLocation() {
        request = new LocationRequest();
        request.setInterval(1000 * 5);
        request.setFastestInterval(1000 * 2);

        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(Utils.SMALLEST_DISPLACEMENT);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        request.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        request.setMaxWaitTime(1000 * 20);
        client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, callback, Looper.getMainLooper());
        }
    }
}