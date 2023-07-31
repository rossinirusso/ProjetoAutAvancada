package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trabalhoavancada.getcurrentlatitudeandlongitudeandroid.GpsTracker;

public class MainActivity extends AppCompatActivity {

    private GpsTracker gpsTracker;
    private GerenciaDados g = new GerenciaDados();
    private ThreadMostraDados threadMostraDados;
    private ThreadGerenciaDados threadGerenciaDados;
    private ThreadGerenciaDados threadGerenciaDadosV2;
    private ServicoTransporte servicoTransporte;

    private ThreadReconcilia threadReconcilia;

    private JsonSaver jsonSaver;
    private MemComp m = new MemComp();
    private Localizacao loc = new Localizacao();

    private DataReader dataReader = new DataReader();
    private DataSaver dataSaver = new DataSaver();
    private TextView tvLatitude,tvLongitude,tvTempo,tvDistanciaPercorrida,tvTimeDifference, tvTravelTime, tvVelMedia, tvTravelDistance, tvVelRecomendada,tvConsumo, tvMotoristaV1,tvMotoristaV2, tvCargaV1, tvCargaV2,tvPassageirosV1,tvPassageirosV2;
    private EditText edDistancia, edConsumo, edTempo, edmotorista, edPassageiros, edCargas, edPontoCross, edIdServico;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvLatitude = (TextView)findViewById(R.id.latitude);
        //tvLongitude = (TextView)findViewById(R.id.longitude);
        //tvTempo = (TextView)findViewById(R.id.);
        tvTimeDifference = (TextView)findViewById(R.id.timeDifference);
        tvTravelTime = (TextView)findViewById(R.id.TextViewTempoAndou);
        tvVelMedia =(TextView)findViewById(R.id.textViewVelMedia);
        tvTravelDistance = (TextView)findViewById(R.id.distanciaPercorrida);
        tvVelRecomendada = (TextView)findViewById(R.id.textViewVelRecomendada);
        tvConsumo = (TextView)findViewById(R.id.textViewConsumo);
        edDistancia = (EditText)findViewById(R.id.editTextDistance);
        //edConsumo = (EditText)findViewById(R.id.editConsumo);
        edTempo = (EditText)findViewById(R.id.editTextTravelTime);
        edmotorista = (EditText)findViewById(R.id.editTextMotorista);
        edPassageiros = (EditText)findViewById(R.id.editTextPassageiros);
        edCargas = (EditText)findViewById(R.id.editTextCargas);
        edPontoCross = (EditText)findViewById(R.id.editTextDistanceCross);
        tvMotoristaV1 = (TextView)findViewById(R.id.textViewMotoristaVeiculo1);
        tvMotoristaV2 = (TextView)findViewById(R.id.textViewMotoristaVeiculo2);
        tvCargaV1 = (TextView)findViewById(R.id.textViewCargasVeiculo1);
        tvCargaV2 = (TextView)findViewById(R.id.textViewCargasVeiculo2);
        tvPassageirosV1 = (TextView)findViewById(R.id.textViewPassageirosVeiculo1);
        tvPassageirosV2 = (TextView)findViewById(R.id.textViewPassageirosVeiculo2);
        edIdServico = (EditText) findViewById(R.id.editIdTransporte);







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
        //jsonSaver = new JsonSaver(this,true);
        //jsonSaver.start();

    }

    public void Inicia(View v){

        servicoTransporte = new ServicoTransporte(this);
        loc.iniciaLocalizacao(this);
        dataReader.deleteData(this);
        servicoTransporte.addMotorista(edmotorista.getText().toString());
        servicoTransporte.addPassageiros(edPassageiros.getText().toString());
        servicoTransporte.addCargas(edCargas.getText().toString());
        servicoTransporte.setId(edIdServico.getText().toString());
        g.SetTempoInico(System.currentTimeMillis());
        //g.SetTipoVeiculo(getIntFromEditText(edConsumo));
        g.SetDistanciaTotal(getDecimalFromEditText(edDistancia));
        g.SetTempoDesejado(getLongFromEditText(edTempo));
        g.setDistanciaCross(getDecimalFromEditText(edPontoCross));

    }

    public void MostraDados(View v){
        threadGerenciaDados = new ThreadGerenciaDados(this,true,servicoTransporte,servicoTransporte.getGerenciaDadosVeiculo1(),1);
        threadGerenciaDados.start();
        threadGerenciaDadosV2 = new ThreadGerenciaDados(this,true,servicoTransporte,servicoTransporte.getGerenciaDadosVeiculo2(),2);
        threadGerenciaDadosV2.start();
        threadMostraDados = new ThreadMostraDados(this,true,tvTimeDifference,tvTravelTime,tvVelMedia,tvTravelDistance,tvVelRecomendada,tvConsumo,servicoTransporte,tvMotoristaV1,tvMotoristaV2, tvCargaV1, tvCargaV2,tvPassageirosV1,tvPassageirosV2);
        threadMostraDados.start();
        threadReconcilia = new ThreadReconcilia(true);
        threadReconcilia.start();


    }

    public void Stop(View v){
        threadMostraDados.Stop();
        loc.terminaThread();
        threadGerenciaDados.Stop();
        jsonSaver.Stop();
        threadGerenciaDadosV2.Stop();
        threadReconcilia.Stop();

    }



    public double getDecimalFromEditText(EditText editText) {
        String inputValue = editText.getText().toString().trim();

        if (inputValue.isEmpty()) {
            // Se o EditText estiver vazio, você pode definir um valor padrão ou lançar uma exceção, se necessário.
            return 0.0;
        }

        return Double.parseDouble(inputValue);
    }

    public long getLongFromEditText(EditText editText) {
        String inputValue = editText.getText().toString().trim();

        if (inputValue.isEmpty()) {
            // Se o EditText estiver vazio, você pode definir um valor padrão ou lançar uma exceção, se necessário.
            return 0L;
        }

        try {
            return Long.parseLong(inputValue);
        } catch (NumberFormatException e) {
            // Lidar com o erro de formato inválido, se necessário.
            e.printStackTrace();
        }

        return 0L;
    }

    public int getIntFromEditText(EditText editText) {
        String inputValue = editText.getText().toString().trim();

        if (inputValue.isEmpty()) {
            // Se o EditText estiver vazio, você pode definir um valor padrão ou lançar uma exceção, se necessário.
            return 0;
        }

        try {
            return (int) Double.parseDouble(inputValue);
        } catch (NumberFormatException e) {
            // Lidar com o erro de formato inválido, se necessário.
            e.printStackTrace();
        }

        return 0;
    }





}