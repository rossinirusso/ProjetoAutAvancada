package com.example.trabalhoavancada;

import android.content.Context;
import android.util.Base64;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class JsonSaver extends Thread {

    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String CHARSET_NAME = StandardCharsets.UTF_8.name();

    private static final String PUBLIC_KEY_FILE = "public_key.pem";
    private static final String PRIVATE_KEY_FILE = "private_key.pem";
    private static int cont;

    private boolean chave;
    private Context context;

    private DataReader dataReader = new DataReader();

    private GerenciaDados g = new GerenciaDados();

    private MemComp m = new MemComp();

    private Dados veiculo;


    public JsonSaver(Context context, boolean chave){
        this.context = context;
        this.chave = chave;

    }



    /*public static void generateAndSaveKeyPair(Context context) {
        // Check if keys already exist, if not generate new ones
        if (!hasSavedKeys(context)) {
            KeyPair keyPair = generateRSAKeyPair();

            savePublicKeyToFile(context, keyPair.getPublic());
            savePrivateKeyToFile(context, keyPair.getPrivate());
        }
    }*/

    public static void generateAndSaveKeyPair(Context context) {
        if (cont ==0 && hasSavedKeys(context)){
            context.deleteFile(PUBLIC_KEY_FILE); // Exclui o arquivo se já existir
            context.deleteFile(PRIVATE_KEY_FILE);
        }
        // Check if keys already exist, if not generate new ones
        if (!hasSavedKeys(context)) {
            KeyPair keyPair = generateRSAKeyPair();

            savePublicKeyToFile(context, keyPair.getPublic());
            savePrivateKeyToFile(context, keyPair.getPrivate());
            savePrivateKeyToFirebase(keyPair.getPrivate());
            cont++;
        }

    }

    private static boolean hasSavedKeys(Context context) {
        return context.getFileStreamPath(PUBLIC_KEY_FILE).exists()
                && context.getFileStreamPath(PRIVATE_KEY_FILE).exists();
    }

    private static KeyPair generateRSAKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA key pair", e);
        }
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private static void savePublicKeyToFile(Context context, PublicKey publicKey) {
        try {
            byte[] publicKeyBytes = publicKey.getEncoded();
            FileOutputStream fos = context.openFileOutput(PUBLIC_KEY_FILE, Context.MODE_PRIVATE);
            fos.write(Base64.encode(publicKeyBytes, Base64.DEFAULT));
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException("Error saving public key to file", e);
        }
    }

    private static void savePrivateKeyToFile(Context context, PrivateKey privateKey) {
        try {
            byte[] privateKeyBytes = privateKey.getEncoded();
            FileOutputStream fos = context.openFileOutput(PRIVATE_KEY_FILE, Context.MODE_PRIVATE);
            fos.write(Base64.encode(privateKeyBytes, Base64.DEFAULT));
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException("Error saving private key to file", e);
        }
    }


    private static void savePrivateKeyToFirebase(PrivateKey privateKey) {
        try {
            // Converter a chave privada em um formato de String PEM
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
            String privateKeyPem = Base64.encodeToString(spec.getEncoded(), Base64.DEFAULT);

            // Obter uma referência do Realtime Database
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

            // Salvar o arquivo .pem no Realtime Database
            databaseRef.child("users").child("userId").child("privateKeyPem").setValue(privateKeyPem)
                    .addOnSuccessListener(aVoid -> {
                        // Sucesso ao salvar o arquivo .pem no Firebase
                        // Aqui você pode adicionar alguma lógica caso necessário
                    })
                    .addOnFailureListener(e -> {
                        // Falha ao salvar o arquivo .pem no Firebase
                        // Aqui você pode lidar com a exceção caso necessário
                    });
        } catch (Exception e) {
            throw new RuntimeException("Error saving private key to Firebase", e);
        }
    }


    public static String encryptAndSaveObject(Context context, Object object, String objectFileName) {
        String json = objectToJson(object);


        String encryptedJson = encryptWithRSA(context, json);

        if (context.getFileStreamPath(objectFileName).exists()) {
            context.deleteFile(objectFileName); // Exclui o arquivo se já existir
        }

        saveToFile(context, encryptedJson, objectFileName);

        // Método para salvar a string no Realtime Database
        // Obtém uma referência do Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Cria um novo nó no Realtime Database e define a string a ser salva
        databaseRef.child("DadosVeiculo").setValue(encryptedJson)
                .addOnSuccessListener(aVoid -> {
                    // Sucesso ao salvar a string
                    // Aqui você pode adicionar alguma lógica caso necessário
                })
                .addOnFailureListener(e -> {
                    // Falha ao salvar a string
                    // Aqui você pode lidar com a exceção caso necessário
                });

        return encryptedJson;
    }

    private static void deleteExistingFile(Context context, String fileName) {
        context.deleteFile(fileName);
    }

    private static String objectToJson(Object object) {
        Gson gson = new GsonBuilder().create();
        String teste = gson.toJson(object);
        return teste;
    }

    private static String encryptWithRSA(Context context, String data) {
        try {
            PublicKey publicKey = loadPublicKey(context);
            byte[] encryptedBytes = encrypt(data.getBytes(CHARSET_NAME), publicKey);
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data with RSA", e);
        }
    }

    private static PublicKey loadPublicKey(Context context) {
        try {
            byte[] keyBytes = readKeyFile(context, PUBLIC_KEY_FILE);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading public key", e);
        }
    }

    private static byte[] encrypt(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data with RSA", e);
        }
    }

    private static void saveToFile(Context context, String data, String fileName) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes(CHARSET_NAME));
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException("Error saving data to file", e);
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

    public void Stop(){
        chave = false;
    }

    @Override
    public void run(){
        while (chave){
            generateAndSaveKeyPair(context);
            try {
                encryptAndSaveObject(context,m.adiquireVeiculo(),"dadosCriptografados.json");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }



}
