package com.example.trabalhoavancada;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

public class ThreadGerenciaDados extends Thread {

    private GerenciaDados g;
    private Context context;
    private DataReader dataReader = new DataReader();
    private  MemComp m = new MemComp();
    private boolean chave;
    private Dados d;
    private int id;

    private ServicoTransporte servicoTransporte;


    public ThreadGerenciaDados(Context context, boolean chave, ServicoTransporte servicoTransporte, GerenciaDados g, int id ){
        this.context = context;
        this.chave = chave;
        this.servicoTransporte = servicoTransporte;
        this.g = g;
        this.id = id;
    }

    public void Stop(){
        chave = false;
    }

    @Override
    public void run() {
        while (chave) {
            try {
                servicoTransporte.setDadosVeiculos();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (g.VerificaPilha()) {
                d = servicoTransporte.getVeiculo1();
                double distanciaPercorrida = g.calculaDistanciaTotalPercorrida();
                double velMedia = g.CalculaVelocidadeMedia();
                double velRecomendada = g.VelocidadeRecomendada();
                //double consumo = g.calcularConsumo();
                double temp = g.calculateTimeDifference();
                long travelTime = g.calculaTempoDeslocamento();
                g.setPassageiros((ArrayList<String>) d.getPassageiros());
                g.setcargas((ArrayList<String>) d.getCargas());

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            if (id == 1){
                try {
                    m.escreveGerenciaDadosV1(g);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(id==2){
                try {
                    m.escreveGerenciaDadosV2(g);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }



        }


    }
}


