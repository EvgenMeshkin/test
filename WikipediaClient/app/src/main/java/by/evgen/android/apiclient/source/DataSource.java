package by.evgen.android.apiclient.source;

/**
 * Created by evgen on 18.10.2014.
 */
public interface DataSource<Result, Params> {

    Result getResult(Params params) throws Exception;
    //   Result setResult(Params params) throws Exception;
}
