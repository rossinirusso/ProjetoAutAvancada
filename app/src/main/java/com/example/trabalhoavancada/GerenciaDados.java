package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class GerenciaDados {

    private static Stack<Dados> dados = new Stack<>();

    private DataReader dataReader = new DataReader();

    private Reconciliacao reconciliacao;

    private static final int RAIO_TERRA = 6371;
    private static long tempoIncio;

    private static double distanciaTotal; //em km
    private static long tempoDesejado; //em hora
    private static int tipo;

    private double consumo;

    private double TEMPO;

    private ArrayList<Dados> dadosRec = new ArrayList<>();

    public synchronized boolean VerificaPilha(){
        if(dados.size()>1){
            return true;
        }
        else{
            return false;
        }
    }

    public synchronized void SetTempoInico(long tempoIncio){
        this.tempoIncio = tempoIncio;
    }

    public synchronized void SetDistanciaTotal(double distanciaTotal){
        this.distanciaTotal = distanciaTotal;
    }

    public synchronized void SetTempoDesejado(long tempoDesejado){
        this.tempoDesejado = (tempoDesejado*(60*60));
    }

    public synchronized void SetTipoVeiculo(int tipo){
        this.tipo = tipo;
    }

    public synchronized void SalvaDados(Stack<Dados> dados) {
        this.dados = dados;
    }

    public synchronized Dados GetDado(){
        return dados.peek();
    }


    public void reconcilia() {
        if (VerificaPilha()) {
            // Cria uma cópia da pilha original
            Stack<Dados> pilhaCopia = new Stack<>();
            pilhaCopia.addAll(dados);
                while (!pilhaCopia.isEmpty()) {
                    dadosRec.add(pilhaCopia.pop());
                }
                // Reverter a ordem para ficar igual à pilha original
                java.util.Collections.reverse(dadosRec);
                reconciliacao = new Reconciliacao(dadosRec,true);
                TEMPO = reconciliacao.setRec();

        }
    }

    public synchronized double calcularDistancia() {
        Dados ponto1 = dados.pop();
        Dados ponto2 = dados.peek();
        dados.push(ponto1);

        double lat1 = ponto1.getLatitude();
        double lon1 = ponto1.getLongitude();
        double lat2 = ponto2.getLatitude();
        double lon2 = ponto2.getLongitude();


        // Conversão para radianos
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // Diferenças das latitudes e longitudes
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;

        // Cálculo da distância utilizando a fórmula de Haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAIO_TERRA * c; // km
    }


    public synchronized double calculateTimeDifference() {
        double tempoDisp = (tempoDesejado - calculaTempoDeslocamento());
        return tempoDisp/(60); //em minutos;
    }


    public synchronized long calculaTempoDeslocamento(){
        Dados ponto = dados.peek();
        long tempoAtual = ponto.getTime();
        return (tempoAtual - tempoIncio)/(1000); //em seg
    }

    public synchronized double calculaDistanciaTotalPercorrida(){
        Dados ponto1 = dados.get(0);
        Dados ponto2 = dados.peek();

        double lat1 = ponto1.getLatitude();
        double lon1 = ponto1.getLongitude();
        double lat2 = ponto2.getLatitude();
        double lon2 = ponto2.getLongitude();


        // Conversão para radianos
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2);
        double radLon2 = Math.toRadians(lon2);

        // Diferenças das latitudes e longitudes
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;

        // Cálculo da distância utilizando a fórmula de Haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (RAIO_TERRA * c);

    }

    public synchronized double CalculaVelocidadeMedia(){
        double distancia = calculaDistanciaTotalPercorrida();
        double tempo = (double)calculaTempoDeslocamento()/(60*60);
        if(distancia==0){
            return 0.0;
        }
        else{
            double v = distancia / tempo;
            return v;
        }

        //return (double) calculaDistanciaTotalPercorrida()/((calculaTempoDeslocamento()/(60*60)));

    }

    public synchronized double VelocidadeRecomendada(){
        if(dados.size()>1) {
            double distanciaFalta = distanciaTotal - calculaDistanciaTotalPercorrida();
            double tempoDisp = (tempoDesejado - calculaTempoDeslocamento());
            tempoDisp = tempoDisp/(60*60);
            //TEMPO = TEMPO/(100);
            return distanciaFalta/tempoDisp;
        }

        else{
            //tempoIncio = tempoIncio / (60 * 60);
            return distanciaTotal / (tempoDesejado/(60*60));
        }

    }

    public double calcularConsumo() {
        double consumoAux;
        double velocidade = VelocidadeMediaEntre2Pontos();
        double distancia = calcularDistancia();

        if(tipo ==1) {
            if (velocidade < 80) {
                consumoAux = distancia / 17;
            } else if (velocidade >= 80 && velocidade <= 120) {
                consumoAux = distancia / 14;
            } else {
                consumoAux = distancia / 10;
            }
        }
        else{
            if (velocidade < 80) {
                consumoAux = distancia / 15;
            } else if (velocidade >= 80 && velocidade <= 120) {
                consumoAux = distancia / 10;
            } else {
                consumoAux = distancia / 7;
            }

        }
        consumo = consumo + consumoAux;

        return consumo;
    }

    public synchronized double VelocidadeMediaEntre2Pontos(){
        double distancia = calcularDistancia();
        double tempo = (double)(TempoEntre2Pontos())/(1000*60*60);
        if(distancia==0){
            return 0.0;
        }
        else{
            double v = distancia / tempo;
            return v;
        }
    }

    public synchronized double TempoEntre2Pontos(){
        Dados ponto1 = dados.pop();
        Dados ponto2 = dados.peek();
        dados.push(ponto1);

        long temp1 = ponto1.getTime();
        long temp2 = ponto2.getTime();

        return temp1 - temp2;

    }


}
