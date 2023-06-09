package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MostraDados extends AppCompatActivity {

    private GerenciaDados g = new GerenciaDados();

    private TextView tvTravelTime , tvVelMedia, tvTravelDistance, tvVelRecomendada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_dados);
        tvTravelTime = (TextView)findViewById(R.id.tvTravelTime);
        tvVelMedia =(TextView)findViewById(R.id.velMedia);
        tvTravelDistance = (TextView)findViewById(R.id.travelDistance);
        tvTravelDistance = (TextView)findViewById(R.id.velRecomendada);
    }

    public void VoltaTelaPrincipal(View v){


        long tempoViagem = g.calculaTempoDeslocamento();
        double velMedia = g.CalculaVelocidadeMedia();
        double distanciaPercorrida = g.calculaDistanciaTotalPercorrida();
        double vRecomendada = g.VelocidadeRecomendada();

        tvTravelTime.setText(String.valueOf(tempoViagem));
        tvVelMedia.setText(String.valueOf(velMedia));
        tvTravelDistance.setText(String.valueOf(distanciaPercorrida));
        tvTravelDistance.setText(String.valueOf(vRecomendada));

    }

    public void Mostra(View v){

    }

    public void Teste(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}