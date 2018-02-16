package com.example.musa.imijifrb;

import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Albums extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager =  new LinearLayoutManager(this);

    private List<AlbumDataSource> dataSourceList;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        recyclerView = (RecyclerView) findViewById(R.id.album_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new
                DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);
        dataSourceList = new ArrayList<AlbumDataSource>();


        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                updateAlbumList(dataSnapshot);






            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recyclerViewAdapter = new AlbumAdapter(dataSourceList, this);
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    private void updateAlbumList(DataSnapshot dataSnapshot) {

        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {



            AlbumDataSource album = new AlbumDataSource();

            album.setAlbumTitle(childDataSnapshot.getKey().toString());
            album.setAlbumDescription(childDataSnapshot.child("mode").getValue().toString());
            album.setAlbumOwner(childDataSnapshot.child("owner").getValue().toString());

            //Get phone field and append to list
            //dataSourceList.add((Long) singleUser.get("phone"));

            dataSourceList.add(album);


        }

        recyclerViewAdapter = new AlbumAdapter(dataSourceList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void updateAlbumList(Map<String, Object> values) {

        for (Map.Entry<String, Object> entry : values.entrySet()){

            //Get user map
            Map singleAlbum = (Map) entry.getValue();

            AlbumDataSource album = new AlbumDataSource();

            album.setAlbumTitle(singleAlbum.get("owner").toString());
            album.setAlbumDescription(singleAlbum.get("mode").toString());
            album.setAlbumOwner(singleAlbum.get("owner").toString());

            //Get phone field and append to list
            //dataSourceList.add((Long) singleUser.get("phone"));

            dataSourceList.add(album);

        }

        recyclerViewAdapter = new AlbumAdapter(dataSourceList, this);
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    public void onAlbumAdd(View view){


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Albums.this);

        final View mView = getLayoutInflater().inflate(R.layout.add_newalbum, null);

        Button add = (Button) mView.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText albumTitleET = (EditText) mView.findViewById(R.id.newAlbumTitle);
                final String newAlbumTitle = albumTitleET.getText().toString();

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child(newAlbumTitle).setValue("0").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            mDatabase = FirebaseDatabase.getInstance().getReference().child(newAlbumTitle);
                            mDatabase.child("owner").setValue("musa");
                            mDatabase.child("mode").setValue("private");
                            mDatabase.push().setValue("users");
                            mDatabase.push().setValue("photos");


                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                        } else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();


    }

}
