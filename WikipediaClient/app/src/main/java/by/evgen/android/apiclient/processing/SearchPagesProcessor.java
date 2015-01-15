package by.evgen.android.apiclient.processing;

import by.evgen.android.apiclient.bo.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 18.12.2014.
 */
public class SearchPagesProcessor extends WrapperArrayProcessor<Category>{

    @Override
    protected Category createObject(JSONObject jsonObject) {
        return new Category(jsonObject);
    }

    @Override
    protected JSONArray createArray(JSONObject jsonObject) throws JSONException {
        JSONObject query = jsonObject.getJSONObject("query");
        JSONArray array = (JSONArray)query.get("search");
        return array;
    }

}

