package com.example.inventorymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button signUp;
    private EditText email, password, name, mobile;
    private String rmail, rpsw,rname,rmobile,nameid;
    private ProgressDialog progressDialog;
    private DatabaseReference mRealtime;
    private RadioButton radioManager, radioWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing Up");
        email = findViewById(R.id.regMail);
        password = findViewById(R.id.regPassword);
        name = findViewById(R.id.regName);
        mobile = findViewById(R.id.regMobile);
        signUp = (Button)findViewById(R.id.regButton);
        radioManager = findViewById(R.id.radioManager);
        radioWorker= findViewById(R.id.radioWorker);
        mRealtime = FirebaseDatabase.getInstance().getReference("User");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    progressDialog.show();
                rmail = email.getText().toString().trim();
                rpsw = password.getText().toString().trim();
                rname = name.getText().toString();
                rmobile = mobile.getText().toString();

                int promt = 0;
                if (radioWorker.isChecked()) {
                    promt = 1;
                    nameid = "1 " + rname;
                }
                if (radioManager.isChecked()) {
                    promt = 2;
                    nameid = "2 " + rname;
                }
                if (promt == 2) {
                    mAuth.createUserWithEmailAndPassword(rmail, rpsw)
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nameid)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("log", "User profile updated.");
                                                        }
                                                    }
                                                });
                                        emailVerification();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("Name", rname);
                                        data.put("Email", rmail);
                                        data.put("Mobile", rmobile);
                                        mRealtime.child("Manager").child(mAuth.getCurrentUser().getUid()).updateChildren(data);
                                        progressDialog.dismiss();
                                        startActivity(new Intent(register.this, Login.class));
                                        register.this.finish();
                                    } else{
                                        Toast.makeText(register.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                } else if (promt == 1) {
                    mAuth.createUserWithEmailAndPassword(rmail, rpsw)
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nameid)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("log", "User profile updated.");
                                                        }
                                                    }
                                                });
                                        emailVerification();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("Name", rname);
                                        data.put("Email", rmail);
                                        data.put("Mobile", rmobile);
                                        mRealtime.child("Worker").child(mAuth.getCurrentUser().getUid()).updateChildren(data);
                                        progressDialog.dismiss();
                                        startActivity(new Intent(register.this, Login.class));
                                        register.this.finish();
                                    } else
                                        Toast.makeText(register.this, "Sign Up Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            }
        });


    }

    private void emailVerification() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(register.this, "Registration Successful,Verify Email", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    } else {
                        Toast.makeText(register.this, "Verification Email Cannot Be Sent", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
    private boolean validate()
    {
        Boolean res = false;
        int radioCheck;
        rmail = email.getText().toString().trim();
        rpsw = password.getText().toString().trim();
        rname = name.getText().toString();
        rmobile = mobile.getText().toString();
        RadioGroup field;
        field = findViewById(R.id.field);
        if(field.getCheckedRadioButtonId()==-1)
        {
            radioCheck=0;
        }
        else
        {
            radioCheck=1;
        }
        if(rname.isEmpty()||rpsw.isEmpty()||rmail.isEmpty()||radioCheck==0)
        {
            Toast.makeText(this, "Empty field",Toast.LENGTH_LONG).show();
        }
        else
        {
            res=true;
        }
        return res;
    }
}
