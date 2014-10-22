package com.example.evgen.apiclient.bo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by User on 22.10.2014.
 */
public class NoteGsonModel implements Parcelable {

    private String title;

    private String content;

    private Long id;

    public NoteGsonModel(Long id, String title, String content) {
        this.title = title;
        this.content = content;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] { id.toString(), title, content });
      //  parcel.writeLong(id);
      //  parcel.writeString(title);
      //  parcel.writeString(content);
    }
   public static final Parcelable.Creator<NoteGsonModel> CREATOR = new Parcelable.Creator<NoteGsonModel>() {
    // распаковываем объект из Parcel
    public NoteGsonModel  createFromParcel(Parcel in) {

        return new NoteGsonModel(in);
    }

    public NoteGsonModel[] newArray(int size) {
        return new NoteGsonModel[size];

         }
};

    // конструктор, считывающий данные из Parcel
    private NoteGsonModel(Parcel parcel) {
     //   Log.d(LOG_TAG, "MyObject(Parcel parcel)");
     //   s = parcel.readString();
     //   i = parcel.readInt();
    //    this.title = parcel.readString();
    //    this.content = parcel.readString();
     //   this.id =  parcel.readLong();
        String[] data = new String[3];
        parcel.readStringArray(data);
      //  id = data[0];
        title = data[1];
        content = data[2];

    }

}