package com.example.musa.imijifrb;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

/**
 * Created by musam on 18/02/2018.
 */

public class Commons {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public static void pushNewAlbum(AlbumDataSource newAlbum, Context context) {

        final Context mContext = context;
        final AlbumDataSource album = newAlbum;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("albums");
        mDatabase.child(album.getAlbumTitle()).setValue(album.getAlbumOwner()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    mDatabase = mDatabase.child(album.getAlbumTitle());
                    mDatabase.child("owner").setValue(album.getAlbumOwner());
                    mDatabase.child("mode").setValue(album.getAlbumDescription());
                    mDatabase.child("created").setValue(ServerValue.TIMESTAMP);

                    for(String user : album.getUsers()){
                        mDatabase.child("users").setValue(user);
                    }

                    Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();



                } else{
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
