package com.example.trabalhoavancada;

import java.util.Stack;

public class IntervalosTempo {

    private GerenciaDados g = new GerenciaDados();
    private MemComp m = new MemComp();

    private static Dados veiculo1;
    private static Dados veiculo2;
    private static Stack<Dados> dadosVeiculo2;

    private static double distanciaTotal;
    private static double tempoDesejado;

    private static double velMediaVeiculo2;
    private static double distanciaFaltaVeiculo2;

    private static double tempoEntrePontos;

    public void setVeiculo1(){
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

    }








}
