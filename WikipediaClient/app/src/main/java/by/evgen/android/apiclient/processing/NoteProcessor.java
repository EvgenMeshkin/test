package by.evgen.android.apiclient.processing;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by evgen on 13.01.2015.
 */
public class NoteProcessor implements Processor<Long, InputStream> {

    @Override
    public Long process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        Long id = jsonObject.getLong("response");
        return id;
    }

}