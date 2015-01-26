package by.evgen.android.apiclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import by.evgen.android.apiclient.Api;
import by.evgen.android.apiclient.R;
import by.evgen.android.apiclient.bo.Category;
import by.evgen.android.apiclient.utils.Log;
import by.evgen.android.imageloader.ImageLoader;

/**
 * Created by User on 13.01.2015.
 */
public class RecyclerWikiAdapter extends RecyclerView.Adapter<RecyclerWikiAdapter.ViewHolder> {

    private List<Category> records;
    private ImageLoader imageLoader;
    final static String LOG_TAG = RecyclerWikiAdapter.class.getSimpleName();

    public RecyclerWikiAdapter(Context context, List<Category> records) {
        imageLoader = ImageLoader.get(context);
        this.records = records;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.text(this.getClass(), "Start recycler");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.text(this.getClass(), "Bind recycler");
        Category item = records.get(i);
        viewHolder.name.setText(item.getTitle());
        viewHolder.content.setText(item.getDist() + " m.");
        final String urlImage = Api.IMAGEVIEW_GET + item.getTitle().replaceAll(" ", "%20");
        viewHolder.icon.setImageBitmap(null);
        viewHolder.icon.setTag(urlImage);
        imageLoader.displayImage(urlImage, viewHolder.icon);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView content;
        private ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(android.R.id.text1);
            content = (TextView) itemView.findViewById(android.R.id.text2);
            icon = (ImageView) itemView.findViewById(android.R.id.icon);
        }
    }

}
