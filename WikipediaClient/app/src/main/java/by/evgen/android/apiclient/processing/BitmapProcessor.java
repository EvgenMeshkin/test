package by.evgen.android.apiclient.processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import by.evgen.android.apiclient.source.HttpDataSource;

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
