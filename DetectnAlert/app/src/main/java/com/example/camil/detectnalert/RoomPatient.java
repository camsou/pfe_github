package com.example.camil.detectnalert;

public class RoomPatient {
    int id_card;
    int id_patient;
    int timestamp_in_room;

    public RoomPatient(){
        //Constructeur
    }

    public RoomPatient(int id_card, int id_patient, int timestamp_in_room ){

        this.id_card= id_card;
        this.id_patient=id_patient;
        this.timestamp_in_room= timestamp_in_room;
    }

    public int GetIdCard(){ return id_card; }
    public int GetPatientID(){ return id_patient; }
    public int GetTimestampRoom(){ return timestamp_in_room; }


}
