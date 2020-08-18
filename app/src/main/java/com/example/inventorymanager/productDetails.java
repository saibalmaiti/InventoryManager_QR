package com.example.inventorymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class productDetails extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference mFirebase;
    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> details = new ArrayList<>();
    private Object[] keyArray;
    private Object[] urlArray;
    private Object[] detailsArray;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(productDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        setContentView(R.layout.activity_product_details);
        mFirebase = FirebaseDatabase.getInstance().getReference("Products").child("ProductDetails");
        listView = findViewById(R.id.listView);
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren())
                {
                    key.add(d.getKey().toString());
                    urls.add(d.child("imageUrl").getValue().toString());
                    details.add(d.child("Details").getValue().toString());
                }
                keyArray = key.toArray();
                urlArray = urls.toArray();
                detailsArray = details.toArray();
                CustomAdaptor customAdaptor = new CustomAdaptor();
                listView.setAdapter(customAdaptor);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    class CustomAdaptor extends BaseAdapter{
        @Override
        public int getCount() {
            return keyArray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.customlist, null);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView textView1 =view.findViewById(R.id.textView);
            TextView textView2 =view.findViewById(R.id.textView2);

            Glide.with(productDetails.this).load(urlArray[position].toString()).into(imageView);
            textView1.setText(keyArray[position].toString());
            textView2.setText(detailsArray[position].toString());
            return view;
        }
    }

}
