package com.example.evgen.apiclient.processing;

import com.example.evgen.apiclient.source.HttpDataSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by evgen on 18.10.2014.
 */
public class FileProcessor implements Processor<String, FileInputStream> {
    @Override
    public String process(FileInputStream finputStream) throws Exception {
        InputStreamReader inputStreamReader = null;
        BufferedReader in = null;
        try {
            inputStreamReader = new InputStreamReader(finputStream);
            in = new BufferedReader(inputStreamReader);
            String str;
            StringBuilder builder = new StringBuilder();
            while ((str = in.readLine()) != null) {
                builder.append(str);
            }
            return builder.toString();
        } finally {
            HttpDataSource.close(in);
            HttpDataSource.close(inputStreamReader);
            HttpDataSource.close(finputStream);
        }
    }
}

