package com.example.camil.detectnalert;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.camil.detectnalert.models.NotificationId;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlertActivity extends MainActivity {

    private static final String TAG = "AlertActivty";

    private Button mAlertButton;
    DatabaseReference mDatabase;
    FirebaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        super.onCreateDrawer();

        // Database elements
        mDatabase = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(mDatabase);

        mDatabase.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                /* Get Patients from Firebase Realtime Database */
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                    NotificationId notif = new NotificationId(
                            areaSnapshot.child("Etat").getValue(Integer.class),
                            areaSnapshot.child("Nom").getValue(String.class),
                            areaSnapshot.child("Prenom").getValue(String.class)
                    );

                    Log.d(TAG, notif.GetEtat() + "");
                    Log.d(TAG, notif.GetNom() + "");
                    Log.d(TAG, notif.GetPrenom() + "");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        mAlertButton= (Button) findViewById(R.id.alertButton);

        //Action boutton alerte prise en charge
        mAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User clicked the button
                Intent HistoricIntent = new Intent(AlertActivity.this, HistoricActivity.class);
                startActivity(HistoricIntent);
                mDatabase.child("Notification").child("Notification_1").child("Etat").setValue(0);
            }

        });
    }
}