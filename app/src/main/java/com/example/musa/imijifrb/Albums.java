package com.example.musa.imijifrb;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Albums extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager =  new LinearLayoutManager(this);

    private List<AlbumDataSource> dataSourceList;
    private DatabaseReference mDatabase;

    private ImageView albumnAdd;
    private AlertDialog dialog;

    private SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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

        albumnAdd = (ImageView) findViewById(R.id.addNewAlbum);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("albums");

        updateView();


        recyclerViewAdapter = new AlbumAdapter(dataSourceList, this);
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    private void updateView(){

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                updateAlbumList(dataSnapshot);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void refreshView(View view){
        updateView();
    }

    private void updateAlbumList(DataSnapshot dataSnapshot) {


        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

            if(!childDataSnapshot.getKey().toString().equals("users")){

                AlbumDataSource album = new AlbumDataSource();
                Long timestamp = (Long) childDataSnapshot.child("created").getValue();

                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTimeInMillis(timestamp);

                SimpleDateFormat fmt = new SimpleDateFormat("h:mm a", Locale.US);
                String time = fmt.format(calendar.getTime()).toString();

                album.setAlbumTitle(childDataSnapshot.getKey().toString());
                album.setAlbumDescription(childDataSnapshot.child("mode").getValue().toString());
                album.setAlbumOwner(childDataSnapshot.child("owner").getValue().toString());
                album.setAlbumTimeStamp(time);



                dataSourceList.add(album);

            }


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

    public void onAlbumAdd(View view) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Albums.this);

        albumnAdd.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);

        final View mView = getLayoutInflater().inflate(R.layout.add_newalbum, null);

        Button add = (Button) mView.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText albumTitleET = (EditText) mView.findViewById(R.id.newAlbumTitle);
                final String newAlbumTitle = albumTitleET.getText().toString();

                List<String> users = new ArrayList<>();
                users.add("enoch");

                AlbumDataSource newAlbum = new AlbumDataSource();
                newAlbum.setAlbumTitle(newAlbumTitle);
                newAlbum.setAlbumOwner("enoch");
                newAlbum.setAlbumDescription("public");
                newAlbum.setUsers(users);

                Commons.pushNewAlbum(newAlbum, getApplicationContext());

                dialog.dismiss();
            }
        });


        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                albumnAdd.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.baseDark), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });

    }


    public void pushNewAlbum(View view) {

        List<String> users = new ArrayList<>();
        users.add("enoch");

        AlbumDataSource newAlbum = new AlbumDataSource();
        newAlbum.setAlbumTitle("hska2" + System.currentTimeMillis());
        newAlbum.setAlbumOwner("enoch");
        //newAlbum.setAlbumTimeStamp(ServerValue.);
        newAlbum.setAlbumDescription("public");
        newAlbum.setUsers(users);

        Commons.pushNewAlbum(newAlbum, getApplicationContext());

    }

    public void onClickAccount(View view){
        Intent i = new Intent(this, Dashboard.class);
        startActivity(i);
    }

}
