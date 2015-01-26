package by.evgen.android.apiclient.processing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.evgen.android.apiclient.bo.Category;

/**
 * Created by evgen on 15.11.2014.
 */
public class CategoryArrayProcessor extends WrapperArrayProcessor<Category> {

    @Override
    protected Category createObject(JSONObject jsonObject) {
        return new Category(jsonObject);
    }

    @Override
    protected JSONArray createArray(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject1 = jsonObject.getJSONObject("query");
        JSONArray array = (JSONArray) jsonObject1.get("geosearch");
        return array;
    }
}
