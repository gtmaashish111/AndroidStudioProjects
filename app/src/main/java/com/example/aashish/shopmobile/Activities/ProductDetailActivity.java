package com.example.aashish.shopmobile.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aashish.shopmobile.Product;
import com.example.aashish.shopmobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {

    TextView txtProductName,txtProductDescription,txtProductPrice;
    EditText txtQuantity;
    Button btnAddCart,btnEdit;
    String pn,pd,pp,pq;
    FirebaseAuth firebaseAuth;
    ImageView imgView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference products;
    private String pCode = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        firebaseAuth = FirebaseAuth.getInstance();


        txtProductName = findViewById(R.id.txtProductName);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtQuantity = findViewById(R.id.txtQuantity);

        btnAddCart = findViewById(R.id.btnAddCart);
        btnEdit = findViewById(R.id.btnEdit);
        imgView = findViewById(R.id.imageView10);



        Intent in = getIntent();
        pCode = in.getStringExtra("pCode");

        getDetailProduct(pCode);


        pn = in.getStringExtra("pName");
        txtProductName.setText(pn);
        txtProductName.setEnabled(false);

        pd = in.getStringExtra("pDescription");
        txtProductDescription.setText(pd);
        txtProductDescription.setEnabled(false);

       pp = in.getStringExtra("pPrice");
        txtProductPrice.setText(pp);
        txtProductPrice.setEnabled(false);





        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductDetailActivity.this,EditProduct.class);
                startActivity(in);
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToCart();
                Intent in = new Intent(ProductDetailActivity.this,CartListActivity.class);

                startActivity(in);
            }
        });
    }

    private void getDetailProduct(String productCode)
    {
        DatabaseReference prf = FirebaseDatabase.getInstance().getReference().child("All Barcodes");
        prf.child(productCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product pd = dataSnapshot.getValue(Product.class);

                Picasso.with(getBaseContext()).load(pd.getImageUrl()).into(imgView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addToCart()
    {

        String getCurrentTIme, getCurrentDate;
        Calendar callDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        getCurrentDate = currentDate.format(callDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        getCurrentTIme = currentTime.format(callDate.getTime());
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();


        DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productBarcode",pCode);
        cartMap.put("productName",pn);
        cartMap.put("productPrice",pp);
        cartMap.put("productQuantity",txtQuantity.getText().toString());
        cartMap.put("Date",getCurrentDate);
        cartMap.put("Time",getCurrentTIme);

        cartList.child("User List").child("Products").child(pCode).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProductDetailActivity.this, "Product Added to a Cart List", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(ProductDetailActivity.this, CartListActivity.class);


                        }

                    }
                });





    }
}
