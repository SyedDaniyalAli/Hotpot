package com.sda.syeddaniyalali.hotpot_;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_Seller_Details_Fragment extends android.app.Fragment {

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Orders");

    Card_Admin_Seller_Ordered_List_RecyclerView_Adapter mAdapter;
    RecyclerView recyclerView;
    View view;
    Context context;

    private ArrayList<User_Ordered_Details> user_ordered_details=new ArrayList<>();

    public Admin_Seller_Details_Fragment()
    {

    }

    @SuppressLint("ValidFragment")
    public Admin_Seller_Details_Fragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin__seller__details_, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.sda_rv_admin_seller_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.hasFixedSize();

        loadItems();

        return view;
    }

    void loadItems()
    {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_ordered_details.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {   // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User_Ordered_Details value = ds.getValue(User_Ordered_Details.class);
                    user_ordered_details.add(new User_Ordered_Details(value.sda_foodName,value.sda_amount,value.sda_address, value.sda_Phone));

                }
                mAdapter = new Card_Admin_Seller_Ordered_List_RecyclerView_Adapter(context,user_ordered_details);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(context, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
