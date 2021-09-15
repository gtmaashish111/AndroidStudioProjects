package com.example.aashish.shopmobile.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.Toast;

import com.example.aashish.shopmobile.Product;
import com.example.aashish.shopmobile.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class HomeActivity extends AppCompatActivity {

    ImageView imgProduct;
    final Activity activity=this;
    EditText txtBarcode,txtProductName,txtProductDescription,txtProductPrice,txtQuantity;
    Button btnAdd;
    FirebaseDatabase db;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    private  static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference ProductImageRef;
    static int PReqCode = 1;
    String downloadUrl;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        imgProduct = (ImageView) findViewById(R.id.imgCamera);
        txtBarcode = (EditText) findViewById(R.id.txtBarcode);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtQuantity = findViewById(R.id.txtProductQuantity);
        btnAdd = findViewById(R.id.btnAdd);
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product");

        Intent in = getIntent();
        final String pn = in.getStringExtra("txtC");
        txtBarcode.setText(pn);


        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        reference = db.getReference("All Barcodes");


        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtB = pn;
                String txtPN = txtProductName.getText().toString();
                String txtPD = txtProductDescription.getText().toString();
                String txtP = txtProductPrice.getText().toString();
                String txtPQ = txtQuantity.getText().toString();

                if(downloadUrl == null)
                {
                    Toast.makeText(HomeActivity.this,"Please select Product",Toast.LENGTH_LONG).show();
                }
                if(ImageUri == null)
                {
                    Toast.makeText(HomeActivity.this,"Please select Product Image",Toast.LENGTH_LONG).show();

                }
                if(txtB.isEmpty() || txtPN.isEmpty() || txtP.isEmpty() || txtPD.isEmpty() || txtPQ.isEmpty())
                {
                    showMessage("Please Insert all Fields");
                }
                else {
                        Product pro = new Product(txtB, txtPN, txtPD, txtP, txtPQ, downloadUrl);

                        Toast.makeText(getApplicationContext(), "Already Have the similar Barcode", Toast.LENGTH_LONG).show();

                        reference.child(txtB).setValue(pro);
                        showMessage("Data Successfully Saved");

                        txtBarcode.setText("");
                        txtProductName.setText("");
                        txtProductDescription.setText("");
                        txtProductPrice.setText("");
                        txtQuantity.setText("");

                    }
                }



        });
    }

    public void StoreProduct()
    {
        final StorageReference file = ProductImageRef.child(ImageUri.getLastPathSegment());

        if(ImageUri.getLastPathSegment() == null)
        {
            showMessage("Please Select a Image");

        }

        final UploadTask uploadTask = file.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error" + e);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                showMessage("Image Uploaded Successfully");

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw  task.getException();

                        }
                        downloadUrl = file.getDownloadUrl().toString();
                        return file.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful())
                        {
                            downloadUrl = task.getResult().toString();

                            showMessage("Product Image saved Successfully");
                        }

                    }
                });
            }
        });

    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick && resultCode ==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            imgProduct.setImageURI(ImageUri);
            StoreProduct();
        }
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(HomeActivity.this,"Please accept for required ppermission",Toast.LENGTH_SHORT).show();

            }
            else
            {
                ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);

            }

        }
        else
        {
            OpenGallery();

        }

    }



    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}
