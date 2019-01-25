package com.example.camil.detectnalert;

import android.os.Bundle;
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

public class MyAccountActivity extends BaseActivity {

    private static final String TAG = "MyAccountActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    /* Creation de la page */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private TextView mStatusTextView;
    private TextView mPatient;
    private TextView mEtage;

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            // Getting the information
            mStatusTextView = (TextView) findViewById(R.id.email);

            // Set information
            String username = usernameFromEmail(user.getEmail());
            mStatusTextView.setText(username);
            //String patient =
            //mPatient.setText(patient);
            //mEtage.setText(etage);

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

    // Read from the database
    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            User user = dataSnapshot.getValue(User.class);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            Log.w(TAG, "loadUser:onCancelled", error.toException());
        }
    };
}

