package by.evgen.android.apiclient.processing;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import by.evgen.android.apiclient.bo.Category;

/**
 * Created by User on 19.11.2014.
 */
public class ImageUrlProcessor implements Processor<List<Category>, InputStream> {

    @Override
    public List<Category> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONObject jsonObject = new JSONObject(string);
        JSONObject query = jsonObject.getJSONObject("query");
        JSONObject pages = query.getJSONObject("pages");
        Iterator<?> i = pages.keys();
        JSONObject pagesId = pages.getJSONObject(i.next().toString());
        JSONObject thumbnail = pagesId.getJSONObject("thumbnail");
        List<Category> noteArray = new ArrayList<Category>(thumbnail.length());
        Category category = new Category(thumbnail);
        category.getUrlImage();
        noteArray.add(category);
        return noteArray;
    }

}
