package com.example.camil.detectnalert;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class WeightActivity extends MainActivity  {

    //private Button mAlertButton;
    Spinner spinner;
    DatabaseReference db;
    FirebaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);


        db=FirebaseDatabase.getInstance().getReference();
        helper=new FirebaseHelper(db);

        db.child("patients").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> patients = new ArrayList<String>();


                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String name = areaSnapshot.child("name").getValue(String.class);
                    patients.add(name);
                }

                spinner = (Spinner) findViewById(R.id.patient) ;
                ArrayAdapter<String> patients_final = new ArrayAdapter<String>(WeightActivity.this, android.R.layout.simple_spinner_item, patients);
                patients_final.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(patients_final);
            }


            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
        //mAlertButton= (Button) findViewById(R.id.alertButton);

        /*
        mAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User clicked the button
                Intent HistoricIntent = new Intent(AlertActivity.this, HistoricActivity.class);
                startActivity(HistoricIntent);

            }

        });
        */
        }
}

