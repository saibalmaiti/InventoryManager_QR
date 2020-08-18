package com.example.inventorymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class managerMenu extends AppCompatActivity {
    private ImageView addproduct, logbtn, updateBtn,delBtn,logout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);
        mAuth = FirebaseAuth.getInstance();
        Log.d("Manager", mAuth.getCurrentUser().getDisplayName());
        addproduct = findViewById(R.id.newProduct);
        logbtn = findViewById(R.id.logs);
        updateBtn = findViewById(R.id.updateBtn);
        delBtn = findViewById(R.id.delBtn);
        logout = findViewById(R.id.managerLogout);
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(managerMenu.this,newItem.class);
                startActivity(intent);
                finish();
            }
        });
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(managerMenu.this,table.class);
                startActivity(intent);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(managerMenu.this,MainActivity.class);
                intent.putExtra("class","updateItem");
                startActivity(intent);
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(managerMenu.this,MainActivity.class);
                intent.putExtra("class","deleteItem");
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(managerMenu.this,Login.class));
            }
        });

    }
}
