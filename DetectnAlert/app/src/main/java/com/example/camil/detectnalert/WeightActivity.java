package com.example.camil.detectnalert;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camil.detectnalert.ViewHolder.Graphique;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeightActivity extends MainActivity  {

    double Y_AXIS_MARGIN = 10.0;

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
    TextView id_timestamp_last_weight;
    GraphView graph;
    LineGraphSeries series;
    final List<String> patients = new ArrayList<String>();

    // Tables containing data from Firebase Realtime Database
    ArrayList<Patients>     patients_table      = new ArrayList<Patients>();
    ArrayList<RoomPatient>  room_patient_table  = new ArrayList<RoomPatient>();
    ArrayList<Weights>      weights_table       = new ArrayList<Weights>();

    //Table containing data of weight and date
    ArrayList<Graphique>    graph_in_room     = new ArrayList<Graphique>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);


        // Database elements
        db=FirebaseDatabase.getInstance().getReference();
        helper=new FirebaseHelper(db);

        // Layout elements
        spinner                  = (Spinner)  findViewById(R.id.patient) ;
        id_sex                   = (TextView) findViewById(R.id.id_sex_value) ;
        id_name                  = (TextView) findViewById(R.id.id_name_value) ;
        id_firstname             = (TextView) findViewById(R.id.id_firstname_value) ;
        id_timestamp             = (TextView) findViewById(R.id.id_timestamp_value) ;
        id_weight                = (TextView) findViewById(R.id.id_weight_value) ;
        id_timestamp_last_weight = (TextView) findViewById(R.id.id_timestamp_last_weight_value) ;

        // Graph elements
        graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("Historique pesées");
        graph.getGridLabelRenderer().setGridColor(0xFFDDDDDD);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        series = new LineGraphSeries();
        series.setColor(0xFF58EEF2);
        series.setBackgroundColor(0x33A5E6E8);
        series.setDrawBackground(true);
        series.setDrawDataPoints(true);
        series.setThickness(2);

        //------------------------------------------------------------------------------
        // Update patients
        //------------------------------------------------------------------------------
        db.child("patients").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

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




                /* Fill spinner with patients */
                ArrayAdapter<String> patients_final = new ArrayAdapter<String>(WeightActivity.this, android.R.layout.simple_spinner_item, patients);
                patients_final.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(patients_final);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        updateView(i);
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
        db.child("weights").orderByChild("id_weight").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Re-initialize weights_table before filling it.
                weights_table = new ArrayList<Weights>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    Weights wei = new Weights(
                            Integer.parseInt(String.valueOf(areaSnapshot.child("id_card").getValue())),
                            Integer.parseInt(String.valueOf(areaSnapshot.child("id_weight").getValue())),
                            Integer.parseInt(String.valueOf(areaSnapshot.child("timestamp_weight").getValue())),
                            Float.parseFloat(String.valueOf(areaSnapshot.child("value").getValue()))
                    );

                    weights_table.add(wei);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // cancel
            }
        });

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
     * @param weights_table the patient's weights table.
     * @return an array of given patient's weights.
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

    /**
     * Function that gets a graph made of given patient's weights.
     * @param id_card the room ID (corresponding to 1 patient).
     * @param weights_table the patient's weights table.
     * @return an array of "Graphique" points made of given patient's weights.
     */
    protected ArrayList<Graphique> getGraphInRoom(int id_card, ArrayList<Weights> weights_table)
    {
        ArrayList<Graphique> graph_in_room         = new ArrayList<>();
        ArrayList<Graphique> ordered_graph_in_room = new ArrayList<>();

        Graphique graph_point;
        Graphique last_point = new Graphique(0, 0f);

        for (int i = 0; i < weights_table.size(); i++)
        {
            if (weights_table.get(i).GetIdCard() == id_card)
            {
                graph_point = new Graphique(
                        weights_table.get(i).GetTimestampWeight(),
                        weights_table.get(i).GetValueWeight()
                );
                graph_in_room.add(graph_point);
            }
        }

        return graph_in_room;
    }

    /**
     * Update page view (patient's data and graph).
     * @param i index of patient selected in spinner.
     */
    protected void updateView(int i)
    {
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

        // Display last weight and its timestamp on screen
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
            String timestamp_last_weight = String.valueOf(last_weight.GetTimestampWeight());
            timestamp_last_weight =
                    timestamp_last_weight.substring(0, 4)
                            + "/" + timestamp_last_weight.substring(4, 6)
                            + "/" + timestamp_last_weight.substring(6, timestamp_last_weight.length());
            id_weight.setText(text);
            id_timestamp_last_weight.setText(timestamp_last_weight);
        }

        // Draw graph
        graph_in_room = getGraphInRoom(id_card, weights_table);
        DataPoint datapoint = null;
        series.resetData(new DataPoint[] {});

        for (int g = 0; g < graph_in_room.size(); g++)
        {
            datapoint = new DataPoint(Double.valueOf(graph_in_room.get(g).GetxValue()), Double.valueOf(graph_in_room.get(g).GetyValue()));
            series.appendData(datapoint, false, 5);
        }

        graph.removeAllSeries();
        double lastY = graph_in_room.get(graph_in_room.size() - 1).GetyValue();
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(lastY + Y_AXIS_MARGIN);
        graph.getViewport().setMinY(lastY - Y_AXIS_MARGIN);
        graph.addSeries(series);

        // Information about specified weight on Tap
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(
                        graph.getContext(),
                        Double.valueOf(dataPoint.getY()).floatValue() + " le "
                        + String.valueOf(Double.valueOf(dataPoint.getX()).intValue()).substring(0, 4)
                        + "/" + String.valueOf(Double.valueOf(dataPoint.getX()).intValue()).substring(4, 6)
                        + "/" + String.valueOf(Double.valueOf(dataPoint.getX()).intValue()).substring(6, String.valueOf(Double.valueOf(dataPoint.getX()).intValue()).length()), Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}

