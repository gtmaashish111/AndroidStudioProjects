package com.example.aashish.shopmobile.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aashish.shopmobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private Button btnScan;
    private FirebaseAuth firebaseAuth;
    private Intent Productindex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = (EditText)findViewById(R.id.txtEmail);
        userPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnSignUp = (Button)findViewById(R.id.btnSignup);

        Productindex = new Intent(this, com.example.aashish.shopmobile.Activities.NavMainActivity.class);
        firebaseAuth = FirebaseAuth.getInstance();




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty())
                {
                   showMessage( "Plese Verify all the fields");

                }
                else
                {
                    SignIn(email,password);
                }
            }
        });



        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });



        }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void SignIn(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();

                }
                else {
                    showMessage(task.getException().getMessage());

                }
            }
        });
    }

    private void updateUI() {
        startActivity(Productindex);
        finish();
    }

    ;


    }

