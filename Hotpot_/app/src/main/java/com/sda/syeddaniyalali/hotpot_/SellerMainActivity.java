package com.sda.syeddaniyalali.hotpot_;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class SellerMainActivity extends AppCompatActivity {

    private RecyclerView sda_seller_orders_rv;
    private EditText sda_seller_orders_food_name, sda_seller_orders_chief_name,
            sda_seller_orders_food_price, sda_seller_orders_delivery_price;
    private RatingBar sda_seller_orders_food_rating;
    private ImageView sda_imgView_seller_orders_image_view;
    private Button sda_btn_seller_orders_image_select, sda_btn_seller_orders_submit;

    private String Base64txt;
    private ProgressDialog mProgressDialog;
    private int REQUEST_CODE=10;
    private static String myPath;
    private static Uri sda_imageUri;
    private static StorageTask uploadTask;
    private Card_Seller_Ordered_List_RecyclerView_Adapter mAdapter;
    private ArrayList<User_Ordered_Details> user_ordered_details=new ArrayList<>();

    // Write a message to the database
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Images");
    DatabaseReference myRef2 = database.getReference("Orders");
    ItemsDetail itemsDetail = new ItemsDetail();


    //Session Handling
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        //Finding Ids
        sda_seller_orders_rv = findViewById(R.id.sda_seller_orders_rv);
        sda_seller_orders_food_name = findViewById(R.id.sda_seller_orders_food_name);
        sda_seller_orders_chief_name = findViewById(R.id.sda_seller_orders_chief_name);
        sda_seller_orders_food_price = findViewById(R.id.sda_seller_orders_food_price);
        sda_seller_orders_delivery_price = findViewById(R.id.sda_seller_orders_delivery_price);
        sda_seller_orders_food_rating = findViewById(R.id.sda_seller_orders_food_rating);
        sda_imgView_seller_orders_image_view = findViewById(R.id.sda_imgView_seller_orders_image_view);
        sda_btn_seller_orders_image_select = findViewById(R.id.sda_btn_seller_orders_image_select);
        sda_btn_seller_orders_submit = findViewById(R.id.sda_btn_seller_orders_submit);
        mProgressDialog=new ProgressDialog(this);

        sda_seller_orders_rv.setLayoutManager(new LinearLayoutManager(this));

        //Firebase Storage
        mStorageRef = FirebaseStorage.getInstance().getReference("images/sellers");
        checkFilePermissions();


        Intent serviceIntent = new Intent(this, FirebaseServices.class);
        startService(serviceIntent);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Orders");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Add Food");
        host.addTab(spec);

        // Read from the database
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {   // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User_Ordered_Details value = ds.getValue(User_Ordered_Details.class);
                    user_ordered_details.add(new User_Ordered_Details(value.sda_foodName,value.sda_amount,value.sda_address, value.sda_Phone));

                }
                mAdapter = new Card_Seller_Ordered_List_RecyclerView_Adapter(SellerMainActivity.this,user_ordered_details);
                sda_seller_orders_rv.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(SellerMainActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        sda_btn_seller_orders_image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Images form intent
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });


        sda_btn_seller_orders_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setMessage("Uploading Data");

                if(!sda_seller_orders_food_name.getText().toString().trim().isEmpty() ||
                        !sda_seller_orders_chief_name.getText().toString().trim().isEmpty() ||
                        !sda_seller_orders_delivery_price.getText().toString().trim().isEmpty() ||
                        !sda_seller_orders_food_price.getText().toString().trim().isEmpty())
                {
                    if(uploadTask!=null && uploadTask.isInProgress())
                    {
                        Toast.makeText(SellerMainActivity.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        long imageName=System.currentTimeMillis();
                        UploadData(imageName);
                        mProgressDialog.show();
                    }
                }
                else
                {
                    Toast.makeText(SellerMainActivity.this, "Please Fill the Fileds", Toast.LENGTH_SHORT).show();
                }





            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQUEST_CODE && data!=null && data.getData()!=null) {

            try{
                sda_imageUri=data.getData();
                sda_imgView_seller_orders_image_view.setImageURI(sda_imageUri);
    //            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
    //            sda_imgView_seller_orders_image_view.setImageBitmap(bitmap);
    //            Base64txt=convertToBase64(bitmap);
    //            myPath = data.getData().getPath();
                //Toast.makeText(this, ""+sda_imageUri, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, "We did't get image", Toast.LENGTH_SHORT).show();
            }
        }

    }


    String convertToBase64(Bitmap bitmap)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,stream);
        byte arr[]=stream.toByteArray();
        return Base64.encodeToString(arr,Base64.DEFAULT);
    }

    private String getExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    private void UploadData(final long imageName)
    {

        final StorageReference storageReference=mStorageRef.child(imageName+"."+getExtension(sda_imageUri));

        uploadTask=storageReference.putFile(sda_imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    itemsDetail.sda_FoodName = sda_seller_orders_food_name.getText().toString().trim();
                                    itemsDetail.sda_ChiefName = sda_seller_orders_chief_name.getText().toString().trim();
                                    itemsDetail.sda_FoodPrice = sda_seller_orders_food_price.getText().toString().trim();
                                    itemsDetail.sda_DeliveryPrice = sda_seller_orders_delivery_price.getText().toString().trim();
                                    itemsDetail.sda_Rating = String.valueOf(sda_seller_orders_food_rating.getRating());

                                    //String downloadUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                                    itemsDetail.sda_ImageUri = uri.toString();

                                    //Toast.makeText(SellerMainActivity.this, ""+uri.toString(), Toast.LENGTH_LONG).show();
                                    if(!uri.toString().equals(""))
                                    {
                                        uploadRecord(imageName);
                                    }
                                    else
                                    {
                                        Toast.makeText(SellerMainActivity.this, "Not Ready", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SellerMainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        Toast.makeText(SellerMainActivity.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                });
    }

    private void uploadRecord(long imageName)
    {
        myRef.child(""+imageName)
                .setValue(itemsDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SellerMainActivity.this, "Dish has Been Added", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SellerMainActivity.this, "Failed to add Dish", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });

    }

    private void checkFilePermissions()
    {
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck = SellerMainActivity.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
                permissionCheck += SellerMainActivity.this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");

                if(permissionCheck !=0)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},5);
                    }
                }
            }
            else
            {
                //Toast.makeText(this, "Grand the Permissions from Settings", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        user_ordered_details.clear();
    }
}
