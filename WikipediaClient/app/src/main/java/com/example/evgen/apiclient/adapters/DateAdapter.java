package com.example.evgen.apiclient.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evgen.apiclient.Api;
import com.example.evgen.apiclient.R;
import com.example.evgen.imageloader.ImageLoader;

/**
 * Created by evgen on 11.01.2015.
 */
public class DateAdapter extends SimpleCursorAdapter {
    private Cursor dataCursor;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    final static String LOG_TAG = DateAdapter.class.getSimpleName();
    String prevDate = null;


    public DateAdapter(Context context, int layout, Cursor dataCursor, String[] from,
                         int[] to) {
        super(context, layout, dataCursor, from, to);
        imageLoader = ImageLoader.get(context);
        this.dataCursor = dataCursor;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        super.bindView(view, context, cursor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Log.d(LOG_TAG, "PrevDate = " + position);
        dataCursor.moveToPosition(position);

        if (position > 0 ) {
            dataCursor.moveToPrevious();
            prevDate = (new java.sql.Date(dataCursor.getLong(dataCursor.getColumnIndex("wikidate")))).toString();
            Log.d(LOG_TAG, "PrevDate = " + prevDate);
            dataCursor.moveToNext();
        }
        int title = dataCursor.getColumnIndex("name");
        String task_title = dataCursor.getString(title);
        int title_date = dataCursor.getColumnIndex("wikidate");
        Long task_day = dataCursor.getLong(title_date);
        String dt = (new java.sql.Date(task_day)).toString();

        holder = new ViewHolder();
        if (convertView == null) {
            if (!dt.equals(prevDate)) {
                Log.d(LOG_TAG, "dt = " + dt);

                convertView = mInflater.inflate(R.layout.view_separator, null);
                holder.sec_hr = (TextView) convertView.findViewById(android.R.id.text2);
                holder.sec_hr.setVisibility(View.VISIBLE);
            } else {
                convertView = mInflater.inflate(R.layout.adapter_item, null);
                holder.sec_hr = (TextView) convertView.findViewById(android.R.id.text2);
                holder.sec_hr.setVisibility(View.GONE);
            }
            holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);//Task Title
            holder.img = (ImageView) convertView.findViewById(android.R.id.icon);

            convertView.setTag(holder);

        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.sec_hr.setText(dt.toString());
        final String urlImage = Api.IMAGEVIEW_GET + task_title.replaceAll(" ", "%20");
        holder.text1.setText(task_title);
        imageLoader.displayImage(urlImage, holder.img);
        return convertView;
    }

    static class ViewHolder {
        TextView text1;
        TextView sec_hr;
        ImageView img;
    }
}