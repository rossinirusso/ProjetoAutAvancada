package com.example.trabalhoavancada;

import android.os.Looper;

public class ThreadReconcilia extends Thread {

    private Reconciliacao rec = new Reconciliacao();
    private boolean chave;

    public  ThreadReconcilia(boolean chave){
        this.chave = chave;
    }

    public void Stop(){
        chave = false;
    }



    @Override
    public void run() {
        while (chave) {
            rec.getGerenciaDadosV1();
            rec.getGerenciaDadosV2();
            rec.setDistanciaTotal();
            if(rec.verificaPilhas()){
                rec.setInvervalos();
                rec.setRec();
            }

        }
    }

}
