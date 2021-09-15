package com.example.aashish.shopmobile.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aashish.shopmobile.Product;
import com.example.aashish.shopmobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity {



    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    Button btnCamera,btnGetData;
    EditText txtBarCode;
    String barCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Search Product");
        setContentView(R.layout.activity_scan);

        btnCamera = findViewById(R.id.btnCamera);
        btnGetData = findViewById(R.id.btnGetData);
        txtBarCode = findViewById(R.id.txtBarcode);

        final Activity activity = this;
        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator in = new IntentIntegrator(activity);
                in.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                in.setPrompt("Scan");
                in.setCameraId(0);
                in.setBeepEnabled(false);
                in.initiateScan();;
            }
        });

        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barc = txtBarCode.getText().toString();
                reference = database.getReference("All Barcodes").child(barc);

                if(TextUtils.isEmpty(barc))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter or Scan a BarCode", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Product po = dataSnapshot.getValue(Product.class);



                            if (po != null) {

                                String pCode = po.barcode;
                                String pName = po.productName;
                                String pDescription = po.productDescription;
                                String pPrice = po.productPrice;
                                String pQuantity = po.productQuantity;

                               Intent in = new Intent(ScanActivity.this, ProductDetailActivity.class);
                                in.putExtra("pCode",pCode);
                                in.putExtra("pName", pName);
                                in.putExtra("pDescription", pDescription);
                                in.putExtra("pPrice", pPrice);
                                in.putExtra("pQuantity", pQuantity);

                                startActivity(in);


                            }
                            else {
                                Toast.makeText(ScanActivity.this, "Barcode Not Found", Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w(" ", "Error", databaseError.toException());

                        }
                    };
                    reference.addValueEventListener(valueEventListener);
                }

            }
        });








    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null)
        {
            if(result.getContents() == null)
            {
                Log.d("MainActivity","Cancelled Scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.d("MainActivity","Scanned");
                txtBarCode.setText(result.getContents());
                Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();
                barCode=txtBarCode.getText().toString();

            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
