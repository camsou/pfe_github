package com.example.camil.detectnalert;

public class Weights {
    int id_card;
    int id_weight;
    int timestamp_weight;
    Float value_weight;

    public Weights() {
        //Constructeur
    }

    public Weights(int id_card, int id_weight, int timestamp_weight, Float value_weight) {

        this.id_card = id_card;
        this.id_weight = id_weight;
        this.timestamp_weight = timestamp_weight;
        this.value_weight = value_weight;
    }

    public int GetIdCard() {
        return id_card;
    }

    public int GetIdWeight() {
        return id_weight;
    }

    public int GetTimestampWeight() {
        return timestamp_weight;
    }

    public Float GetValueWeight() {
        return value_weight;
    }
}