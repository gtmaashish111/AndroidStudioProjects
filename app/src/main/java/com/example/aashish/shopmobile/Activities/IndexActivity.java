package com.example.aashish.shopmobile.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.aashish.shopmobile.R;

public class IndexActivity extends AppCompatActivity {

    CardView cvLogin,cvRegister,cvFAQ,cvAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        cvLogin = findViewById(R.id.cvLogin);
        cvRegister = findViewById(R.id.cvRegister);
        cvAboutUs = findViewById(R.id.cvHelp);
        cvFAQ = findViewById(R.id.cvFAQ);

        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(IndexActivity.this,MainActivity.class);
                startActivity(in);
            }
        });

        cvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(IndexActivity.this,RegisterActivity.class);
                startActivity(in);
            }
        });

        /*cvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
}
