package com.example.trabalhoavancada;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class DataSaver {
    private static final String TAG = "DataSaver";

    public static void saveData(Context context, DadosGps dado) {
        String fileName = "data.txt";
        String data = dado.getX() + "," + dado.getY() + "," + dado.getTime() + "\n";

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            outputStreamWriter.append(data);
            outputStreamWriter.close();
            Log.d(TAG, "Data saved successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Error saving data: " + e.getMessage());
        }
    }
}

