package by.evgen.android.apiclient.processing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import by.evgen.android.apiclient.bo.Category;

/**
 * Created by evgen on 31.12.2014.
 */
public class ContentsArrayProcessor extends WrapperArrayProcessor<String> {

    @Override
    protected String createObject(JSONObject jsonObject) {
        return new Category(jsonObject).getLine();
    }

    @Override
    protected JSONArray createArray(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObjectquery = jsonObject.getJSONObject("mobileview");
        JSONArray array = (JSONArray) jsonObjectquery.get("sections");
        return array;
    }

}