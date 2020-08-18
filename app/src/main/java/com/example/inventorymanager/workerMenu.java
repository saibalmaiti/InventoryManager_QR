package com.example.inventorymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class workerMenu extends AppCompatActivity {
    private ImageView add,remove,help,info,logout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_menu);
        mAuth =FirebaseAuth.getInstance();
        logout =findViewById(R.id.workerLogout);
        add = findViewById(R.id.addBtn);
        info = findViewById(R.id.detailsBtn);
        remove = findViewById(R.id.removeBtn);
        help = findViewById(R.id.helpBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(workerMenu.this, MainActivity.class);//change
                intent.putExtra("class","add");
                startActivity(intent);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(workerMenu.this,MainActivity.class);
                intent.putExtra("class","delete");
                startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(workerMenu.this,productDetails.class);
                startActivity(intent);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(workerMenu.this, com.example.inventorymanager.help.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(workerMenu.this,Login.class));
            }
        });
    }
}
