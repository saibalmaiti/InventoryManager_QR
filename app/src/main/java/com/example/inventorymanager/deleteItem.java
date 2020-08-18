package com.example.inventorymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class deleteItem extends AppCompatActivity {
    private String scanned,itemName, itemDetails, surl;
    private TextView name,details;
    private Button delete;
    private ImageView delImage;
    private DatabaseReference mDatabase, mDatabase2;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);
        progressDialog = new ProgressDialog(deleteItem.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        Intent intent = getIntent();
        scanned = intent.getStringExtra("result");
        name = findViewById(R.id.delproductName);
        details = findViewById(R.id.delproductDetails);
        delImage = findViewById(R.id.delItemImageView);
        delete = findViewById(R.id.delProduct);

        itemName = scanned.split(";")[0];
        surl = scanned.split(";")[1];
        //itemDetails =scanned.split(";")[2];

        mDatabase = FirebaseDatabase.getInstance().getReference("Products").child("ProductDetails").child(itemName);
        mDatabase2 = FirebaseDatabase.getInstance().getReference("Products").child("ProductsLog").child(itemName);
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(surl);
        Glide.with(deleteItem.this).load(surl).placeholder(R.drawable.ic_launcher_foreground).into(delImage);
        name.setText(itemName);
        FirebaseDatabase.getInstance().getReference("Products").child("ProductDetails").child(itemName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String d = dataSnapshot.child("Details").getValue().toString();
                    if (!d.equals(""))
                        details.setText(d);
                    progressDialog.dismiss();
                }
                else{
                    name.setVisibility(View.INVISIBLE);
                    details.setVisibility(View.INVISIBLE);
                    delImage.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);
                    delete.setClickable(false);
                    progressDialog.dismiss();
                    Toast.makeText(deleteItem.this,"Product doesn't Exists",Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(deleteItem.this,managerMenu.class));
//                    finish();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.removeValue();
                mDatabase2.removeValue();
                mStorage.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(deleteItem.this,"Item Deleted",Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(deleteItem.this,managerMenu.class));
//                        finish();
                    }
                });
            }
        });

    }
}
