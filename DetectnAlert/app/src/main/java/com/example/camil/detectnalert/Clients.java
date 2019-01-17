package com.example.camil.detectnalert;

public class Clients {

    public int id;
    public String name;
    public String surname;
    public int birth_date;
    public Character sex;

    public Clients()
    {
        //Constructeur
    }

    public Clients(int id, String name, String surname, int birth_date, Character sex){
        this.id= id;
        this.name = name;
        this.surname = surname;
        this.birth_date = birth_date;
        this.sex = sex;
    }
    public String GetClientName() {
        return name;
    }

}
