package com.example.evgen.apiclient.processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.evgen.apiclient.source.HttpDataSource;

import java.io.InputStream;

/**
 * Created by User on 19.11.2014.
 */
public class BitmapProcessor implements Processor<Bitmap, InputStream> {

    @Override
    public Bitmap process(InputStream inputStream) throws Exception {
        try {
            return BitmapFactory.decodeStream(inputStream);
        } finally {
            HttpDataSource.close(inputStream);
        }
    }

}
