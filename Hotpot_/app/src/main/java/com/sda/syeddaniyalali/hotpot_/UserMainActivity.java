package com.sda.syeddaniyalali.hotpot_;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity {

    private Button sda_btn_hotMeals, sda_btn_deal_of_day;
    private ImageButton sda_btn_user_cart;
    private EditText sda_search;
    private TextView sda_item_count_number;
    private Card_User_Order_items_RecyclerView_Adapter mAdapter;
    private RecyclerView recyclerView;


    // Write a message to the database
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Images");

    private ArrayList<Order_Item_Class> orderItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        //Finding Ids
        sda_btn_hotMeals = findViewById(R.id.sda_btn_hotMeals);
        sda_btn_deal_of_day = findViewById(R.id.sda_btn_deal_of_day);
        sda_btn_user_cart = findViewById(R.id.sda_btn_user_cart);
        sda_search = findViewById(R.id.sda_search);
        sda_item_count_number = findViewById(R.id.sda_item_count_number);
        sda_item_count_number.setVisibility(View.INVISIBLE);
        orderItem = new ArrayList<Order_Item_Class>();

        recyclerView = (RecyclerView) findViewById(R.id.sda_rv_orders);
        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);




        hotMeals();


        sda_btn_hotMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotMeals();
            }
        });

        sda_btn_deal_of_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMeals();
            }
        });


        sda_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                findFood(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        sda_btn_user_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMainActivity.this, OrderedBillActivity.class);
                startActivity(intent);
            }
        });
    }

    private void hotMeals()
    {
        orderItem.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    ItemsDetail itemsDetail = ds.getValue(ItemsDetail.class);
                    orderItem.add(new Order_Item_Class(itemsDetail.sda_ImageUri,itemsDetail.sda_ChiefName,itemsDetail.sda_FoodName,
                            itemsDetail.sda_FoodPrice,itemsDetail.sda_DeliveryPrice,itemsDetail.sda_Rating));
                }

                mAdapter = new Card_User_Order_items_RecyclerView_Adapter(UserMainActivity.this, orderItem);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(UserMainActivity.this, ""+error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void findFood(final String findWord)
    {
        orderItem.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                    ItemsDetail itemsDetail = ds.getValue(ItemsDetail.class);
                    if(itemsDetail.sda_FoodName.contains(findWord))
                    {
                        orderItem.add(new Order_Item_Class(itemsDetail.sda_ImageUri,itemsDetail.sda_ChiefName,itemsDetail.sda_FoodName,
                                itemsDetail.sda_FoodPrice,itemsDetail.sda_DeliveryPrice,itemsDetail.sda_Rating));
                    }
                }

                mAdapter = new Card_User_Order_items_RecyclerView_Adapter(UserMainActivity.this, orderItem);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(UserMainActivity.this, ""+error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMeals()
    {
        orderItem.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int start=1, limit = 1;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    if(start==limit)
                    {
                        ItemsDetail itemsDetail = ds.getValue(ItemsDetail.class);
                        orderItem.add(new Order_Item_Class(itemsDetail.sda_ImageUri,itemsDetail.sda_ChiefName,itemsDetail.sda_FoodName,
                                itemsDetail.sda_FoodPrice,itemsDetail.sda_DeliveryPrice,itemsDetail.sda_Rating));
                        limit++;
                    }

                }

                mAdapter = new Card_User_Order_items_RecyclerView_Adapter(UserMainActivity.this, orderItem);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(UserMainActivity.this, ""+error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
