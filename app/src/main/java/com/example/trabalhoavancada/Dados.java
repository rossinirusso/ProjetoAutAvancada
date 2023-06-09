package com.example.trabalhoavancada;


public class Dados {

    public long time;
    private double X;
    private double Y;
    private double Z;


    public Dados(double dadoX, double dadoY, long tempo) {
        this.time = tempo;
        this.X = dadoX;
        this.Y = dadoY;
    }


    public long getTime() {
        return time;
    }

    public double getLatitude() {
        return X;
    }

    public double getLongitude() {
        return Y;
    }



}
