package com.example.trabalhoavancada;

import android.content.Context;
import android.widget.TextView;

public class ThreadGerenciaDados extends Thread {

    private GerenciaDados g = new GerenciaDados();
    private Context context;
    private DataReader dataReader = new DataReader();
    private boolean chave;
    private Dados d;

    private TextView tvLatitude,tvLongitude,tvTempo,tvDistanciaPercorrida,tvTimeDifference, tvTravelTime, tvVelMedia, tvTravelDistance, tvVelRecomendada;

    public ThreadGerenciaDados(Context context, boolean chave, TextView tvTimeDifference, TextView tvTravelTime,TextView tvVelMedia,TextView tvTravelDistance,TextView tvVelRecomendada ){
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
    }

    @Override
    public void run() {
        while (chave) {
            g.SalvaDados(dataReader.readData(context));
            if(g.VerificaPilha()) {
                d = g.GetDado();
                double distanciaPercorrida = g.calculaDistanciaTotalPercorrida();
                double velMedia = g.CalculaVelocidadeMedia();
                double velRecomendada = g.VelocidadeRecomendada();

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



                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            else{
                try {
                    double velRecomendada =  g.VelocidadeRecomendada();
                    tvVelRecomendada.setText("Veocidade Recomendada: " + velRecomendada + "km/h");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }


    }
}
