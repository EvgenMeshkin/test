package by.evgen.android.apiclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by evgen on 16.01.2015.
 */
public class SearchArrayAdapter extends ArrayAdapter<Category> {

    private TextView mTitle;
    private TextView mContent;
    private ImageLoader imageLoader;
    private Context mContext;

    public SearchArrayAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
        mContext = context;
        imageLoader = ImageLoader.get(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_item, null);
        }
        Category item = getItem(position);
        mTitle = (TextView) convertView.findViewById(android.R.id.text1);
        mTitle.setText(item.getTitle());
        mContent = (TextView) convertView.findViewById(android.R.id.text2);
        mContent.setText(item.getDist());
        final String urlImage = Api.IMAGEVIEW_GET + item.getTitle().replaceAll(" ", "%20");
        convertView.setTag(item.getId());
        final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
        imageView.setImageBitmap(null);
        imageView.setTag(urlImage);
        imageLoader.displayImage(urlImage, imageView);
        return convertView;
    }

}
