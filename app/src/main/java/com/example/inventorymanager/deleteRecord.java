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
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class deleteRecord extends AppCompatActivity {
    private String scanned,itemName, itemDetails, surl, eAmount;
    private TextView name,details,dateView,quantity;
    private Button remove;
    private ImageView imageView;
    private SimpleDateFormat dateFormat;
    private Double q;
    private Date c;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_record);
        progressDialog = new ProgressDialog(deleteRecord.this);
        progressDialog.setMessage("Removing");
        Intent intent = getIntent();
        scanned = intent.getStringExtra("result");
        name = findViewById(R.id.dproductName);
        details = findViewById(R.id.dproductDetails);
        dateView = findViewById(R.id.delDate);
        quantity = findViewById(R.id.quantity);
        remove = findViewById(R.id.removeRecord);
        imageView = (ImageView) findViewById(R.id.dproductImageView);

        mDatabase = FirebaseDatabase.getInstance().getReference("Products").child("ProductsLog");

        c = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        final String currentDate = dateFormat.format(c);

        itemName = scanned.split(";")[0];
        surl = scanned.split(";")[1];
        itemDetails =scanned.split(";")[2];

        Glide.with(deleteRecord.this).load(surl).placeholder(R.drawable.ic_launcher_foreground).into(imageView);
        name.setText(itemName);
        if(itemDetails!=null){
            details.setText(itemDetails);
        }
        dateView.setText(currentDate);

        mDatabase.child(itemName).child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eAmount = dataSnapshot.getValue().toString();
                quantity.setText("Existing Quantity: "+eAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                mDatabase.child(itemName).child("Total").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        q = Double.valueOf(dataSnapshot.getValue().toString());
                        if(q != 0){
                            q = q - 1.0;
                            mDatabase.child(itemName).child("Total").setValue(q);
                            mDatabase.child(itemName).child("RemoveDate").child(currentDate).setValue("1").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    startActivity(new Intent(deleteRecord.this,workerMenu.class));
                                }
                            });
                        }
                        else{
                            Toast.makeText(deleteRecord.this,"Product Outof Stock",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(deleteRecord.this,workerMenu.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

    }
}
