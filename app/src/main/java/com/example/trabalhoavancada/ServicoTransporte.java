package com.example.trabalhoavancada;

import android.content.Context;

import com.google.gson.Gson;

import java.util.List;

public class ServicoTransporte {

    private Dados veiculo1;
    private Dados veiculo2;
    private Context context;
    private GerenciaDados g = new GerenciaDados();
    private DataReader dataReader = new DataReader();

    private JsonReader jsonReader = new JsonReader();

    private MemComp m = new MemComp();

    private Reconciliacao reconciliacao;

    private static long tempoInicio;
    private static List<String> motoristas;
    private static List<String> cargas;
    private static List<String> passageiros;
    private static String id;

    public ServicoTransporte(Context context){
        this.context = context;
    }

    public void setDadosVeiculo1() throws InterruptedException {
        g.SalvaDados(dataReader.readData(context));
        if(g.VerificaPilha()) {
            veiculo1 = g.GetDado();
            m.escreveVeiculo(veiculo1);

            //veiculo1.setMotorista(motoristas.get(0));
            //veiculo1.setCargas(cargas);
            //veiculo1.setPassageiros(passageiros);
        }

    }

    public void setDadosVeiculo2(){
        veiculo2 = jsonReader.decryptFileToObject(context,"dadosCriptografados.json",Dados.class);


    }

    public Dados getVeiculo1(){
        return veiculo1;
    }

    public Dados getVeiculo2(){
        return veiculo2;
    }

    public GerenciaDados getGerenciaDados(){
        return g;
    }

    public String getDadosVeiculo1(){
        Gson v = new Gson();
        return v.toJson(veiculo1);
    }


}
