package com.example.inventorymanager;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashScreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        LogoLauncher logoLauncher=new LogoLauncher();
        logoLauncher.start();
    }
    private class LogoLauncher extends Thread{

        public void run()
        {
            try{
                sleep(2000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            mAuth= FirebaseAuth.getInstance();
            FirebaseUser user=mAuth.getCurrentUser();
//            Log.d("User", user.toString());
            if(user!=null)
            {
                String userType = mAuth.getCurrentUser().getDisplayName().split(" ")[0];
                Log.d("UserType",userType);
                if(userType.equals("2")) {
                    Log.d("Current User", mAuth.getCurrentUser().getDisplayName().toString());
                    Intent intentResident = new Intent(splashScreen.this, managerMenu.class);
//                    intentResident.putExtra("UID", mAuth.getCurrentUser().getUid().toString());
                    startActivity(intentResident);
                    splashScreen.this.finish();
                }
                else
                {
                    Log.d("Current User", mAuth.getCurrentUser().getDisplayName().toString());
                    Intent intentResident = new Intent(splashScreen.this, workerMenu.class);
//                    intentResident.putExtra("UID", mAuth.getCurrentUser().getUid().toString());
                    startActivity(intentResident);
                    splashScreen.this.finish();
                }

            }
            else {
                Intent intent = new Intent(splashScreen.this, Login.class);
                startActivity(intent);
                splashScreen.this.finish();
            }

        }
    }
}
