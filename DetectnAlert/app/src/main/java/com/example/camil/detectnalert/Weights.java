package com.example.camil.detectnalert;

public class Weights {
    String id_card;
    String id_weight;
    int timestamp_weight;
    int value_weight;

    public Weights() {
        //Constructeur
    }

    public Weights(String id_card, String id_weight, int timestamp_weight, int value_weight) {

        this.id_card = id_card;
        this.id_weight = id_weight;
        this.timestamp_weight = timestamp_weight;
        this.value_weight = value_weight;
    }

    public String GetIdCard() {
        return id_card;
    }

    public String GetIdWeight() {
        return id_weight;
    }

    public int GetTimestampWeight() {
        return timestamp_weight;
    }

    public int GetValueWeight() {
        return value_weight;
    }
}