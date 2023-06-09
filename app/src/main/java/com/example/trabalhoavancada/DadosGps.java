package com.example.trabalhoavancada;


public class DadosGps {

    public long time;
    private double X;
    private double Y;
    private double Z;


    public void setDado(double dadoX, double dadoY,long tempo) {
        this.time = tempo;
        this.X = dadoX;
        this.Y = dadoY;
    }


    public long getTime() {
        return time;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public double getZ() {
        return Z;
    }


}
