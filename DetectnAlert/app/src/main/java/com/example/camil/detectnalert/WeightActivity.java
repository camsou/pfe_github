package com.example.camil.detectnalert;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.camil.detectnalert.ViewHolder.Graphique;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

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
    GraphView graph;
    LineGraphSeries series;

    // Tables containing data from Firebase Realtime Database
    ArrayList<Patients>     patients_table      = new ArrayList<Patients>();
    ArrayList<RoomPatient>  room_patient_table  = new ArrayList<RoomPatient>();
    ArrayList<Weights>      weights_table       = new ArrayList<Weights>();

    //Table containing data of weight and date
    ArrayList<Graphique>    graphique_table     = new ArrayList<Graphique>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);


        db=FirebaseDatabase.getInstance().getReference();
        helper=new FirebaseHelper(db);

        //------------------------------------------------------------------------------
        // Update patients
        //------------------------------------------------------------------------------
        db.child("patients").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final List<String> patients = new ArrayList<String>();

                /* Get Patients from Firebase Realtime Database */
                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {

                    String name         = areaSnapshot.child("name").getValue(String.class);
                    String first_name   = areaSnapshot.child("first_name").getValue(String.class);

                    Patients pat = new Patients(
                            areaSnapshot.child("id_patient").getValue(Integer.class),
                            areaSnapshot.child("name").getValue(String.class),
                            areaSnapshot.child("first_name").getValue(String.class),
                            areaSnapshot.child("timestamp_in_ehpad").getValue(Integer.class),
                            areaSnapshot.child("sex").getValue(String.class)
                    );

                    // Name in Spinner
                    patients.add(first_name + " " + name);
                    patients_table.add(pat);
                }

                spinner = (Spinner) findViewById(R.id.patient) ;
                id_sex = (TextView) findViewById(R.id.id_sex_value) ;
                id_name = (TextView) findViewById(R.id.id_name_value) ;
                id_firstname = (TextView) findViewById(R.id.id_firstname_value) ;
                //id_patient = (TextView) findViewById(R.id.id_patient) ;
                graph = (GraphView) findViewById(R.id.graph);
                series = new LineGraphSeries();
                graph.addSeries(series);
                id_timestamp = (TextView) findViewById(R.id.id_timestamp_value) ;
                id_weight = (TextView) findViewById(R.id.id_weight_value) ;


                /* Fill spinner with patients */
                ArrayAdapter<String> patients_final = new ArrayAdapter<String>(WeightActivity.this, android.R.layout.simple_spinner_item, patients);
                patients_final.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(patients_final);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String selected = spinner.getItemAtPosition(i).toString();


                        String sex;
                        sex = patients_table.get(i).GetPatientSex();
                        if (sex.equals("h"))
                        {
                            id_sex.setText(R.string.sex_h);
                        }
                        else
                        {
                            id_sex.setText(R.string.sex_f);
                        }

                        String name;
                        name = patients_table.get(i).GetPatientName();
                        id_name.setText(name);

                        String name_first;
                        name_first = patients_table.get(i).GetPatientFirstName();
                        id_firstname.setText(name_first);

                        int id_patient;
                        id_patient = patients_table.get(i).GetPatientID();
                        //id_patient.setText(id);

                        int timestamp_ehpad;
                        timestamp_ehpad = patients_table.get(i).GetPatientTimestamp();

                        // Change timestamp to string just for display
                        // (int) 20192305 -> (String) "2019/23/05"
                        String timestamp_ehpad_string = String.valueOf(timestamp_ehpad);
                        timestamp_ehpad_string =
                                timestamp_ehpad_string.substring(0, 4)
                                + "/" + timestamp_ehpad_string.substring(4, 6)
                                + "/" + timestamp_ehpad_string.substring(6, timestamp_ehpad_string.length());
                        id_timestamp.setText(timestamp_ehpad_string);

                        int                id_card        = matchIdCard(id_patient, room_patient_table);
                        ArrayList<Weights> weight_in_room = getWeightsInRoom(id_card, weights_table);

                        Weights last_weight = new Weights();
                        if (weight_in_room.size() != 0)
                        {
                            for (int w = 0; w < weight_in_room.size(); w++)
                            {
                                if (weight_in_room.get(w).GetTimestampWeight() > last_weight.GetTimestampWeight())
                                {
                                    last_weight = weight_in_room.get(w);
                                }
                            }

                            String text = last_weight.GetValueWeight() + " kg";
                            id_weight.setText(text);
                        }

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

        //------------------------------------------------------------------------------
        // Update room_patient
        //------------------------------------------------------------------------------
        db.child("room_patient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {

                    RoomPatient rp = new RoomPatient(
                            Integer.parseInt(String.valueOf(areaSnapshot.child("id_card").getValue())),
                            Integer.parseInt(String.valueOf(areaSnapshot.child("id_patient").getValue())),
                            Integer.parseInt(String.valueOf(areaSnapshot.child("timestamp_in_room").getValue()))
                    );

                    room_patient_table.add(rp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // cancel
            }
        });

        //------------------------------------------------------------------------------
        // Update weights
        //------------------------------------------------------------------------------
        db.child("weights").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    Weights wei = new Weights(
                            Integer.parseInt(String.valueOf(areaSnapshot.child("id_card").getValue())),
                            Integer.parseInt(String.valueOf(areaSnapshot.child("id_weight").getValue())),
                            Integer.parseInt(String.valueOf(areaSnapshot.child("timestamp_weight").getValue())),
                            Float.parseFloat(String.valueOf(areaSnapshot.child("value").getValue()))
                    );
                    Graphique gra = new Graphique(
                            Integer.parseInt(String.valueOf(areaSnapshot.child("timestamp_weight").getValue())),
                            Float.parseFloat(String.valueOf(areaSnapshot.child("value").getValue()))
                    );

                    weights_table.add(wei);
                    graphique_table.add(gra);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // cancel
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

    /**
     * Function that matches the patient ID with the room ID.
     *
     * @param id the patient id.
     * @param room_patient_table the RoomPatient table.
     * @return the room id (id_card), null if error.
     */
    protected int matchIdCard(int id, ArrayList<RoomPatient> room_patient_table)
    {
        for (int i = 0; i < room_patient_table.size(); i++)
        {
            if (room_patient_table.get(i).GetPatientID() == id)
            {
                return room_patient_table.get(i).id_card;
            }
        }

        return 0;
    }

    /**
     * Function that gets weight value in given room.
     *
     * @param id_card the room ID.
     * @param weights_table the weight table.
     * @return the weight value, null if error.
     */
    protected ArrayList<Weights> getWeightsInRoom(int id_card, ArrayList<Weights> weights_table)
    {
        ArrayList<Weights> weights_in_room = new ArrayList<>();

        for (int i = 0; i < weights_table.size(); i++)
        {
            if (weights_table.get(i).GetIdCard() == id_card)
            {
                weights_in_room.add(weights_table.get(i));
            }
        }

        return weights_in_room;
    }
}

