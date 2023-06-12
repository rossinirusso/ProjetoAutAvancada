package com.example.trabalhoavancada;

import android.content.Context;

import android.content.Context;

public class ThreadDataSaver extends Thread {
    private MemComp m = new MemComp();
    DataSaver dataSaver = new DataSaver();
    private Context context;

    public ThreadDataSaver(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DadosGps dado = m.adiquire();
                dataSaver.saveData(context, dado);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

