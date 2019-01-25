package com.example.camil.detectnalert;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAccountActivity extends MainActivity {

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    /* Creation de la page */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        super.onCreateDrawer();

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            // Getting the information
            mStatusTextView = (TextView) findViewById(R.id.email);

            // Set information
            String username = usernameFromEmail(user.getEmail());
            mStatusTextView.setText(username);

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

