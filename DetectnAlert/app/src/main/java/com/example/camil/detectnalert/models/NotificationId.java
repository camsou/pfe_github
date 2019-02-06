package com.example.camil.detectnalert.models;

public class NotificationId {

    public int Etat;
    public String Prenom;
    public String Nom;

    public NotificationId() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public NotificationId(int Etat, String Nom, String Prenom) {
        this.Etat = Etat;
        this.Nom = Nom;
        this.Prenom = Prenom;
    }

    public int GetEtat()
    {
        return Etat;
    }

    public String GetPrenom()
    {
        return Prenom;
    }

    public String GetNom()
    {
        return Nom;
    }
}
