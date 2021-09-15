package com.example.aashish.shopmobile.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aashish.shopmobile.Customer;
import com.example.aashish.shopmobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCustomerActivity extends AppCompatActivity {
    private EditText userFullName, userAddress, userPhone, userEmail;
    private Button btnAdd;
    FirebaseDatabase db;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        userFullName = (EditText)findViewById(R.id.txtFullName1);
        userAddress = (EditText)findViewById(R.id.txtAddress2);
        userPhone = (EditText)findViewById(R.id.txtPhone2);
        userEmail = (EditText)findViewById(R.id.txtEmail2);

        btnAdd = (Button)findViewById(R.id.btnAdd);



        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        reference = db.getReference("Customers");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname = userFullName.getText().toString();
                final String address = userAddress.getText().toString();
                final String phone = userPhone.getText().toString();
                final String email = userEmail.getText().toString();

                if (fullname.isEmpty() || address.isEmpty()||phone.isEmpty()|| email.isEmpty())
                {
                    // Display an Error message
                    Toast.makeText(getApplicationContext(), "Please ENter all the fields", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Customer pro = new Customer(fullname,email,address,phone);



                    reference.child(fullname).setValue(pro);
                    Toast.makeText(getApplicationContext(), "Data Successfully Saved", Toast.LENGTH_LONG).show();


                    userFullName.setText("");
                    userAddress.setText("");
                    userEmail.setText("");
                    userPhone.setText("");

                }
            }
        });




    }
}
