package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class GerenciaDados {

    private static Stack<Dados> dados = new Stack<>();

    private DataReader dataReader = new DataReader();

    private static final int RAIO_TERRA = 6371;

    public synchronized void SalvaDados(Stack<Dados> dados) {
        this.dados = dados;
    }

    public synchronized Dados GetDado(){
        return dados.peek();
    }


    public double calcularDistancia() {
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

        return RAIO_TERRA * c;
    }


    public long calculateTimeDifference() {
        Dados ponto1 = dados.pop();
        Dados ponto2 = dados.peek();
        dados.push(ponto1);

        long startTime = ponto1.getTime();
         long endTime = ponto2.getTime();
        return Math.abs(endTime - startTime);
    }




}
