package com.example.evgen.apiclient.helper;


import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.evgen.apiclient.MainActivity;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.os.AsyncTask;
import com.example.evgen.apiclient.os.MyLoader;
import com.example.evgen.apiclient.processing.Processor;
import com.example.evgen.apiclient.source.DataSource;

import static android.app.PendingIntent.getActivity;

/**
 * Created by evgen on 18.10.2014.
 */
public class DataManager implements LoaderManager.LoaderCallbacks<String> {

    public static final int IS_ASYNC_TASK = 2;
    public static final int LOADER_ID = 1;
    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        Loader<String> loader = null;
        if (i == LOADER_ID) {
            loader = new MyLoader(this, bundle);
      //      Log.d(LOG_TAG, "onCreateLoader: " + loader.hashCode());
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> stringLoader, String s) {
        callback.onDone(processingResult);
    }

    @Override
    public void onLoaderReset(Loader<String> stringLoader) {

    }

    public static interface Callback<Result> {
        void onDataLoadStart();
        void onDone(Result data);
        void onError(Exception e);
    }

    public static <ProcessingResult, DataSourceResult, Params> void
    loadData(
            final Callback<ProcessingResult> callback,
            final Params params,
            final DataSource<DataSourceResult, Params> dataSource,
            final Processor<ProcessingResult, DataSourceResult> processor) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }

        switch(IS_ASYNC_TASK) {
            case 1:
                executeInAsyncTask(callback, params, dataSource);
                break;
            case 2:
                executeInThread(callback, params, dataSource, processor);
                break;
            case 3:
                LoaderManager supportLoaderManager = getSupportLoaderManager();
                supportLoaderManager.restartLoader(LOADER_ID, new Bundle(), callback);
                break;

        }
     /*   if (IS_ASYNC_TASK) {
            executeInAsyncTask(callback, params, dataSource);
        } else {
            executeInThread(callback, params, dataSource, processor);
        }*/
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInAsyncTask(final Callback<ProcessingResult> callback, Params params, final DataSource<DataSourceResult, Params> dataSource) {
        new AsyncTask<Params, Void, ProcessingResult>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onDataLoadStart();
            }

            @Override
            protected void onPostExecute(ProcessingResult processingResult) {
                super.onPostExecute(processingResult);
                callback.onDone(processingResult);
            }

            @Override
            protected ProcessingResult doInBackground(Params... params) throws Exception {
                return (ProcessingResult) dataSource.getResult((Params) params);
            }

            @Override
            protected void onPostException(Exception e) {
                callback.onError(e);
            }

        }.execute(params);
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInThread(final Callback<ProcessingResult> callback, final Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
        final Handler handler = new Handler();
        callback.onDataLoadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final DataSourceResult result = dataSource.getResult(params);
                    final ProcessingResult processingResult = processor.process(result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(processingResult);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();
    }

 /*   public static <ProcessingResult, DataSourceResult, Params> void
    setData(
            final Callback<ProcessingResult> callback,
            final Params params,
            final DataSource<DataSourceResult, Params> dataSource,
            final Processor<ProcessingResult, DataSourceResult> processor) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        final Handler handler = new Handler();
        callback.onDataLoadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final DataSourceResult result = dataSource.setResult(params);
                    final ProcessingResult processingResult = processor.processwriter(result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(processingResult);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();
    }*/



}
