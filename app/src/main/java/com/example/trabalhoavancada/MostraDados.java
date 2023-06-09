package com.example.trabalhoavancada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MostraDados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_dados);
    }

    public void VoltaTelaPrincipal(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }
}