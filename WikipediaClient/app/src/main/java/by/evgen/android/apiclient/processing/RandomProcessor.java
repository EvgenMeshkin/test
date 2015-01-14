package by.evgen.android.apiclient.processing;

import by.evgen.android.apiclient.bo.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 05.01.2015.
 */
public class RandomProcessor implements Processor<List<Category>,InputStream>{

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject query = jsonObject.getJSONObject("query");
        JSONArray array = (JSONArray)query.get("wikigrokrandom");
        List<Category> noteArray = new ArrayList<Category>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject rand = array.getJSONObject(i);
            noteArray.add(new Category(rand));
        }
        return noteArray;
    }

}
