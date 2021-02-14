package com.sda.syeddaniyalali.hotpot_;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.sda.syeddaniyalali.hotpot_.Ordered_final_items_collection.foodNames;

public class Card_User_Order_List_RecyclerView_Adapter extends RecyclerView.Adapter<Card_User_Order_List_RecyclerView_Adapter.ViewHolder> {

    private ArrayList<Ordered_final_items_Class> mproducts;
    Context mcontext;

    Card_User_Order_List_RecyclerView_Adapter(Context context, ArrayList<Ordered_final_items_Class> products)
    {
        mproducts=products;
        mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view= LayoutInflater.from(mcontext).inflate(R.layout.card_user_final_order_list, parent,false);
        return new Card_User_Order_List_RecyclerView_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.sda_user_order_list_name.setText(mproducts.get(i).sda_item_name);
        viewHolder.sda_user_order_list_price.setText(mproducts.get(i).sda_item_amount);
        viewHolder.sda_user_order_list_delivery_price.setText(mproducts.get(i).sda_item_delivery_price);

        Ordered_final_items_collection.totalAmount += Integer.parseInt(mproducts.get(i).sda_item_amount);
        Ordered_final_items_collection.totalAmount += Integer.parseInt(mproducts.get(i).sda_item_delivery_price);
        Ordered_final_items_collection.foodNames += mproducts.get(i).sda_item_name+"\n";

    }

    @Override
    public int getItemCount() {
        return mproducts.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView sda_user_order_list_name, sda_user_order_list_price, sda_user_order_list_delivery_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sda_user_order_list_name=itemView.findViewById(R.id.sda_user_order_list_name);
            sda_user_order_list_price=itemView.findViewById(R.id.sda_user_order_list_price);
            sda_user_order_list_delivery_price=itemView.findViewById(R.id.sda_user_order_list_quantity);
        }
    }

}
