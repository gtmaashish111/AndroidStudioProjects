package com.example.aashish.shopmobile.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.aashish.shopmobile.R;

public class EditProduct extends AppCompatActivity {

    EditText txtBarcode,txtProductName,txtProductDescription,txtProductPrice,txtQuantity;
    Button btnAddCart,btnEdit,btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        txtBarcode = (EditText) findViewById(R.id.txtBarcode);
        txtProductName = findViewById(R.id.txtProductName);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtQuantity = findViewById(R.id.txtProductQuantity);

        btnEdit = findViewById(R.id.btnEdit);


        Intent in = getIntent();

       final String pCode = in.getStringExtra("pCode");
       txtBarcode.setText(pCode);
        txtBarcode.setEnabled(false);


        final String pn = in.getStringExtra("pName");
        txtProductName.setText(pn);


        final String pd = in.getStringExtra("pDescription");
        txtProductDescription.setText(pd);


        final String pp = in.getStringExtra("pPrice");
       txtProductPrice.setText(pp);


        final String pq = in.getStringExtra("pQuantity");
        txtQuantity.setText(pq);
        txtQuantity.setEnabled(false);

    }
}
