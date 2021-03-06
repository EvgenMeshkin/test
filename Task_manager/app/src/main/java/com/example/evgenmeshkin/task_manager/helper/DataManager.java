package com.example.evgenmeshkin.task_manager.helper;

import android.os.Handler;

import com.example.evgenmeshkin.task_manager.ListviewActivity;
import com.example.evgenmeshkin.task_manager.source.DataSource;

import java.util.List;

/**
 * Created by User on 16.10.2014.
 */
public class DataManager {


    public static interface Callback {
        void onDataLoadStart();
        void onDone(List<String> data);
        void onError(Exception e);
    }

    public static void loadData(final Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        final Handler handler = new Handler();
        callback.onDataLoadStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<String> data = DataSource.getData();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDone(data);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();
    }

    public static void AddData(final Callback callback, final String S) {

      // try {
      //      DataSource.setData(S);
      //  } catch (Exception e) {
      //      e.printStackTrace();
    //    }


        final Handler handler1 = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataSource.setData(S);

                } catch (final Exception e) {
                    handler1.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(e);
                        }
                    });
                }
            }
        }).start();


    }
}
