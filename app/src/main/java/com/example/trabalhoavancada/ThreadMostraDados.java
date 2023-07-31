package com.example.trabalhoavancada;

import android.content.Context;
import android.widget.TextView;

public class ThreadMostraDados extends Thread {

    private GerenciaDados g;
    private GerenciaDados g2;

    private Reconciliacao rec = new Reconciliacao();
    private Context context;
    private DataReader dataReader = new DataReader();
    private  MemComp m = new MemComp();
    private boolean chave;
    private Dados d;
    private Dados d2;

    private ServicoTransporte servicoTransporte;

    private TextView tvLatitude,tvLongitude,tvTempo,tvDistanciaPercorrida,tvTimeDifference, tvTravelTime, tvVelMedia, tvTravelDistance, tvVelRecomendada, tvConsumo, tvMotoristaV1,tvMotoristaV2, tvCargaV1, tvCargaV2,tvPassageirosV1,tvPassageirosV2;

    public ThreadMostraDados(Context context, boolean chave, TextView tvTimeDifference, TextView tvTravelTime, TextView tvVelMedia, TextView tvTravelDistance, TextView tvVelRecomendada, TextView tvConsumo, ServicoTransporte servicoTransporte, TextView tvMotoristaV1, TextView tvMotoristaV2, TextView tvCargaV1, TextView tvCargaV2, TextView tvPassageirosV1, TextView tvPassageirosV2){
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
        this.tvMotoristaV1 = tvMotoristaV1;
        this.tvMotoristaV2 = tvMotoristaV2;
        this.tvCargaV1 = tvCargaV1;
        this.tvCargaV2 = tvCargaV2;
        this.tvPassageirosV1 = tvPassageirosV1;
        this.tvPassageirosV2 = tvPassageirosV2;

    }

    public void Stop(){
        chave = false;
    }

    @Override
    public void run() {
        while (chave) {
            g = servicoTransporte.getGerenciaDadosVeiculo1();
            g2 = servicoTransporte.getGerenciaDadosVeiculo2();

            if (g != null){
                if (g.VerificaPilha()) {
                    d = servicoTransporte.getVeiculo1();
                    d2 = servicoTransporte.getVeiculo2();

                    double distanciaPercorrida = g.getDistanciaPercorrida();
                    double velMedia = g.getVelMedia();
                    //double velRecomendada = g.getVelRecomendada();
                    double velRecomendada = rec.getVelRecomendada();
                    double consumo = g.getConsumo();

                    double temp = g.getTempoRestante();
                    long travelTime = g.getTravelTime();
                    String motorista = d.getMotorista();
                    String motorista2 = d2.getMotorista();
                    String passageiros1 = g.getPassageiros();
                    String passageiros2 = g2.getPassageiros();
                    String cargas1 = g.getCargas();
                    String cargas2 = g2.getCargas();
                    //tvLatitude.setText(String.valueOf(latitude));
                    //tvLongitude.setText(String.valueOf(longitude));
                    //tvTempo.setText(String.valueOf(tempo));
                    tvTravelDistance.setText("Distancia Percorrida: " + distanciaPercorrida + "km");
                    tvTimeDifference.setText("Tempo até o destino:" + temp + " minutos");
                    tvVelMedia.setText("Velocidade Media: " + velMedia + "km/h");
                    tvTravelTime.setText("Tempo de viagem :" + travelTime + " segundos");
                    tvVelRecomendada.setText("Veocidade Recomendada: " + velRecomendada + "km/h");
                    //tvConsumo.setText("Consumo: " + consumo + " litros");
                    tvMotoristaV1.setText("Motorista: " + motorista);
                    tvMotoristaV2.setText("Motorista: " + motorista2);
                    tvPassageirosV1.setText("Passageiros: "+ passageiros1);
                    tvPassageirosV2.setText("Passageiros: " + passageiros2);
                    tvCargaV1.setText("Cargas: " + cargas1);
                    tvCargaV2.setText("Cargas: " + cargas2);


                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        double velRecomendada = g.getVelRecomendada();
                        double temp = g.getTempoRestante();
                        long travelTime = g.getTravelTime();
                        tvTravelDistance.setText("Distancia Percorrida: 0 km");
                        tvTimeDifference.setText("Tempo até o destino:" + temp + " minutos");
                        tvVelMedia.setText("Velocidade Media: " + 0 + "km/h");
                        tvTravelTime.setText("Tempo de viagem :" + travelTime + " segundos");
                        tvVelRecomendada.setText("Veocidade Recomendada: " + velRecomendada + "km/h");
                        //tvConsumo.setText("Consumo: " + 0 + " litros");

                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }


        }


        }



    }
}
