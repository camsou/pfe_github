package com.example.camil.detectnalert.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String profession;
    public String patient;
    public String etage;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String profession, String patient, String etage) {
        this.username = username;
        this.email = email;
        this.profession = profession;
        this.patient = patient;
        this.etage = etage;
    }


}
// [END blog_user_class]