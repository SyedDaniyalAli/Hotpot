package com.sda.syeddaniyalali.hotpot_;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Card_Seller_Ordered_List_RecyclerView_Adapter extends RecyclerView.Adapter<Card_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<User_Ordered_Details> mProducts;

    Card_Seller_Ordered_List_RecyclerView_Adapter(Context mContext, ArrayList<User_Ordered_Details> mProducts)
    {
        this.mContext = mContext;
        this.mProducts = mProducts;
    }


    @NonNull
    @Override
    public Card_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.card_seller_orders, parent,false);
        return new Card_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Card_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder viewHolder, final int i) {

        viewHolder.sda_seller_order_list_name.setText(mProducts.get(i).sda_foodName);
        viewHolder.sda_seller_order_list_price.setText(mProducts.get(i).sda_amount);
        viewHolder.sda_seller_order_list_phone.setText(mProducts.get(i).sda_Phone);
        viewHolder.sda_seller_order_list_address.setText(mProducts.get(i).sda_address);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView sda_seller_order_list_name, sda_seller_order_list_price, sda_seller_order_list_phone,sda_seller_order_list_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sda_seller_order_list_name=itemView.findViewById(R.id.sda_seller_order_list_name);
            sda_seller_order_list_price=itemView.findViewById(R.id.sda_seller_order_list_price);
            sda_seller_order_list_phone=itemView.findViewById(R.id.sda_seller_order_list_phone);
            sda_seller_order_list_address=itemView.findViewById(R.id.sda_seller_order_list_address);

        }
    }
}
