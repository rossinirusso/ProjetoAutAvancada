package com.example.trabalhoavancada;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DataReader {
    private static final String TAG = "DataReader";
    private static final String FILE_NAME = "data.txt";

    public static Stack<Dados> readData(Context context) {
        Stack<Dados> dataList = new Stack<>();

        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    double latitude = Double.parseDouble(data[0]);
                    double longitude = Double.parseDouble(data[1]);
                    long tempo = Long.parseLong(data[2]);

                    Dados dataObject = new Dados(latitude, longitude, tempo);
                    dataList.push(dataObject);
                }
            }

            bufferedReader.close();
            Log.d(TAG, "Data read successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Error reading data: " + e.getMessage());
        }

        return dataList;
    }

    public static void deleteData(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.close();
            Log.d(TAG, "Data deleted successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Error deleting data: " + e.getMessage());
        }
    }
}
