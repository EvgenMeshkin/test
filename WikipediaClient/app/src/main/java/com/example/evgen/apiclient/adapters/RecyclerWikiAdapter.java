package com.example.evgen.apiclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.apiclient.bo.Category;
import com.example.evgen.imageloader.ImageLoader;

import java.util.List;

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
        Log.d(LOG_TAG, "Start recycler");
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Log.d(LOG_TAG, "Bind recycler");
        Category item = records.get(i);
        viewHolder.name.setText(item.getTitle());
        viewHolder.content.setText(item.getDist() + " m.");
        final String urlImage = Api.IMAGEVIEW_GET + item.getTitle().replaceAll(" ","%20");
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
