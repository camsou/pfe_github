package com.example.camil.detectnalert;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
    TextView id_sex;
    TextView id_name;
    TextView id_firstname;
    TextView id_patient;
    TextView id_timestamp;
    TextView id_weight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);


        db=FirebaseDatabase.getInstance().getReference();
        helper=new FirebaseHelper(db);

        db.child("patients").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final List<String> patients = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String name = areaSnapshot.child("name").getValue(String.class);
                    Patients pat = areaSnapshot.getValue(Patients.class);
                    patients.add(name);
                    table.add(pat);
                }

                spinner = (Spinner) findViewById(R.id.patient) ;
                id_sex = (TextView) findViewById(R.id.id_sex) ;
                id_name = (TextView) findViewById(R.id.id_name) ;
                id_firstname = (TextView) findViewById(R.id.id_firstname) ;
                //id_patient = (TextView) findViewById(R.id.id_patient) ;
                id_timestamp = (TextView) findViewById(R.id.id_timestamp) ;



                ArrayAdapter<String> patients_final = new ArrayAdapter<String>(WeightActivity.this, android.R.layout.simple_spinner_item, patients);
                patients_final.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(patients_final);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String selected = spinner.getItemAtPosition(i).toString();


                        String sex;
                        sex = patients_table.get(i).GetPatientSex();
                        id_sex.setText(sex);

                        String name;
                        name = patients_table.get(i).GetPatientName();
                        id_name.setText(name);

                        String name_first;
                        name_first = patients_table.get(i).GetPatientFirstName();
                        id_firstname.setText(name_first);

                        String id;
                        id = patients_table.get(i).GetPatientID();
                        //id_patient.setText(id);

                        String timestamp_ehpad;
                        timestamp_ehpad = patients_table.get(i).GetPatientTimestamp();
                        id_timestamp.setText(timestamp_ehpad);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
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

