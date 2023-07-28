package com.example.trabalhoavancada;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

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

    public interface PrivateKeyCallback {
        void onPrivateKeyLoaded(PrivateKey privateKey);
        void onError(Exception e);
    }



    private static void readPrivateKeyFromFirebase(PrivateKeyCallback callback) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        databaseRef.child("users").child("userId").child("privateKeyPem")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String privateKeyPem = dataSnapshot.getValue(String.class);

                            try {
                                String privateKeyData = privateKeyPem
                                        .replace("-----BEGIN PRIVATE KEY-----\n", "")
                                        .replace("-----END PRIVATE KEY-----\n", "");

                                byte[] keyBytes = Base64.decode(privateKeyData, Base64.DEFAULT);
                                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
                                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                                PrivateKey privateKey = keyFactory.generatePrivate(spec);

                                // Chame o callback com a chave privada obtida
                                callback.onPrivateKeyLoaded(privateKey);
                            } catch (Exception e) {
                                // Tratar a exceção, caso ocorra algum erro na decodificação da chave
                                callback.onError(e);
                            }
                        } else {
                            // A chave privada não foi encontrada no Firebase
                            // Lide com o caso em que a chave não existe
                            callback.onError(new Exception("Chave privada não encontrada no Firebase"));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Tratar erros de leitura do Firebase, se necessário
                        callback.onError(databaseError.toException());
                    }
                });
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
            PrivateKey privateKey = loadPrivateKey(context);
            byte[] encryptedBytes = readKeyFile(context, fileName);
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




