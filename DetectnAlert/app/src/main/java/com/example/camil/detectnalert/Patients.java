package com.example.camil.detectnalert;

public class Patients {

    private int id_patient;
    private String name;
    private String first_name;
    private int timestamp_in_ephad;
    private String sex;

    public Patients()
    {
        //Constructeur
    }

    public Patients(int id_patient, String name, String first_name, int timestamp_in_ephad, String sex){
        this.id_patient = id_patient;
        this.name = name;
        this.first_name = first_name;
        this.timestamp_in_ephad = timestamp_in_ephad;
        this.sex = sex;
    }

    protected String GetPatientName()
    {
        return name;
    }

    protected String GetPatientFirstName()
    {
        return first_name;
    }

    protected int GetPatientTimestamp()
    {
        return timestamp_in_ephad;
    }

    protected String GetPatientSex()
    {
        return sex;
    }

    protected int GetPatientID()
    {
        return id_patient;
    }
}
