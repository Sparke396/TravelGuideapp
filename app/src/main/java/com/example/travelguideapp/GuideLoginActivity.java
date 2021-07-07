package com.example.travelguideapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.travelguideapp.model.Guide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

public class GuideLoginActivity extends AppCompatActivity {
    ImageView guideImageView;
    private Uri filePath = null;
    boolean image = false;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;

    EditText emailEt, passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_login);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);

    }

    public void login(View view) {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("myanme", "signInWithEmail:success");
                    FirebaseUser user
                            = auth.getCurrentUser();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
                    ref.child("guides").orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) {
                            Guide guide=  snapshot.getValue(Guide.class);
                            Intent i = new Intent(GuideLoginActivity.this, GuideHomeActivity.class);
                            i.putExtra("guide", guide);
                            startActivity(i);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("myname","name : "+email+" password : "+password);
                    Log.w("myname", "signInWithEmail:failure", task.getException());
                    Toast.makeText(GuideLoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void showPopup(View v) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.guide_registration, null);
        final EditText emailEt = view.findViewById(R.id.emailEt);
        final EditText nameEt = view.findViewById(R.id.nameEt);
        final EditText mobileEt = view.findViewById(R.id.mobileEt);
        final EditText passwordEt = view.findViewById(R.id.passwordEt);
        final EditText aadharEt = view.findViewById(R.id.aadharEt);

        guideImageView = view.findViewById(R.id.guideImageView);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String name = nameEt.getText().toString();
                String mobile = mobileEt.getText().toString();
                String password = passwordEt.getText().toString();
                String aadhar = aadharEt.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(GuideLoginActivity.this, "Enter name first", Toast.LENGTH_SHORT).show();
                    nameEt.setError("Name is missing");
                    nameEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(GuideLoginActivity.this, "Enter email first", Toast.LENGTH_SHORT).show();
                    emailEt.setError("Email is missing");
                    emailEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mobile) || mobile.length() != 10) {
                    Toast.makeText(GuideLoginActivity.this, "Mobile is not valid", Toast.LENGTH_SHORT).show();
                    mobileEt.setError("Incorrect mobile number");
                    mobileEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 8) {
                    Toast.makeText(GuideLoginActivity.this, "Password length is too short", Toast.LENGTH_SHORT).show();
                    passwordEt.setError("Password length is too short");
                    passwordEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(aadhar) || aadhar.length() != 12) {
                    Toast.makeText(GuideLoginActivity.this, "Aadhar card number is not valid", Toast.LENGTH_SHORT).show();
                    aadharEt.setError("Aadhar is invalid");
                    aadharEt.requestFocus();
                    return;
                }
                if (!image) {
                    Toast.makeText(GuideLoginActivity.this, "Select image from gallery first", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(GuideLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(GuideLoginActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                        } else {

                            String imagePath = uploadImage(name, email, mobile, password, aadhar);

                        }
                    }
                });
            }
        });
        guideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGallery();
            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToGallery() {
        Intent intent = new Intent();   //implicit
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 111);

    }

    String uploadedImagePath = null;

    String uploadImage(String name, String email, String mobile, String password, String aadhar) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        uploadedImagePath = null;

        StorageReference ref = storageReference.child("images/guide_profile/" + UUID.randomUUID().toString());
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();


                // to getpath of upoaded image
                final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Toast.makeText(GuideLoginActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();

                        // saving guide into database
                        uploadedImagePath = uri.toString();
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("guides");

                        String guideId = mDatabase.push().getKey();

                        Guide guide = new Guide(guideId, name, email, mobile, password, aadhar, uploadedImagePath, new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US).format(new Date()));

                        mDatabase.child(guideId).setValue(guide);
                        Toast.makeText(GuideLoginActivity.this, "Record saved", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(GuideLoginActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                guideImageView.setImageBitmap(bitmap);
                image = true;
            } catch (IOException e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "You didn't select any image", Toast.LENGTH_SHORT).show();
        }

    }
}