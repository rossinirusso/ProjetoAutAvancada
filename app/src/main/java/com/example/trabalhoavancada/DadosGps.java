package com.example.trabalhoavancada;


import java.util.List;

public class DadosGps {

    public long time;
    private double X;
    private double Y;
    private String motorista;

    private List<String> passageiros;

    private  List<String> cargas;



    public void setDado(double dadoX, double dadoY,long tempo) {
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

    public double getX() {
        return X;
    }

    public double getY() {
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



