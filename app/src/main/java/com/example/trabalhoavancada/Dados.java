package com.example.trabalhoavancada;


import java.util.List;

public class Dados {

    public long time;
    private double X;
    private double Y;

    private String motorista;

    private List<String> passageiros;

    private  List<String> cargas;


    public Dados(double dadoX, double dadoY, long tempo) {
        this.time = tempo;
        this.X = dadoX;
        this.Y = dadoY;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public void setCargas(List<String> cargas) {
        this.cargas = cargas;
    }

    public void setPassageiros(List<String> passageiros) {
        this.passageiros = passageiros;
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

    public List<String> getCargas() {
        return cargas;
    }

    public List<String> getPassageiros() {
        return passageiros;
    }

    public String getMotorista() {
        return motorista;
    }
}
