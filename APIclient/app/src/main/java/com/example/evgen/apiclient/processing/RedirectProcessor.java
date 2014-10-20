package com.example.evgen.apiclient.processing;

/**
 * Created by evgen on 18.10.2014.
 */
public class RedirectProcessor <DataSourceResult> implements Processor<DataSourceResult, DataSourceResult> {
    @Override
    public DataSourceResult process(DataSourceResult dataSourceResult) throws Exception {
        return dataSourceResult;
    }
}
