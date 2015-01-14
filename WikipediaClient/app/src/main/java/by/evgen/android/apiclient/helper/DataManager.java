package by.evgen.android.apiclient.helper;


import by.evgen.android.apiclient.auth.VkOAuthHelper;
import by.evgen.android.apiclient.os.AsyncTask;
import by.evgen.android.apiclient.processing.Processor;
import by.evgen.android.apiclient.source.DataSource;

/**
 * Created by evgen on 18.10.2014.
 */
public class DataManager<ProcessingResult, DataSourceResult, Params> {
    private static final String TAG = VkOAuthHelper.class.getSimpleName();

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
        executeInAsyncTask(callback, params, dataSource, processor);
   }

    private static <ProcessingResult, DataSourceResult, Params> void executeInAsyncTask(final Callback<ProcessingResult> callback, Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
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
