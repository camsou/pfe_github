package com.example.camil.detectnalert;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.camil.detectnalert.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends BaseActivity {

    private static final String TAG = "MyAccountActivity";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    FirebaseHelper helper;

    String username;

    // Tables containing data from Firebase Realtime Database
    ArrayList<User> users_table = new ArrayList<User>();


    /* Creation de la page */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(mDatabase);

        //------------------------------------------------------------------------------
        // Update patients
        //------------------------------------------------------------------------------
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                //Get Users from Firebase Realtime Database
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                    User user = new User(
                            areaSnapshot.child("username").getValue(String.class),
                            areaSnapshot.child("email").getValue(String.class),
                            areaSnapshot.child("profession").getValue(String.class),
                            areaSnapshot.child("patient").getValue(String.class),
                            areaSnapshot.child("etage").getValue(String.class)
                    );
                    Log.d(TAG, user.GetEmail() + "");
                    Log.d(TAG, user.GetUsername() + "");
                    Log.d(TAG, user.GetProfession() + "");
                    Log.d(TAG, user.GetPatient() + "");
                    Log.d(TAG, user.GetEtage() + "");
                    users_table.add(user);
                }

                mPatient = (TextView) findViewById(R.id.nb_patient);
                mEtage = (TextView) findViewById(R.id.etage);

                String patient;
                String etage;
                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

    // [START on_start_check_user]
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/
    // [END on_start_check_user]

    private TextView mStatusTextView;
    private TextView mProfession;
    private TextView mPatient;
    private TextView mEtage;

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Getting the information
            mStatusTextView = (TextView) findViewById(R.id.email);
            mProfession = (TextView) findViewById(R.id.user);

            // Set information
            username = usernameFromEmail(user.getEmail());
            mStatusTextView.setText(username);
            for (int i = 0; i < users_table.size(); i++)
            {
                if (users_table.get(i).GetUsername().equals(username))
                {
                    mPatient.setText(getString(R.string.nb_patient,users_table.get(i).GetPatient()));
                    mEtage.setText(getString(R.string.etage2, users_table.get(i).GetEtage()));
                    mProfession.setText(users_table.get(i).GetProfession());
                }
            }

        } else {
            mStatusTextView.setText(R.string.signed_out);
        }
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}

