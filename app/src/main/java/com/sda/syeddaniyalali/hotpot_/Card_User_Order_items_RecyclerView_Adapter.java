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

public class Card_User_Order_items_RecyclerView_Adapter extends RecyclerView.Adapter<Card_User_Order_items_RecyclerView_Adapter.ViewHolder> {


    private Context mContext;
    private ArrayList<Order_Item_Class> mProducts;

    Card_User_Order_items_RecyclerView_Adapter(Context mContext, ArrayList<Order_Item_Class> mProducts)
    {
        this.mContext = mContext;
        this.mProducts = mProducts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.card_user_order_item, parent,false);
        return new Card_User_Order_items_RecyclerView_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.sda_user_order_card_food_name.setText(mProducts.get(i).sda_Name);
        viewHolder.sda_user_order_card_shop_tile.setText(mProducts.get(i).sda_Title);
        viewHolder.sda_user_order_card_food_price.setText(mProducts.get(i).sda_Food_Price);
        viewHolder.sda_user_order_card_delivery_price.setText(mProducts.get(i).sda_Delivery_Price);
        viewHolder.sda_user_order_card_food_rating.setRating(Float.parseFloat(mProducts.get(i).sda_Rating));

        //Toast.makeText(mContext, mProducts.get(i).imagePath+"", Toast.LENGTH_SHORT).show();
        Picasso.with(mContext).load(mProducts.get(i).imagePath).placeholder(R.drawable.loading).error(R.drawable.loading).into(viewHolder.sda_user_order_card_img);

        viewHolder.sda_user_order_card_btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Toast.makeText(mContext, mProducts.get(i).sda_Title+" has been Added to Cart", Toast.LENGTH_SHORT).show();

                Ordered_final_items_collection.items_collection.add(
                        new Ordered_final_items_Class(
                                mProducts.get(i).sda_Title,
                                mProducts.get(i).sda_Food_Price,
                                mProducts.get(i).sda_Delivery_Price));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView sda_user_order_card_img;
        TextView sda_user_order_card_food_name, sda_user_order_card_shop_tile, sda_user_order_card_food_price,sda_user_order_card_delivery_price;
        RatingBar sda_user_order_card_food_rating;
        ImageButton sda_user_order_card_btn_add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sda_user_order_card_img=itemView.findViewById(R.id.sda_user_order_card_img);
            sda_user_order_card_food_name=itemView.findViewById(R.id.sda_user_order_card_food_name);
            sda_user_order_card_shop_tile=itemView.findViewById(R.id.sda_user_order_card_shop_tile);
            sda_user_order_card_food_price=itemView.findViewById(R.id.sda_user_order_card_food_price);
            sda_user_order_card_delivery_price=itemView.findViewById(R.id.sda_user_order_card_delivery_price);
            sda_user_order_card_food_rating=itemView.findViewById(R.id.sda_user_order_card_food_rating);
            sda_user_order_card_btn_add_to_cart=itemView.findViewById(R.id.sda_user_order_card_btn_add_to_cart);

//            sda_user_order_card_btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, "Item Has been Added to Cart", Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }


}
