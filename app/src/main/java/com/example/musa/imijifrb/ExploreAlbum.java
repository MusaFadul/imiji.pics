package com.example.musa.imijifrb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ExploreAlbum extends AppCompatActivity {

    private StorageReference mStorage, storageAlbumReference;
    private DatabaseReference mDatabase, databasePhotoDirReference;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;


    private ImageView uploadBtn;
    private List<Picture> pictureList = new ArrayList<Picture>();

    private String album;
    private  Bitmap bitmap;

    private String downloadUrl;
    private TextView albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_album);

        Intent i = getIntent();
        album = i.getStringExtra("album");

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);

        recyclerView = (RecyclerView) findViewById(R.id.photoGrid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("albums");;
        mStorage = FirebaseStorage.getInstance().getReference();

        storageAlbumReference = mStorage.child(album);
        databasePhotoDirReference = mDatabase.child(album).child("photos");


        albumName = (TextView) findViewById(R.id.albumNames);
        albumName.setText(album);



        updateView();
    }

    private void updateView() {

        pictureList.clear();

        databasePhotoDirReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                retrieveAllPhotosFromAlbum(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     *
     * @param dataSnapshot
     */
    private void retrieveAllPhotosFromAlbum(DataSnapshot dataSnapshot) {

        long totalNumberOfPhotos = dataSnapshot.getChildrenCount();

        if(totalNumberOfPhotos > 0) {

            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                String title = childDataSnapshot.getKey().toString();
                String photoUrl = childDataSnapshot.getValue().toString();

                Picture picture = new Picture();
                picture.setUrl(photoUrl);
                picture.setTitle(title);

                pictureList.add(picture);
            }

            recyclerViewAdapter = new PhotoAdapter(pictureList, this);
            recyclerView.setAdapter(recyclerViewAdapter);

        } else{

            Toast.makeText(getApplicationContext(), "Album is empty, add more photos", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadPhoto(View view){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

        Toast.makeText(getApplicationContext(), "Upload photo now", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == RESULT_OK){

            final Uri uri = data.getData();

            StorageReference filePath = storageAlbumReference.child(uri.getLastPathSegment());

            downloadUrl = null;

            //Commons.uploadFileToStorage(data);

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    databasePhotoDirReference.child(uri.getLastPathSegment()).setValue(taskSnapshot.getDownloadUrl().toString());

                    updateView();
                }
            });


        }
    }


    public void backToAlbums(View view){
        Intent i = new Intent(ExploreAlbum.this, Albums.class);
        startActivity(i);
    }

}
