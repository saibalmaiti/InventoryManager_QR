package com.example.inventorymanager;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updateDetails extends AppCompatActivity {
    private String scanned,itemName,surl,uDetails;
    private TextView name;
    private EditText details;
    private Button update;
    private ImageView imageView;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);
        progressDialog = new ProgressDialog(updateDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        Intent intent = getIntent();
        scanned = intent.getStringExtra("result");
        name = findViewById(R.id.updateproductName);
        details = findViewById(R.id.updateproductDetails);
        imageView = findViewById(R.id.updateItemImageView);
        update = findViewById(R.id.updateProduct);

        itemName = scanned.split(";")[0];
        surl = scanned.split(";")[1];

        mDatabase = FirebaseDatabase.getInstance().getReference("Products").child("ProductDetails");
        Glide.with(updateDetails.this).load(surl).placeholder(R.drawable.ic_launcher_foreground).into(imageView);
        name.setText(itemName);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(itemName))
                {
                    progressDialog.dismiss();
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uDetails = details.getText().toString();
                            Log.d("NewDetails",uDetails);
                            if(uDetails.equals(""))
                            {
                                Toast.makeText(updateDetails.this,"Blank Details",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mDatabase.child(itemName).child("Details").setValue(uDetails);
                                Toast.makeText(updateDetails.this, "Details Updated", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(updateDetails.this, managerMenu.class));
//                                finish();
                            }
                        }
                    });
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(updateDetails.this,"Product does not exists",Toast.LENGTH_SHORT).show();
                    imageView.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    details.setVisibility(View.INVISIBLE);
                    update.setVisibility(View.INVISIBLE);
                    update.setClickable(false);
//                    startActivity(new Intent(updateDetails.this,managerMenu.class));
//                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
