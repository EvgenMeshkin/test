package by.evgen.android.apiclient.helper;


import by.evgen.android.apiclient.os.AsyncTask;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.DataSource;

/**
 * Created by evgen on 18.10.2014.
 */
public class ManagerDownload {

    public static interface Callback<Result> {
        void onPreExecute();
        void onPostExecute(Result data);
        void onError(Exception e);
    }


    public static  void load(
            final Callback callback,
            final String params,
            final DataSource dataSource,
            final Processor processor) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        executeInAsyncTask(callback, params, dataSource, processor);
   }

    private static <ProcessingResult, DataSourceResult, Params> void executeInAsyncTask(final Callback<ProcessingResult> callback, Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
        new AsyncTask<Params, Void, ProcessingResult>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                callback.onPreExecute();
            }

            @Override
            protected void onPostExecute(ProcessingResult processingResult) {
                super.onPostExecute(processingResult);
                callback.onPostExecute(processingResult);
            }

            @Override
            protected ProcessingResult doInBackground(Params... params) throws Exception {
                DataSourceResult dataSourceResult = dataSource.getResult(params[0]);
                return processor.process(dataSourceResult);
            }

            @Override
            protected void onPostException(Exception e) {
                callback.onError(e);
            }

        }.execute(params);
    }

}
