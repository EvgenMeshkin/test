package com.example.evgen.apiclient.processing;

/**
 * Created by evgen on 18.10.2014.
 */
public interface Processor<ProcessingResult, Source> {

    ProcessingResult process(Source source) throws Exception;
    //ProcessingResult processwriter(Source source) throws Exception;


}
