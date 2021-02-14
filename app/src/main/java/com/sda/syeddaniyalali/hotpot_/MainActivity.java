package com.sda.syeddaniyalali.hotpot_;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    Spinner sda_login_select_branch;
    EditText sda_login_email, sda_login_pass;
    Button sda_login_btn_sumbit;
    TextView sda_login_txt_acc;
    ProgressDialog progress;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private static String userId="NO_ID";
    //private static boolean value=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        sda_login_select_branch=findViewById(R.id.sda_login_select_branch);
        sda_login_email=findViewById(R.id.sda_login_email);
        sda_login_pass=findViewById(R.id.sda_login_pass);
        sda_login_btn_sumbit=findViewById(R.id.sda_login_btn_sumbit);
        sda_login_txt_acc=findViewById(R.id.sda_login_txt_acc);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        sda_login_btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = sda_login_email.getText().toString().trim();
                String password = sda_login_pass.getText().toString().trim();

                if(!email.equals("") && !password.equals(""))
                {
                    try
                    {
                        myRef = database.getReference(sda_login_select_branch.getSelectedItem().toString());
                        addUser(email,password);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please Enter Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sda_login_txt_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void addUser(final String email, String password)
    {
        try {


        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            try
                            {
                                //getData(user.getEmail());
                                getData(email);
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }




                        } else {
                            progress.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    //Getting Data==================================================================================
    void getData(final String userEmail)
    {
        try
        {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progress.dismiss();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User u=ds.getValue(User.class);
                    if(u.Email.equalsIgnoreCase(userEmail))
                    {
                        Toast.makeText(MainActivity.this, "Welcome "+u.Role, Toast.LENGTH_SHORT).show();
                        if(u.Role.equalsIgnoreCase("Admin"))
                        {
                            startActivity(new Intent(MainActivity.this, AdminMainActivity.class));
                            finish();
                        }
                        else if(u.Role.equalsIgnoreCase("Seller"))
                        {
                            Intent intent=new Intent(MainActivity.this,SellerMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(u.Role.equalsIgnoreCase("User"))
                        {
                            Intent intent=new Intent(MainActivity.this,UserMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Your can't access another's Account", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                progress.dismiss();
                // Failed to read value
                Log.w(TAG, "Please Check your Internet Connection", error.toException());
            }
        });
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    //Getting Data==================================================================================

}
