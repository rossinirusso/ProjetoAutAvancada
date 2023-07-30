package com.example.trabalhoavancada;

import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

public class Reconciliacao extends Thread {

    private GerenciaDados g = new GerenciaDados();
    private GerenciaDados g2 = new GerenciaDados();
    private MemComp m = new MemComp();

    private static Dados veiculo1;
    private static Dados veiculo2;
    private static Stack<Dados> dadosVeiculo2;

    private static double distanciaTotal;
    private static double tempoDesejado;

    private static double velMediaVeiculo2;
    private static double distanciaFaltaVeiculo2;

    private static double tempoEntrePontos;

    private static double intervalo;

    private double P1;
    private double P2;
    private double P3;
    private double P4;
    private double P0;


   /* public void setVeiculo1(){
        try {
            veiculo1 = m.adiquireVeiculo();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void setVeiculo2(){
        try {
            veiculo2 = m.adiquireVeiculo2();
            dadosVeiculo2.push(veiculo2);
            g.SalvaDados(dadosVeiculo2);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }*/

    public void getGerenciaDadosV1(){
        try {
            g = m.adiquireGerenciaDadosV1();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void getGerenciaDadosV2(){
        try {
            g2 = m.adiquireGerenciaDadosV2();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public double tempoDisponivel(){
        velMediaVeiculo2 = g2.getVelMedia();
        distanciaFaltaVeiculo2 = g2.calculaDistanciaFalta();
        double tempodisp = velMediaVeiculo2/distanciaFaltaVeiculo2;

        if(tempodisp==0){
            return g.getTempoDesejado()/60;
        }

        else {
            return tempodisp;
        }
    }

    public void setDistanciaTotal(){
        distanciaTotal = g.getDistanciaTotal();
    }

    public void setInvervalos(){
        intervalo = distanciaTotal/5;
        P0 = tempoDisponivel();

        if(g.calcularDistancia() > intervalo && g.calcularDistancia()<(2*intervalo)){
            if(P1==0) {
                P1 = g.getTravelTime();
            }
        }

        else if(g.calcularDistancia() > 2*intervalo && g.calcularDistancia()<(3*intervalo)){
            if(P2==0) {
                P2 = g.getTravelTime();
            }

        }

        else if(g.calcularDistancia() > 3*intervalo && g.calcularDistancia()<(4*intervalo)){
            if(P3==0) {
                P3 = g.getTravelTime();
            }

        }

        else if(g.calcularDistancia() > 4*intervalo && g.calcularDistancia()<(5*intervalo)){
            if(P4==0) {
                P4 = g.getTravelTime();
            }

        }


    }

    private double[] y;
    private double[] v;
    private double[][] A;

    private boolean chave;

    ArrayList<Dados> reconciliacao;

    public Reconciliacao (boolean chave){
        //this.reconciliacao = reconciliacao;
        this.chave = chave;
    }

    public double setRec(){
        y = new double[]{P0,P1,P2,P3,P4};
        v = new double[5];
        A = new double[1][5];
        int j = 0;
        for (int i = 0; i < 5; i++) {
            j=i;
            if(i == 0){
                v[i]= 1;
                A[0][i] = 1;
            }
            else {
                A[0][i] = -1;
                v[i]= 0.0001;
            }

        }
        Reconciliation rec = new Reconciliation(y,v,A);
        ArrayList<Double> recPronto = new ArrayList<>();
        recPronto = rec.printMatrix(rec.getReconciledFlow());
        Log.d("rec resultado:",recPronto.toString());

        //teste
        double distanciaFalta = distanciaTotal - g.calculaDistanciaTotalPercorrida();
        double TEMPO = (recPronto.get(j)/60)*10000;
        double velRecomendada = distanciaFalta/TEMPO;

        return  recPronto.get(j);
    }

    public void Stop(){
        chave = false;
    }

    @Override
    public void run() {
        while (chave) {
            getGerenciaDadosV1();
            getGerenciaDadosV2();
            setDistanciaTotal();
            if(g2.VerificaPilha() && g.VerificaPilha()){
            setInvervalos();
            setRec();
            }

        }
        }


}
