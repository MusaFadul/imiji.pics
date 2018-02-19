package com.example.musa.imijifrb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private EditText usernameET;
    private EditText passwordET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        usernameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);

    }

    public void signInUser(View view){

        final String email = usernameET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

        Log.d("Data", email + " " + password);


        //String password = ( (EditText) mView.findViewById(R.id.password)).getText().toString().trim();


        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.d("LOGIN", "createUserWithEmail:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {

                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Log.e("Error", "Failed Registration", e);

                                Toast.makeText(Login.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else{
                                Intent i = new Intent(Login.this, Dashboard.class);
                                i.putExtra("email", email);
                                startActivity(i);
                            }
                        }
                    });

        } else {


        }



    }

    public void registerNewUser(View view){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Login.this);

        final View mView = getLayoutInflater().inflate(R.layout.register, null);

        Button register = (Button) mView.findViewById(R.id.registerBtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = ( (EditText) mView.findViewById(R.id.newUsername)).getText().toString().trim();
                String password = ( (EditText) mView.findViewById(R.id.newPassword)).getText().toString().trim();

                mDatabase = FirebaseDatabase.getInstance().getReference();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("LOGIN", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {

                                        FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                        Log.e("LoginActivity", "Failed Registration", e);

                                        Toast.makeText(Login.this, e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    } else{

                                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                        mDatabase.child("users").child(currentFirebaseUser.getUid());

                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                                        mDatabase.child(currentFirebaseUser.getUid()).setValue(ServerValue.TIMESTAMP);

                                        Intent i = new Intent(Login.this, Dashboard.class);
                                        i.putExtra("email", email);
                                        startActivity(i);
                                    }

                                    // ...
                                }
                            });

                } else {

                    ( (EditText) mView.findViewById(R.id.newUsername)).setError("Error");
                    ( (EditText) mView.findViewById(R.id.newPassword)).setError("Error");
                }

            }
        });



        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();



    }
}
