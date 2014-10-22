package com.example.evgen.apiclient.source;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.evgen.apiclient.CoreApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by evgen on 18.10.2014.
 */
public class FileDataSource implements DataSource<File, String> {

    public static final String KEY = "FileDataSource";
    final String LOG_TAG = "myLogs";
    final String FILENAME = "file";
    final String DIR_SD = "MyFiles";
    final String FILENAME_SD = "fileSD";
    public static FileDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public File getResult(String s) throws Exception {
        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(s);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);

            // открываем поток для чтения

        return sdFile;
    }

    /*@Override
    public File setResult(String s) throws Exception {

        File sdPath = Environment.getExternalStorageDirectory();
        sdPath = new File(s);
        // создаем каталог
        sdPath.mkdirs();
        File sdFile = new File(s, FILENAME_SD);
        return sdFile;
    }*/

    public static void close(Closeable in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

