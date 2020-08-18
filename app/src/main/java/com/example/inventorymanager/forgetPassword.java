package com.example.inventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPassword extends AppCompatActivity {
    private EditText rstMail;
    private Button rstBtn;
    private FirebaseAuth mauth;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        rstMail =findViewById(R.id.rstEmail);
        rstBtn =findViewById(R.id.rstBtn);
        rstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth=FirebaseAuth.getInstance();
                email = rstMail.getText().toString();
                if(email.equals(""))
                {
                    Toast.makeText(forgetPassword.this,"Enter Registered Email ID",Toast.LENGTH_LONG).show();
                }
                else
                {
                    mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful())
                           {
                               Toast.makeText(forgetPassword.this, "Reset Mail Sent Successfully", Toast.LENGTH_LONG).show();
                               finish();
                               startActivity(new Intent(forgetPassword.this, Login.class));
                           }
                           else
                           {
                               Toast.makeText(forgetPassword.this,"Request Failed",Toast.LENGTH_LONG).show();
                           }
                        }
                    });
                }

            }
        });

    }
    //Added this Override Method to back to Login activity from this with out changing password
    @Override
    public void onBackPressed(){
        startActivity(new Intent(new Intent(forgetPassword.this, Login.class)));
        super.onBackPressed();
    }
}

