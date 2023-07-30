package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Semaphore;

public class MemComp extends AppCompatActivity {

    private static DadosGps dados = new DadosGps();
    private static Dados veiculo;
    private static Dados veiculo2;

    private GerenciaDados g = new GerenciaDados();
    private GerenciaDados g2 = new GerenciaDados();

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

    public Dados adiquireVeiculo () throws InterruptedException {
        // travar a memória, adquire os dados, destravar a memória
        Dados pkg = new Dados(0,0,0);

        semaforo.acquire();// trava a memória
        pkg = veiculo;
        semaforo.release();// destrava a memória

        return pkg;
    }

    public void escreveVeiculo (Dados obj) throws InterruptedException {
        // travar a memória, escreve os dados, destravar a memória
        semaforo.acquire();// trava a memória
        veiculo = obj; //escreve os dados do a serem escritos no objeto principal
        semaforo.release();// destrava a memória
    }

    public Dados adiquireVeiculo2 () throws InterruptedException {
        // travar a memória, adquire os dados, destravar a memória
        Dados pkg = new Dados(0,0,0);

        semaforo.acquire();// trava a memória
        pkg = veiculo2;
        semaforo.release();// destrava a memória

        return pkg;
    }

    public void escreveVeiculo2 (Dados obj) throws InterruptedException {
        // travar a memória, escreve os dados, destravar a memória
        semaforo.acquire();// trava a memória
        veiculo2 = obj; //escreve os dados do a serem escritos no objeto principal
        semaforo.release();// destrava a memória
    }


    public GerenciaDados adiquireGerenciaDadosV1 () throws InterruptedException {
        // travar a memória, adquire os dados, destravar a memória
        GerenciaDados pkg = new GerenciaDados();

        semaforo.acquire();// trava a memória
        pkg = g;
        semaforo.release();// destrava a memória

        return pkg;
    }

    public void escreveGerenciaDadosV1 (GerenciaDados obj) throws InterruptedException {
        // travar a memória, escreve os dados, destravar a memória
        semaforo.acquire();// trava a memória
        g = obj; //escreve os dados do a serem escritos no objeto principal
        semaforo.release();// destrava a memória
    }

    public GerenciaDados adiquireGerenciaDadosV2 () throws InterruptedException {
        // travar a memória, adquire os dados, destravar a memória
        GerenciaDados pkg = new GerenciaDados();

        semaforo.acquire();// trava a memória
        pkg = g2;
        semaforo.release();// destrava a memória

        return pkg;
    }

    public void escreveGerenciaDadosV2 (GerenciaDados obj) throws InterruptedException {
        // travar a memória, escreve os dados, destravar a memória
        semaforo.acquire();// trava a memória
        g2 = obj; //escreve os dados do a serem escritos no objeto principal
        semaforo.release();// destrava a memória
    }
}
