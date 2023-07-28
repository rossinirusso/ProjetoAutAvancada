package com.example.trabalhoavancada;

import android.util.Log;

import java.util.ArrayList;

public class Reconciliacao {

    private double[] y;
    private double[] v;
    private double[][] A;

    private boolean chave;

    ArrayList<Dados> reconciliacao;

    public Reconciliacao (ArrayList<Dados> reconciliacao,boolean chave){
        this.reconciliacao = reconciliacao;
        y = new double[reconciliacao.size()];
        v = new double[reconciliacao.size()];
        A = new double[1][reconciliacao.size()];
        this.chave = chave;
    }

    public double setRec(){
        int j = 0;
        for (int i = 0; i < reconciliacao.size(); i++) {
            j=i;
            y[i] = reconciliacao.get(i).getTime();
            v[i]= 0.0001;
            if(i == 0){
                A[0][i] = 1;
            }
            else {
                A[0][i] = -1;
            }

        }
        Reconciliation rec = new Reconciliation(y,v,A);
        ArrayList<Double> recPronto = new ArrayList<>();
        recPronto = rec.printMatrix(rec.getReconciledFlow());
        Log.d("rec resultado:",recPronto.toString());

        return  recPronto.get(j);
    }


}
