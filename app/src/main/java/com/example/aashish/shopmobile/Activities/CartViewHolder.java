package com.example.aashish.shopmobile.Activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.aashish.shopmobile.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductQuantity, txtProductPrice;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.txtProductName);
        txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
        txtProductQuantity = itemView.findViewById(R.id.txtProductQuantity);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
