package com.example.musa.imijifrb;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class Dashboard extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private FirebaseUser currentFirebaseUser;

    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        mStorageRef = FirebaseStorage.getInstance().getReference().child("userPhotos");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentFirebaseUser.getUid());

        Intent i = getIntent();
        String email = i.getStringExtra("email");

        ((TextView) findViewById(R.id.userEmail)).setText(email);
        profilePhoto = (ImageView) findViewById(R.id.userPhoto);

        updateProfilePhoto();


    }

    private void updateProfilePhoto() {

        DatabaseReference photoRef = mDatabaseRef.child("photoUrl");

        photoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() != null){

                    String photoUrl = dataSnapshot.getValue().toString();

                    Picasso.with(getApplicationContext())
                            .load(photoUrl)
                            .fit()
                            .centerCrop()
                            .into(profilePhoto);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void onUploadProfilePhoto(View view){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);

    }

    public void onClickAlbum(View view){
        Intent i = new Intent(this, Albums.class);
        startActivity(i);
    }

    public void onClickCamera(View view){


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);



    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 2 && resultCode == RESULT_OK){

            final Uri uri = data.getData();

            uri.getPath();



            StorageReference filePath = mStorageRef.child(currentFirebaseUser.getUid());


            //Commons.uploadFileToStorage(data);

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    mDatabaseRef.child("photoUrl").setValue(taskSnapshot.getDownloadUrl().toString());


                    Picasso.with(getApplicationContext())
                            .load(taskSnapshot.getDownloadUrl())
                            .fit()
                            .centerCrop()
                            .into(profilePhoto);


                }
            });




        }
    }
}
