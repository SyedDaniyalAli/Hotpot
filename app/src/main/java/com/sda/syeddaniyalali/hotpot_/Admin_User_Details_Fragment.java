package com.sda.syeddaniyalali.hotpot_;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_User_Details_Fragment extends android.app.Fragment {

    // Write a message to the database
    private DatabaseReference myRef;
    private StorageReference mStorageRef;

    Card_Admin_User_Order_items_RecyclerView_Adapter mAdapter;
    RecyclerView recyclerView;
    View view;
    Context context;

    boolean condition=true;


    private ArrayList<Order_Item_Class> orderItem=new ArrayList<Order_Item_Class>();

    public Admin_User_Details_Fragment()
    {

    }

    @SuppressLint("ValidFragment")
    public Admin_User_Details_Fragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin__user__details_, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.sda_rv_admin_user_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.hasFixedSize();

       myRef = FirebaseDatabase.getInstance().getReference("Images");

        hotMeals();

        return view;
    }

    private void hotMeals()
    {
        orderItem.clear();
        condition=true;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                setItems(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(context, ""+error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setItems(DataSnapshot dataSnapshot)
    {
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            ItemsDetail itemsDetail = ds.getValue(ItemsDetail.class);
            orderItem.add(new Order_Item_Class(itemsDetail.sda_ImageUri,itemsDetail.sda_ChiefName,itemsDetail.sda_FoodName,
                    itemsDetail.sda_FoodPrice,itemsDetail.sda_DeliveryPrice,itemsDetail.sda_Rating));
        }
        mAdapter = new Card_Admin_User_Order_items_RecyclerView_Adapter(context, orderItem);
        recyclerView.setAdapter(mAdapter);
    }
}
