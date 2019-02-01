package com.example.camil.detectnalert.ViewHolder;

public class graphique {
    int xValue;
    int yValue;

    public  graphique(){

    }

    public graphique(int xValue, int yValue){

        this.xValue= xValue;
        this.yValue= yValue;

    }

    public int GetxValue(){
        return xValue;
    }

    public int GetyValue(){
        return  yValue; 
    }
}
