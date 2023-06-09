package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trabalhoavancada.getcurrentlatitudeandlongitudeandroid.GpsTracker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private GerenciaDados g = new GerenciaDados();
    private MemComp m = new MemComp();
    private Localizacao loc = new Localizacao();

    private DataReader dataReader = new DataReader();
    private DataSaver dataSaver = new DataSaver();
    private TextView tvLatitude,tvLongitude,tvTempo,tvDistanciaPercorrida,tvTimeDifference, tvTravelTime;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = (TextView)findViewById(R.id.latitude);
        tvLongitude = (TextView)findViewById(R.id.longitude);
        tvTempo = (TextView)findViewById(R.id.tempo);
        tvDistanciaPercorrida = (TextView)findViewById(R.id.distanciaPercorrida);
        tvTimeDifference = (TextView)findViewById(R.id.timeDifference);
        tvTravelTime = (TextView)findViewById(R.id.tvTravelTime);





        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }



    public void getLocation(View view) throws InterruptedException {
        ThreadDataSaver threadDataSaver = new ThreadDataSaver(this);
        threadDataSaver.start();

    }

    public void Inicia(View v){

        loc.iniciaLocalizacao(this);
        dataReader.deleteData(this);
        g.SetTempoInico(System.currentTimeMillis());

    }

    public void MostraDados(View v){
        g.SalvaDados(dataReader.readData(this));
        Dados d = g.GetDado();
        double distanciaPercorrida = g.calcularDistancia();

        double latitude = d.getLatitude();
        double longitude = d.getLongitude();
        long tempo = d.getTime();
        Long temp = g.calculateTimeDifference();
        long travelTime = g.calculaTempoDeslocamento();
        tvLatitude.setText(String.valueOf(latitude));
        tvLongitude.setText(String.valueOf(longitude));
        tvTempo.setText(String.valueOf(tempo));
        tvDistanciaPercorrida.setText(String.valueOf(distanciaPercorrida));
        tvTimeDifference.setText(String.valueOf(temp));

    }


    public void AbreCalculaDados(View v){
        Intent i = new Intent(this, MostraDados.class);
        startActivity(i);

    }


}