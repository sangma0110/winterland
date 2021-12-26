package com.jaess.winterland;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;

import com.google.firebase.storage.StorageReference;

public class Uploadimage extends AppCompatActivity {

    Button btnbrowse, btnupload;
    EditText txtdata,txtemail,txtdescription ;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    private String num;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadimage);

        storageReference = FirebaseStorage.getInstance().getReference("Gifts");
        databaseReference = FirebaseDatabase.getInstance().getReference("Gifts");
        database = FirebaseDatabase.getInstance();
        btnbrowse = (Button)findViewById(R.id.btnbrowse);
        btnupload= (Button)findViewById(R.id.btnupload);
        txtdata = (EditText)findViewById(R.id.txtdata);
        txtemail = (EditText)findViewById(R.id.txtemail);
        txtdescription = (EditText)findViewById(R.id.txtdescription);

        imgview = (ImageView)findViewById(R.id.image_view);
        progressDialog = new ProgressDialog(Uploadimage.this);// context name as per your project name

        //유저 토큰 세는거
        ref = database.getReference().child("UserAccount").child("User1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getKey().equals("tokens")) n = ds.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Uploadimage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }

        });

        //아이템 몇개인지 세는거
        ref = database.getReference().child("Gifts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                num = Long.toString(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Uploadimage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);

            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UploadImage() == true)
                {
                    int save1 = Integer.parseInt(n);
                    save1++;
                    n = Integer.toString(save1);

                    ref = database.getReference().child("UserAccount").child("User1");
                    ref.child("tokens").setValue(n);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }

    public boolean UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Image is Uploading…");
            progressDialog.show();

            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String TempImageName = txtdata.getText().toString().trim();
                            String TempImageName2 = txtemail.getText().toString().trim();
                            String TempImageName3 = txtdescription.getText().toString().trim();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

                            @SuppressWarnings("VisibleForTests")
                            uploadinfo imageUploadInfo = new uploadinfo(TempImageName, taskSnapshot.getUploadSessionUri().toString());

                            int save = Integer.parseInt(num);
                            save++;
                            num = Integer.toString(save);

                            String ImageUploadId =  "item"+ num ; //databaseReference.push().getKey();

                            databaseReference.child(ImageUploadId).child("Name").setValue(txtdata.getText().toString());
                            databaseReference.child(ImageUploadId).child("Email").setValue(txtemail.getText().toString());
                            databaseReference.child(ImageUploadId).child("Description").setValue(txtdescription.getText().toString());
                            databaseReference.child(ImageUploadId).child("ImageURL").setValue(imageUploadInfo.getImageURL());

                            storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Log.d(url,"url");
                                    databaseReference.child(ImageUploadId).child("ImageURL").setValue(url);
                                }
                            });

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            return true;
        }
        else {
            Toast.makeText(Uploadimage.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}