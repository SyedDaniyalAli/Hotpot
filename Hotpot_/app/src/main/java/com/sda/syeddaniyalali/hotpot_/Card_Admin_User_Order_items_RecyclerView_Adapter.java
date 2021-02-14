package com.sda.syeddaniyalali.hotpot_;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Card_Admin_User_Order_items_RecyclerView_Adapter extends RecyclerView.Adapter<Card_Admin_User_Order_items_RecyclerView_Adapter.ViewHolder> {


    private Context mContext;
    private ArrayList<Order_Item_Class> mProducts;

    // Write a message to the database
    private DatabaseReference myRef;
    private StorageReference mStorageRef;

    Card_Admin_User_Order_items_RecyclerView_Adapter(Context mContext, ArrayList<Order_Item_Class> mProducts)
    {
        this.mContext = mContext;
        this.mProducts = mProducts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.card_admin_user_order_item, parent,false);
        myRef = FirebaseDatabase.getInstance().getReference("Images");
        return new Card_Admin_User_Order_items_RecyclerView_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.sda_admin_user_order_card_food_name.setText(mProducts.get(i).sda_Name);
        viewHolder.sda_admin_user_order_card_shop_tile.setText(mProducts.get(i).sda_Title);
        viewHolder.sda_admin_user_order_card_food_price.setText(mProducts.get(i).sda_Food_Price);
        viewHolder.sda_admin_user_order_card_delivery_price.setText(mProducts.get(i).sda_Delivery_Price);
        viewHolder.sda_admin_user_order_card_food_rating.setRating(Float.parseFloat(mProducts.get(i).sda_Rating));

        //Toast.makeText(mContext, mProducts.get(i).imagePath+"", Toast.LENGTH_SHORT).show();
        Picasso.with(mContext).load(mProducts.get(i).imagePath).placeholder(R.drawable.loading).error(R.drawable.loading).into(viewHolder.sda_admin_user_order_card_img);

        viewHolder.sda_admin_user_order_card_btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(mContext)
                        .setMessage("Do you wants to Delete?")
                        .setIcon(R.drawable.ic_delete_forever_black_24dp)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                checkRecord(mProducts.get(i).imagePath,mProducts.get(i).sda_Title);


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


    //Firebase Methods------------------------------------------------------------------------------

    void checkRecord(final String imagePath, final String sda_Title)
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    ItemsDetail item_class = ds.getValue(ItemsDetail.class);

                    if(item_class.sda_ImageUri.equals(imagePath))
                    {
                        String key = ds.getKey();
                        mProducts.clear();
                        deleteRecord(key,sda_Title );
                        deleteImage(item_class.sda_ImageUri);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void deleteRecord(String key, final String sda_Title)
    {
        DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("Images").child(key);
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

    void deleteImage(String sda_ImageUri)
    {
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(sda_ImageUri);
        mStorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "Image deleted from Storage", Toast.LENGTH_SHORT).show();
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
        ImageView sda_admin_user_order_card_img;
        TextView sda_admin_user_order_card_food_name, sda_admin_user_order_card_shop_tile, sda_admin_user_order_card_food_price,sda_admin_user_order_card_delivery_price;
        RatingBar sda_admin_user_order_card_food_rating;
        ImageButton sda_admin_user_order_card_btn_add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sda_admin_user_order_card_img=itemView.findViewById(R.id.sda_admin_user_order_card_img);
            sda_admin_user_order_card_food_name=itemView.findViewById(R.id.sda_admin_user_order_card_food_name);
            sda_admin_user_order_card_shop_tile=itemView.findViewById(R.id.sda_admin_user_order_card_shop_tile);
            sda_admin_user_order_card_food_price=itemView.findViewById(R.id.sda_admin_user_order_card_food_price);
            sda_admin_user_order_card_delivery_price=itemView.findViewById(R.id.sda_admin_user_order_card_delivery_price);
            sda_admin_user_order_card_food_rating=itemView.findViewById(R.id.sda_admin_user_order_card_food_rating);
            sda_admin_user_order_card_btn_add_to_cart=itemView.findViewById(R.id.sda_admin_user_order_card_btn_add_to_cart);

//            sda_user_order_card_btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, "Item Has been Added to Cart", Toast.LENGTH_SHORT).show();
//                }
//            });

        }
    }


}
