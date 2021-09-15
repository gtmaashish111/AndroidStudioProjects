package com.example.aashish.shopmobile.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aashish.shopmobile.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartListActivity extends AppCompatActivity {


    private RecyclerView rv;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnFinish;
    private TextView txtPrice;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        rv = findViewById(R.id.cartList);
        rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);



        btnFinish = (Button) findViewById(R.id.btnFinal);
        txtPrice = findViewById(R.id.txtTotalPrice);





    }


    @Override
    protected void onStart()
        {
            super.onStart();

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance()
                    .getReference().child("Cart List");

            FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions
                    .Builder<Cart>().setQuery(cartListRef.child("User List").child("Products")
                    ,Cart.class).build();

            FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                    holder.txtProductQuantity.setText(model.getProductQuantity());
                    holder.txtProductName.setText(model.getProductName());
                    holder.txtProductPrice.setText(model.getProductPrice() + "$");
try {
    int price = (Integer.parseInt(model.getProductPrice()));
    int quantity = (Integer.parseInt(model.getProductQuantity()));
    int oneTypeProductTprice = price * quantity;
    overTotalPrice = overTotalPrice + oneTypeProductTprice;
    txtPrice.setText("Total Price: $" + String.valueOf(overTotalPrice));
}
                 catch(NumberFormatException e){
                        Log.e("IO","IO"+e);

                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() { //if the user click on any item in cart activity
                        @Override
                        public void onClick(View v)
                        {
                            CharSequence options[] = new CharSequence[] //this will popup with options when any item is clicked
                                    {
                                            "Edit",
                                            "Remove"
                                    };
                            AlertDialog.Builder builder = new AlertDialog.Builder(CartListActivity.this);
                            builder.setTitle("Cart Options:");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    if(which == 0)
                                    {
                                        Intent intent = new Intent(CartListActivity.this, ProductDetailActivity.class);
                                        intent.putExtra("pCode", model.getProductCode());
                                        startActivity(intent);
                                    }
                                    if(which == 1)
                                    {
                                        cartListRef.child("User List").child("Products")
                                                .child(model.getProductCode())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task)
                                                    {
                                                        if (task.isSuccessful())
                                                        {
                                                            Toast.makeText(CartListActivity.this, "Item Removed Successfully.", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(CartListActivity.this, NavMainActivity.class);
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });
                                    }
                                }
                            });
                            builder.show();
                        }
                    });

                }
                @NonNull
                @Override
                public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);

                    CartViewHolder holder = new CartViewHolder(view);
                    return holder;
                }
            };

         rv.setAdapter(adapter);
         adapter.startListening();
        }




    }

