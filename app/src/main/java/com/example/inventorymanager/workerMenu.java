package com.example.inventorymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class workerMenu extends AppCompatActivity {
    private ImageView add,remove,help,info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_menu);
        add = findViewById(R.id.addBtn);
        info = findViewById(R.id.detailsBtn);
        remove = findViewById(R.id.removeBtn);
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
    }
}
