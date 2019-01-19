package com.example.camil.detectnalert;

public class Patients {

    public int id;
    public String name;
    public String first_name;
    public int birth_date;
    public Character sex;

    public Patients()
    {
        //Constructeur
    }

    public Patients(int id, String name, String first_name, int birth_date, Character sex){
        this.id= id;
        this.name = name;
        this.first_name = first_name;
        this.birth_date = birth_date;
        this.sex = sex;
    }
    public String GetPatientName() {
        return name;
    }

}
