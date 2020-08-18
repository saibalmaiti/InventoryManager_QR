package com.example.inventorymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class table extends AppCompatActivity {
    private ScrollView scrollView;
    private TableLayout mTableLayout;
    private DatabaseReference mFirebase;
    private ArrayList<String> total = new ArrayList<>();
    private ArrayList<String> key = new ArrayList<>();
    private ArrayList<String> entry = new ArrayList<>();
    private ArrayList<String> out = new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        progressDialog = new ProgressDialog(table.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        scrollView = findViewById(R.id.scroll);
        mTableLayout = (TableLayout)findViewById(R.id.RHE);
        mFirebase = FirebaseDatabase.getInstance().getReference("Products").child("ProductsLog");

        displayTable();
    }
    private void displayTable(){
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot k: dataSnapshot.getChildren())
                {
                    key.add(k.getKey().toString());
                    total.add(k.child("Total").getValue().toString());
                    entry.add(Long.toString(k.child("EntryDate").getChildrenCount()));
                    out.add(Long.toString(k.child("RemoveDate").getChildrenCount()));
                    Log.d("Total",total.toString());  //Debug Statement
                    Log.d("Key",key.toString()); //Debug Statement
                    Log.d("Count",entry.toString());

                }
                        int j = 1;
                        for(String k:key)
                        {
                            TableRow row = new TableRow(table.this);


                            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            row.setPadding(5,5,5,5);
                            row.setBackgroundColor(Color.parseColor("#DAE8FC"));

                            TextView index = new TextView(table.this);
                            index.setText(Integer.toString(j));
                            index.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 1f));

                            TextView item = new TextView(table.this);
                            item.setText(k);
                            item.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            TextView quantity = new TextView(table.this);
                            quantity.setText(total.get(key.indexOf(k)));
                            quantity.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            TextView Entryquantity = new TextView(table.this);
                            Entryquantity.setText(entry.get(key.indexOf(k)));
                            Entryquantity.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            TextView Outquantity = new TextView(table.this);
                            Outquantity.setText(out.get(key.indexOf(k)));
                            Outquantity.setLayoutParams(new TableRow.LayoutParams(
                                    TableRow.LayoutParams.WRAP_CONTENT,
                                    TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            row.addView(index);
                            row.addView(item);
                            row.addView(Entryquantity);
                            row.addView(Outquantity);
                            row.addView(quantity);
                            mTableLayout.addView(row);
                            j++;
                        } // adding to table
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("DatabaseError", databaseError.getDetails());
            }
        });
    }
}
