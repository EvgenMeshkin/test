package com.example.evgen.apiclient.helper;


import android.content.Context;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.evgen.apiclient.MainActivity;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.os.AsyncTask;
//import com.example.evgen.apiclient.os.MyLoader;
import com.example.evgen.apiclient.processing.NoteArrayProcessor;
import com.example.evgen.apiclient.processing.Processor;
import com.example.evgen.apiclient.source.DataSource;
import com.example.evgen.apiclient.source.HttpDataSource;

import static android.app.PendingIntent.getActivity;

/**
 * Created by evgen on 18.10.2014.
 */
public class DataManager<ProcessingResult, DataSourceResult, Params> extends AsyncTaskLoader<Integer> {

    public static final int IS_ASYNC_TASK = 4;
    public static final int LOADER_ID = 1;
    private Callback<MainActivity> callback;
    private Params params;
    private DataSource<DataSourceResult, Params> dataSource;
    private Processor<ProcessingResult, DataSourceResult> processor;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
         forceLoad();
        Log.d("datamanager", "start " );
    }

    public static interface Callback<Result> {
        void onDataLoadStart();
        void onDone(Result data);
        void onError(Exception e);
    }

    public DataManager(Context context,  Params url, DataSource dataSource, Processor processor) {
        super(context);
      //  this.callback = call;
        this.params = url;
        this.dataSource = dataSource;
        this.processor = processor;
        Log.d("datamanager", "запуск " );

    }


    @Override
    public Integer loadInBackground() {
        Log.d("Datamanager", "переход " );
     //  final Handler handler = new Handler();
        callback.onDataLoadStart();

                try {
                    final DataSourceResult result = dataSource.getResult(params);
                    final ProcessingResult processingResult = processor.process(result);
   //                 handler.post(new Runnable() {
   //                     @Override
    //                    public void run() {
                            Log.d("Datamanager", "переход2 " );
                            callback.onDone((MainActivity) processingResult);
   //                     }
  //                  });
               } catch (final Exception e) {
    //                handler.post(new Runnable() {
     //                   @Override
    //                    public void run() {
                            callback.onError(e);
                        }
     //               });
     //           }
   //         }
   //     }).start();
        return Log.d("Datamanager", "переход3 " );
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
               /* LoaderManager supportLoaderManager = getActivity(MainActivity).getSupportLoaderManager();
                supportLoaderManager.restartLoader(LOADER_ID, new Bundle(), callback);*/
                break;

        }

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


}
