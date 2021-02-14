package com.sda.syeddaniyalali.hotpot_;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText sda_reg_email, sda_reg_pass;
    Button sda_reg_btn_sumbit;
    TextView sda_reg_txt_acc;
    Spinner sda_reg_select_branch;
    private FirebaseAuth mAuth;
    // Write a message to the database
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    private static final String TAG = "RegisterActivity";
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progress = new ProgressDialog(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        sda_reg_email=findViewById(R.id.sda_reg_email);
        sda_reg_pass=findViewById(R.id.sda_reg_pass);
        sda_reg_btn_sumbit=findViewById(R.id.sda_reg_btn_sumbit);
        sda_reg_txt_acc=findViewById(R.id.sda_reg_txt_acc);
        sda_reg_select_branch=findViewById(R.id.sda_reg_select_branch);

        sda_reg_btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        sda_reg_txt_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void addUser()
    {
        final String email=sda_reg_email.getText().toString().trim();
        final String password=sda_reg_pass.getText().toString().trim();


        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password))
        {
            progress.setMessage("Please Wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                User users = new User(sda_reg_select_branch.getSelectedItem().toString(),email,password);
                                myRef = database.getReference(sda_reg_select_branch.getSelectedItem().toString());
                                        myRef.child(user.getUid())
                                        .setValue(users)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               progress.dismiss();
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    Toast.makeText(RegisterActivity.this, "Now u can Login with Your Credentials", Toast.LENGTH_SHORT).show();

                                                }
                                                else
                                                {
                                                    Toast.makeText(RegisterActivity.this, "Failed to Create Account", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });



                            } else {
                                progress.dismiss();
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Email Already Exist",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });

        }
        else
        {
            Toast.makeText(this, "Enter Your Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
