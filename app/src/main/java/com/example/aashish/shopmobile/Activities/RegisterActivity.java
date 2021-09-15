package com.example.aashish.shopmobile.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aashish.shopmobile.R;
import com.example.aashish.shopmobile.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView imgUser;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImageUrl;

    private EditText userFullName, userAddress, userPhone, userDOB, userPassword, userConfirmPassword, userEmail, userName;
    private ProgressBar loading;

    private Button btnSignUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userFullName = (EditText)findViewById(R.id.txtFullName1);
        userAddress = (EditText)findViewById(R.id.txtAddress);
        userPhone = (EditText)findViewById(R.id.txtPhone);
        userDOB = (EditText)findViewById(R.id.txtDOB);
        userEmail = (EditText)findViewById(R.id.txtEmail);
        userName = (EditText)findViewById(R.id.txtEmail);
        userPassword = (EditText)findViewById(R.id.txtPassword);
        userConfirmPassword = (EditText)findViewById(R.id.txtConfirmPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);


        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String fullname = userFullName.getText().toString();
                final String address = userAddress.getText().toString();
                final String phone = userPhone.getText().toString();
                final String dob = userDOB.getText().toString();
                final String username = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String confirmpassword = userConfirmPassword.getText().toString();


                if (fullname.isEmpty() || address.isEmpty()||phone.isEmpty()||dob.isEmpty()||username.isEmpty()||password.isEmpty()||confirmpassword.isEmpty())
                {
                    // Display an Error message
                    showMessage("Please Insert all Fields");
                    btnSignUp.setVisibility(View.VISIBLE);

                }
                else
                {
                    // every thing is fine
                    // CreateUSerAccount method will try to create the user if the email is valid.
                    CreateUserAccount(fullname,address,phone,dob,username,email,password);
                }



            }
        });



        imgUser = findViewById(R.id.imgUser);
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();

                }
            }
        });
    }

    private void CreateUserAccount(final String fullname,final String address,final String phone,final String dob,final String username,final String email, String password) {
        // this method create user Account with specific email and password
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        // user account created successfully


                        User us = new User(fullname,address,phone,dob,username,email);
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(us).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                    showMessage("Account Created");

                            }
                        });

                        // after we created a user account we need to update the profile picture
                        updateUserInfo(fullname, pickedImageUrl,firebaseAuth.getCurrentUser());

                    }
                    else
                    {
                        showMessage("Account creation failed" + task.getException().getMessage());
                        btnSignUp.setVisibility(View.VISIBLE);
                    }
                }
            })    ;

    }

    private void updateUserInfo(final String fullname, Uri pickedImageUrl, final FirebaseUser currentUser) {
        // upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImageUrl.getLastPathSegment());
        imageFilePath.putFile(pickedImageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image Uploaded Successfully
                // We can get out Image URi

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        // uri contain user image url


                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(fullname)
                                .setPhotoUri(uri).build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            showMessage("Registration Completed");
                                            UpdateUI();

                                        }
                                    }
                                });
                    }
                });

            }
        });

    }

    private void UpdateUI() {
        Intent homeActivity = new Intent(getApplicationContext(),ProductIndex.class);
        startActivity(homeActivity);
        finish();
    }

    // Simple method to show Toast Message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null)
        {
            // user has pickde an image successfully
            pickedImageUrl = data.getData();
            imgUser.setImageURI(pickedImageUrl);

        }
    }

    private void openGallery() {
        // TODO: open gallery intent and wait for user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(RegisterActivity.this,"Please accept for required ppermission",Toast.LENGTH_SHORT).show();

            }
            else
            {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);

            }

        }
        else
        {
            openGallery();

        }

    }
}
