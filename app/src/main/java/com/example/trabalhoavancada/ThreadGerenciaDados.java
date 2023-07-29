package com.example.trabalhoavancada;

import android.content.Context;
import android.widget.TextView;

public class ThreadGerenciaDados extends Thread {

    private GerenciaDados g;
    private Context context;
    private DataReader dataReader = new DataReader();
    private  MemComp m = new MemComp();
    private boolean chave;
    private Dados d;

    private ServicoTransporte servicoTransporte;

    private TextView tvLatitude,tvLongitude,tvTempo,tvDistanciaPercorrida,tvTimeDifference, tvTravelTime, tvVelMedia, tvTravelDistance, tvVelRecomendada, tvConsumo;

    public ThreadGerenciaDados(Context context, boolean chave, TextView tvTimeDifference, TextView tvTravelTime,TextView tvVelMedia,TextView tvTravelDistance,TextView tvVelRecomendada,TextView tvConsumo, ServicoTransporte servicoTransporte ){
        this.context = context;
        this.chave = chave;
        //this.tvLatitude = tvLatitude;
        //this.tvLongitude = tvLongitude;
        //this.tvTempo = tvTempo;
        //this.tvDistanciaPercorrida = tvDistanciaPercorrida;
        this.tvTimeDifference = tvTimeDifference;
        this.tvTravelTime = tvTravelTime;
        this.tvVelMedia = tvVelMedia;
        this.tvTravelDistance = tvTravelDistance;
        this.tvVelRecomendada = tvVelRecomendada;
        this.tvConsumo = tvConsumo;
        this.servicoTransporte = servicoTransporte;
    }

    public void Stop(){
        chave = false;
    }

    @Override
    public void run() {
        while (chave) {
            g = servicoTransporte.getGerenciaDadosVeiculo1();
            try {
                servicoTransporte.setDadosVeiculo1();
                servicoTransporte.setDadosVeiculo2();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(g.VerificaPilha()) {
                g.reconcilia();
                d = servicoTransporte.getVeiculo1();
                double distanciaPercorrida = g.calculaDistanciaTotalPercorrida();
                double velMedia = g.CalculaVelocidadeMedia();
                double velRecomendada = g.VelocidadeRecomendada();
                double consumo = g.calcularConsumo();

                double latitude = d.getLatitude();
                double longitude = d.getLongitude();
                long tempo = d.getTime();
                double temp = g.calculateTimeDifference();
                long travelTime = g.calculaTempoDeslocamento();
                //tvLatitude.setText(String.valueOf(latitude));
                //tvLongitude.setText(String.valueOf(longitude));
                //tvTempo.setText(String.valueOf(tempo));
                tvTravelDistance.setText("Distancia Percorrida: " + distanciaPercorrida + "km");
                tvTimeDifference.setText("Tempo até o destino:" + temp + " minutos");
                tvVelMedia.setText("Velocidade Media: " + velMedia + "km/h");
                tvTravelTime.setText("Tempo de viagem :"+ travelTime + " segundos");
                tvVelRecomendada.setText("Veocidade Recomendada: " + velRecomendada + "km/h");
                tvConsumo.setText("Consumo: " + consumo + " litros");



                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            else{
                try {
                    double velRecomendada =  g.VelocidadeRecomendada();
                    double temp = g.calculateTimeDifference();
                    long travelTime = g.calculaTempoDeslocamento();
                    tvTravelDistance.setText("Distancia Percorrida: 0 km");
                    tvTimeDifference.setText("Tempo até o destino:" + temp + " minutos");
                    tvVelMedia.setText("Velocidade Media: " + 0 + "km/h");
                    tvTravelTime.setText("Tempo de viagem :"+ travelTime + " segundos");
                    tvVelRecomendada.setText("Veocidade Recomendada: " + velRecomendada + "km/h");
                    tvConsumo.setText("Consumo: " + 0 + " litros");

                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }



        }

        try {
            m.escreveGerenciaDadosV1(g);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
