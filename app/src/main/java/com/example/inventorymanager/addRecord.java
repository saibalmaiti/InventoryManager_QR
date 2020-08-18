package com.example.inventorymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addRecord extends AppCompatActivity {
    private String scanned,itemName, itemDetails, surl;
    private TextView name,details,dateView,quantity;
    private Button add;
    private ImageView imageView2;
    private SimpleDateFormat dateFormat;
    private Double q = 1.0;
    private Date c;
    private DatabaseReference mDatabase, mDatabase2;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecord);
        progressDialog = new ProgressDialog(addRecord.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        Intent intent = getIntent();
        scanned = intent.getStringExtra("result");
        name = findViewById(R.id.productName);
        details = findViewById(R.id.productDetails);
        dateView = findViewById(R.id.date);
        quantity = findViewById(R.id.quantity);
        add = findViewById(R.id.addRecord);
        imageView2 = (ImageView) findViewById(R.id.productImageView);

        mDatabase = FirebaseDatabase.getInstance().getReference("Products").child("ProductsLog");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("Products").child("ProductDetails");

        c = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        final String currentDate = dateFormat.format(c);


        itemName = scanned.split(";")[0];
        surl = scanned.split(";")[1];
        //itemDetails =scanned.split(";")[2];

//Added to check if the product is actually exists
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if(!dataSnapshot.hasChild(itemName))
               {
                   name.setVisibility(View.INVISIBLE);
                   details.setVisibility(View.INVISIBLE);
                   dateView.setVisibility(View.INVISIBLE);
                   quantity.setVisibility(View.INVISIBLE);
                   add.setVisibility(View.INVISIBLE);
                   imageView2.setVisibility(View.INVISIBLE);
                   Toast.makeText(addRecord.this, "Item Not Exists",Toast.LENGTH_SHORT).show();
                   add.setClickable(false);
                   progressDialog.dismiss();

               }
               else {
                   String d = dataSnapshot.child(itemName).child("Details").getValue().toString();
                   details.setText(d);
                   progressDialog.dismiss();
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//checking upto this


        Glide.with(addRecord.this).load(surl).placeholder(R.drawable.ic_launcher_foreground).into(imageView2);
        Log.d("//<<",surl);
        name.setText(itemName);
//        if(itemDetails!=null){
//        details.setText(itemDetails);
//        }
        dateView.setText(currentDate);
        quantity.setText("Quantity: 1");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Adding");
                progressDialog.show();

                mDatabase.child(itemName).child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                        q = Double.valueOf(dataSnapshot.getValue().toString()) + 1.0;
                        mDatabase.child(itemName).child("Total").setValue(q);
                        }
                        else{
                            mDatabase.child(itemName).child("Total").setValue(q);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mDatabase.child(itemName).child("EntryDate").child(currentDate).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(addRecord.this, "Record Added",Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(addRecord.this,workerMenu.class));
//                        finish();
                    }
                });

            }
        });



    }

}
