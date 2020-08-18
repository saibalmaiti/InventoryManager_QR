package com.example.inventorymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class newItem extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button choose, add;
    private ImageView imageView;
    private ProgressBar mprogressBar;
    private Uri mImageUri;
    private EditText eItem, eDetails;
    private String itemName, details;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        choose = findViewById(R.id.selectBtn);
        imageView = findViewById(R.id.uploadedImage);
        mprogressBar = findViewById(R.id.progressBar);
        eItem = findViewById(R.id.productName);
        eDetails = findViewById(R.id.productdetails);
        add = findViewById(R.id.addItem);

        mStorageRef = FirebaseStorage.getInstance().getReference("ProductImage");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Products").child("ProductDetails");
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfilechooser();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask!=null && mUploadTask.isInProgress()){
                    Toast.makeText(newItem.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadfile();
                }
            }
        });
    }
    private void openfilechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!= null){
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadfile(){
        if(mImageUri!=null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mprogressBar.setProgress(0);
                        }
                    }, 500); // adding delay

            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Upload upload = new Upload(eItem.getText().toString().trim(),
                            task.getResult().toString());
                    mDatabaseRef.child(eItem.getText().toString().trim()).setValue(upload);
                    mDatabaseRef.child(eItem.getText().toString().trim()).child("Details").setValue(eDetails.getText().toString());

                    String url = upload.getImageUrl();
                    Log.d("ImageUrl2", url);//debug Statement

                    String name = eItem.getText().toString().trim();
                    String details = eDetails.getText().toString().trim();
                    Intent intent = new Intent(newItem.this,QRgenerator.class);
                    intent.putExtra("Url",url);
                    intent.putExtra("name",name);
                    //intent.putExtra("details",details); //delete details
                    startActivity(intent);
                    finish();

                }
            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(newItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mprogressBar.setProgress((int) progress);
                }
            });
        }
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(newItem.this,managerMenu.class));
        super.onBackPressed();
    }
}
