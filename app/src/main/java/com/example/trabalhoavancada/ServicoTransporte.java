package com.example.trabalhoavancada;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ServicoTransporte {

    private Dados veiculo1;
    private Dados veiculo2;

    private Stack<Dados> dadosVeiculo2 = new Stack<>();
    private Context context;
    private GerenciaDados g = new GerenciaDados();

    private GerenciaDados g2 = new GerenciaDados();
    private DataReader dataReader = new DataReader();

    private JsonReader jsonReader = new JsonReader();

    private MemComp m = new MemComp();

    private Reconciliacao reconciliacao;

    private static long tempoInicio;
    private static List<String> motoristas = new ArrayList<>();
    private static List<String> cargas = new ArrayList<>();
    private static List<String> passageiros = new ArrayList<>();
    private static String id;

    public ServicoTransporte(Context context){
        this.context = context;
    }

    public void setDadosVeiculos() throws InterruptedException {
        g.SalvaDados(dataReader.readData(context));
        if(g.VerificaPilha()) {
            veiculo1 = g.GetDado();
            m.escreveVeiculo(veiculo1);

            if(!motoristas.isEmpty() && veiculo1 !=null){
            veiculo1.setMotorista(motoristas.get(0));

            veiculo1.setCargas(cargas);
            veiculo1.setPassageiros(passageiros);
            }

            veiculo2 = jsonReader.decryptFileToObject(context,"dadosCriptografados.json",Dados.class);
            if(veiculo2 != null) {
                dadosVeiculo2.push(veiculo2);
                g2.SalvaDados(dadosVeiculo2);
                try {
                    m.escreveVeiculo2(veiculo2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public ArrayList<String> splitNames(String namesString) {
        // Verifica se a string não é nula ou vazia
        if (namesString == null || namesString.isEmpty()) {
            return new ArrayList<>(); // Retorna um ArrayList vazio se a string for nula ou vazia
        }

        // Remove espaços em branco adicionais antes ou depois dos nomes
        namesString = namesString.trim();

        // Divide os nomes com base na vírgula e converte o array em ArrayList
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList(namesString.split(",")));

        // Remove espaços em branco adicionais dos nomes individuais
        for (int i = 0; i < namesList.size(); i++) {
            namesList.set(i, namesList.get(i).trim());
        }

        return namesList;
    }

    public Dados getVeiculo1(){
        return veiculo1;
    }

    public Dados getVeiculo2(){
        return veiculo2;
    }

    public GerenciaDados getGerenciaDadosVeiculo1(){
        return g;
    }

    public GerenciaDados getGerenciaDadosVeiculo2(){
        return g2;
    }

    public String getDadosVeiculo1(){
        Gson v = new Gson();
        return v.toJson(veiculo1);
    }

    public void addMotorista(String motorista){
        motoristas.add(motorista);
    }

    public void addPassageiros(String passageiros){
        this.passageiros = splitNames(passageiros);
    }

    public void addCargas(String cargas){
        this.cargas = splitNames(cargas);
    }


}
