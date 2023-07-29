package com.example.trabalhoavancada;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

import javax.crypto.Cipher;

public class JsonReader {

    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String CHARSET_NAME = StandardCharsets.UTF_8.name();

    private static final String PRIVATE_KEY_FILE = "private_key.pem";

    private static PrivateKey loadPrivateKey(Context context) {
        try {
            byte[] keyBytes = readKeyFile(context, PRIVATE_KEY_FILE);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading private key", e);
        }
    }

    private static PrivateKey lerPrivateKeyDoFirebase() {
        // Obtém uma referência do Realtime Database para a chave privada PEM
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child("userId")
                .child("privateKeyPem");

        final String[] privateKeyPem = {null}; // Usando um array para armazenar o resultado final

        // Adiciona um listener para recuperar a chave privada PEM
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica se o snapshot tem algum valor
                if (dataSnapshot.exists()) {
                    // Obtém a string PEM salva do snapshot
                    privateKeyPem[0] = dataSnapshot.getValue(String.class);
                } else {
                    // A chave privada não foi encontrada no nó especificado
                    // Lidar com esse cenário, caso necessário
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Ocorreu um erro ao tentar recuperar a chave privada PEM
                // Lidar com o erro, caso necessário
            }
        });

        // Aguarda a finalização da leitura antes de retornar a chave privada
        // Isso é necessário porque o Firebase executa a leitura de forma assíncrona
        while (privateKeyPem[0] == null) {
            try {
                Thread.sleep(100); // Pequena pausa para evitar consumo excessivo de recursos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Decodifica a string PEM para obter o array de bytes da chave privada
        byte[] privateKeyBytes = Base64.decode(privateKeyPem[0].getBytes(),Base64.DEFAULT);

        try {
            // Carrega a chave privada usando a classe KeyFactory e a especificação PKCS8
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            // Ocorreu um erro ao tentar carregar a chave privada
            // Lidar com o erro, caso necessário
            return null;
        }
    }

    private static byte[] lerBytesCriptografadosDoFirebase() {
        // Obtém uma referência do Realtime Database para o nó "DadosVeiculo"
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("DadosVeiculo");

        final byte[][] encryptedBytes = {null}; // Usando um array para armazenar o resultado final

        // Adiciona um listener para recuperar os dados
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verifica se o snapshot tem algum valor
                if (dataSnapshot.exists()) {
                    // Obtém a string salva do snapshot
                    String encryptedBase64 = dataSnapshot.getValue(String.class);

                    // Decodifica a string Base64 para obter o array de bytes criptografados
                    encryptedBytes[0] = Base64.decode(encryptedBase64,Base64.DEFAULT);
                } else {
                    // O nó "DadosVeiculo" não possui nenhum valor
                    // Lidar com esse cenário, caso necessário
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Ocorreu um erro ao tentar recuperar os dados
                // Lidar com o erro, caso necessário
            }
        });

        // Aguarda a finalização da leitura antes de retornar os bytes criptografados
        // Isso é necessário porque o Firebase executa a leitura de forma assíncrona
        while (encryptedBytes[0] == null) {
            try {
                Thread.sleep(100); // Pequena pausa para evitar consumo excessivo de recursos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return encryptedBytes[0];
    }




    private static byte[] decrypt(byte[] data, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data with RSA", e);
        }
    }

    private static byte[] readKeyFile(Context context, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(fileName)));
        StringBuilder keyStr = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            keyStr.append(line);
        }
        br.close();
        return Base64.decode(keyStr.toString(), Base64.DEFAULT);
    }

    public static <T> T decryptFileToObject(Context context, String fileName, Class<T> clazz) {
        try {
            PrivateKey privateKey = lerPrivateKeyDoFirebase();
            byte[] encryptedBytes = lerBytesCriptografadosDoFirebase();
            byte[] decryptedBytes = decrypt(encryptedBytes, privateKey);
            String json = new String(decryptedBytes, CHARSET_NAME);
            return jsonToObject(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data and creating object", e);
        }
    }

    private static <T> T jsonToObject(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder().create();
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }
}




