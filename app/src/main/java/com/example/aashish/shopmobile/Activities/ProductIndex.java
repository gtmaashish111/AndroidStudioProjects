package com.example.aashish.shopmobile.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.aashish.shopmobile.R;

public class ProductIndex extends AppCompatActivity {

    CardView cvScan, cvSearchP, cvGenerate, cvAddCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cvScan = findViewById(R.id.cvScan);
        cvSearchP = findViewById(R.id.cvSearch);
        cvGenerate = findViewById(R.id.cvGenerate);
        cvAddCustomer = findViewById(R.id.cvAddCustomer);

        cvScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductIndex.this, ScanActivity.class);
                startActivity(in);
            }
        });

        cvGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductIndex.this, BarGeneratorActivity.class);
                startActivity(in);
            }
        });

        cvAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductIndex.this, AddCustomerActivity.class);
                startActivity(in);
            }
        });

        /*cvSearchP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProductIndex.this,ScanActivity.class);
                startActivity(in);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

    }
}