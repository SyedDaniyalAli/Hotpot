package com.sda.syeddaniyalali.hotpot_;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Card_Admin_Seller_Ordered_List_RecyclerView_Adapter extends RecyclerView.Adapter<Card_Admin_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder> {

    private Context mContext;
    private ArrayList<User_Ordered_Details> mProducts;

    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;

    Card_Admin_Seller_Ordered_List_RecyclerView_Adapter(Context mContext, ArrayList<User_Ordered_Details> mProducts)
    {
        this.mContext = mContext;
        this.mProducts = mProducts;
    }


    @NonNull
    @Override
    public Card_Admin_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.card_admin_seller_orders, parent,false);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Orders");
        return new Card_Admin_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Card_Admin_Seller_Ordered_List_RecyclerView_Adapter.ViewHolder viewHolder, final int i) {

        viewHolder.sda_admin_seller_order_list_name.setText(mProducts.get(i).sda_foodName);
        viewHolder.sda_admin_seller_order_list_price.setText(mProducts.get(i).sda_amount);
        viewHolder.sda_admin_seller_order_list_phone.setText(mProducts.get(i).sda_Phone);
        viewHolder.sda_admin_seller_order_list_address.setText(mProducts.get(i).sda_address);


        viewHolder.sda_btn_admin_del_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(mContext)
                        .setMessage("Do you wants to Delete?")
                        .setIcon(R.drawable.ic_delete_forever_black_24dp)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                loadItems(mProducts.get(i).sda_foodName);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create().show();
            }
        });
    }


    private void loadItems(final String value)
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    User_Ordered_Details item_class = ds.getValue(User_Ordered_Details.class);
                    if(item_class.sda_foodName.equals(value))
                    {
                        String key = ds.getKey();
                        deleteRecord(key,"Order:"+key);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Firebase Methods------------------------------------------------------------------------------
    private void deleteRecord(String key, final String sda_Title)
    {
        DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("Orders").child(key);
        myRef2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, sda_Title+" has been Deleted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, sda_Title+" failed to Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Firebase Methods------------------------------------------------------------------------------


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton sda_btn_admin_del_items;
        TextView sda_admin_seller_order_list_name, sda_admin_seller_order_list_price, sda_admin_seller_order_list_phone,sda_admin_seller_order_list_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sda_btn_admin_del_items = itemView.findViewById(R.id.sda_btn_admin_del_items);
            sda_admin_seller_order_list_name=itemView.findViewById(R.id.sda_admin_seller_order_list_name);
            sda_admin_seller_order_list_price=itemView.findViewById(R.id.sda_admin_seller_order_list_price);
            sda_admin_seller_order_list_phone=itemView.findViewById(R.id.sda_admin_seller_order_list_phone);
            sda_admin_seller_order_list_address=itemView.findViewById(R.id.sda_admin_seller_order_list_address);

        }
    }
}
