package com.sda.syeddaniyalali.hotpot_;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.sda.syeddaniyalali.hotpot_.Ordered_final_items_collection.foodNames;
import static com.sda.syeddaniyalali.hotpot_.Ordered_final_items_collection.items_collection;

public class OrderedBillActivity extends AppCompatActivity {

    RecyclerView sda_rv_ordered_bill;
    TextView sda_txt_total_amount;
    EditText sda_txt_order_address, sda_txt_order_phone;
    Button sda_btn_submit_order_detail;
    private RecyclerView.LayoutManager layoutManager;
    private Card_User_Order_List_RecyclerView_Adapter mAdapter;
    final static int time=3000;
    private long orderNumber=System.currentTimeMillis();

    private ArrayList<Ordered_final_items_Class> final_orderItem = new ArrayList<Ordered_final_items_Class>();
    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_bill);


        sda_rv_ordered_bill = findViewById(R.id.sda_rv_ordered_bill);
        sda_txt_total_amount = findViewById(R.id.sda_txt_total_amount);
        sda_txt_order_address = findViewById(R.id.sda_txt_order_address);
        sda_txt_order_phone = findViewById(R.id.sda_txt_order_phone);
        sda_btn_submit_order_detail = findViewById(R.id.sda_btn_submit_order_detail);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Orders");

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(OrderedBillActivity.this);
        sda_rv_ordered_bill.setLayoutManager(layoutManager);


        mAdapter = new Card_User_Order_List_RecyclerView_Adapter(OrderedBillActivity.this, items_collection);
        sda_rv_ordered_bill.setAdapter(mAdapter);

        //Setting Ammount
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAmmount();
            }
        },time);

//        Animation animation = AnimationUtils.loadAnimation(this,R.anim.bounce);
//        imageView.setAnimation(animation);



        sda_btn_submit_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!foodNames.isEmpty())
                {
                    User_Ordered_Details user_ordered_details=new User_Ordered_Details(
                            foodNames,
                            sda_txt_total_amount.getText().toString().trim(),
                            sda_txt_order_address.getText().toString().trim(),
                            sda_txt_order_phone.getText().toString().trim()
                    );

                    if(!sda_txt_order_address.getText().toString().isEmpty() &&
                            !sda_txt_order_phone.getText().toString().isEmpty())
                    {
                        myRef.child(""+orderNumber).setValue(user_ordered_details).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AlertDialog.Builder s=new AlertDialog.Builder(OrderedBillActivity.this);
                                s.setTitle("The Order Has been Completed");
                                s.setMessage("Please Wait for 45 mnt");
                                s.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent(OrderedBillActivity.this, UserMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        items_collection.clear();
                                    }
                                });

                                s.create().show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OrderedBillActivity.this, "Our Service is Busy at this time!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(OrderedBillActivity.this, "Fill the above Fields", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(OrderedBillActivity.this, "Please Select item to Order", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Ordered_final_items_collection.totalAmount=0;
    }

    @SuppressLint("SetTextI18n")
    private void setAmmount()
    {
        sda_txt_total_amount.setText(""+Ordered_final_items_collection.totalAmount);
    }
}
