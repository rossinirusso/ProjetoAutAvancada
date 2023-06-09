package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Semaphore;

public class MemComp extends AppCompatActivity {

    private static DadosGps dados = new DadosGps();
    private GerenciaDados g = new GerenciaDados();

    //private boolean semaforo = false; // false == travado / true == destravado
    private Semaphore semaforo = new Semaphore(10); // Número de threads do app

    // usar a variável própria de semaforo do Java semaforo ou mutex

    public DadosGps adiquire () throws InterruptedException {
        // travar a memória, adquire os dados, destravar a memória
        DadosGps pkg = new DadosGps();

        semaforo.acquire();// trava a memória
        pkg = dados;
        semaforo.release();// destrava a memória

        return pkg;
    }

    public void Escreve (DadosGps obj) throws InterruptedException {
        // travar a memória, escreve os dados, destravar a memória
        semaforo.acquire();// trava a memória
        dados = obj; //escreve os dados do a serem escritos no objeto principal
        semaforo.release();// destrava a memória
    }
}