package com.example.camil.detectnalert.ViewHolder;

public class Graphique {
    int  xValue; // timestamp
    Float yValue; // value

    public Graphique(){

    }

    public Graphique(int xValue, Float yValue){

        this.xValue= xValue;
        this.yValue= yValue;

    }

    public int GetxValue(){
        return xValue;
    }

    public Float GetyValue(){
        return  yValue; 
    }
}
