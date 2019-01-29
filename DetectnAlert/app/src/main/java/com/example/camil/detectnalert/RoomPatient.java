package com.example.camil.detectnalert;

public class RoomPatient {
    String id_card;
    String id_patient;
    String timestamp_in_room;

    public RoomPatient(){
        //Constructeur
    }

    public RoomPatient(String id_card,String id_patient,String timestamp_in_room ){

        this.id_card= id_card;
        this.id_patient=id_patient;
        this.timestamp_in_room= timestamp_in_room;
    }

    public String GetIdCard(){ return id_card; }
    public String GetPatientID(){ return id_patient; }
    public String GetTimestampRoom(){ return timestamp_in_room; }


}
