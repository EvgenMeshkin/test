package com.example.evgen.apiclient.processing;

import android.util.Log;

import com.example.evgen.apiclient.source.FileDataSource;
import com.example.evgen.apiclient.source.HttpDataSource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by evgen on 18.10.2014.
 */
public class FileProcessor implements Processor<String, File> {

    final String LOG_TAG = "myLogs";
    @Override
    public String process(File br) throws Exception {
        InputStreamReader inputStreamReader = null;
        BufferedReader in = null;
        try {
            String str = "";
            StringBuilder builder1 = new StringBuilder();
            in = new BufferedReader(new FileReader(br));
                // читаем содержимое
                while ((str = in.readLine()) != null) {
                    builder1.append(str);
                    Log.d(LOG_TAG, str);
                }
            return builder1.toString();
        } finally {
            FileDataSource.close(in);
            FileDataSource.close(inputStreamReader);

        }
    }

  /*  @Override
    public String processwriter(File file) throws Exception {
        InputStreamReader inputStreamReader = null;
        BufferedWriter bw = null;
        try {
            String str = "";
            bw = new BufferedWriter(new FileWriter(file));
            // пишем данные
            bw.write("Данные файла на SD");
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан на SD: " + file.getAbsolutePath());
            // читаем содержимое

            return null;
        } finally {
            FileDataSource.close(bw);
            FileDataSource.close(inputStreamReader);

        }
    }*/


}


