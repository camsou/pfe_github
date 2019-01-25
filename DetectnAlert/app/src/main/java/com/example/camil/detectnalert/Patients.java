package com.example.camil.detectnalert;

public class Patients {

    public String id;
    public String name;
    public String first_name;
    public String timestamp_Ephad;
    public String sex;

    public Patients()
    {
        //Constructeur
    }

    public Patients(String id, String name, String first_name, String timestamp_Ephad, String sex){
        this.id= id;
        this.name = name;
        this.first_name = first_name;
        this.timestamp_Ephad = timestamp_Ephad;
        this.sex = sex;
    }
    public String GetPatientName() {
        return name;
    }
    public String GetPatientFirstName() {
        return first_name;
    }
    public String GetPatientTimestamp() {
        return timestamp_Ephad;
    }
    public String GetPatientSex() {
        return sex;
    }
    public String GetPatientID() {
        return id;
    }


}
