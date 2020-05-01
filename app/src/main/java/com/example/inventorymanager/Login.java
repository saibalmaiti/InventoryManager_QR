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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText email,password;
    private String semail, spassword;
    private Button login;
    private TextView register,forgetpsw;
    private FirebaseAuth mauth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Logging in");
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.register);
        forgetpsw = findViewById(R.id.fpsw);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                semail = email.getText().toString();
                spassword = password.getText().toString();
                Log.d("!!!",semail+" "+spassword);
                mauth.signInWithEmailAndPassword(semail,spassword).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Boolean key = checkEmailVerification();
                            if(key)
                            {
                                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                String userType = currentuser.split(" ")[0];
                                Log.d("Type",userType + currentuser);
                                if(userType.equals("1"))
                                {
                                    Log.d("Tag2", currentuser);
                                    Intent intent = new Intent(Login.this,workerMenu.class);
                                    startActivity(intent);
                                }
                                else if(userType.equals("2"))
                                {
                                    Log.d("Tag",currentuser);
                                    Intent intent = new Intent(Login.this,managerMenu.class);
                                    startActivity(intent);
                                }
                            }

                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, register.class);
                startActivity(intent);
                Login.this.finish();
            }
        });


    }
    private Boolean checkEmailVerification(){
        Boolean check = false;
        FirebaseUser firebaseUser=mauth.getInstance().getCurrentUser();
        Boolean flag=firebaseUser.isEmailVerified();
        if(flag)
        {
            check = true;


        }
        else
        {
            Toast.makeText(this,"Verify Email",Toast.LENGTH_LONG).show();
            mauth.signOut();
        }
        return check;
    }
}
