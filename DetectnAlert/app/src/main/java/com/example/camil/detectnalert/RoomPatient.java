package com.example.camil.detectnalert;

public class RoomPatient {
    String id_card;
    String id_id;
    String timestamp_in_room;

    public RoomPatient(){
        //Constructeur
    }

    public RoomPatient(String id_card,String id_id,String timestamp_in_room ){

        this.id_card= id_card;
        this.id_id=id_id;
        this.timestamp_in_room= timestamp_in_room;
    }

    public String GetIdCard(){ return id_card; }
    public String GetPatientID(){ return id_id; }
    public String GetTimestampRoom(){ return timestamp_in_room; }


}
