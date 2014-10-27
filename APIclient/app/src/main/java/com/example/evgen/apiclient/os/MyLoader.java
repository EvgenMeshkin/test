package com.example.evgen.apiclient.os;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by evgen on 27.10.2014.
 */
public class MyLoader<Params, Progress, Result> extends AsyncTaskLoader<String> {

    public MyLoader (Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        final DataSourceResult result = dataSource.getResult(params);
        final ProcessingResult processingResult = processor.process(result);
        Log.e("TEST", "Loading started");
        return null;
    }


}
