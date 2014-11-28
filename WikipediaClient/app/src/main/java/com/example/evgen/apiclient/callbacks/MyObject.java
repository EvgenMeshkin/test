package com.example.evgen.apiclient.callbacks;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.evgen.apiclient.bo.NoteGsonModel;

import org.json.JSONObject;

/**
 * Created by evgen on 07.11.2014.
 */
public class MyObject implements Parcelable {

    public NoteGsonModel paramOne;
    public int paramToo;

    public MyObject(NoteGsonModel paramOne, int paramToo) {
        this.paramOne = paramOne;
        this.paramToo = paramToo;
    }

    private MyObject(Parcel parcel) {  // Создание объекта через Parcel
        String string = parcel.readString();
        try {
       //     paramOne = new JSONObject(string);
        } catch (Exception e) {
            e.printStackTrace();
        }


   //     paramOne = parcel.readString();
        paramToo = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {      //Упаковывание объекта в Parcel
        parcel.writeString(paramOne.toString());
        parcel.writeInt(paramToo);
    }

    public static final Parcelable.Creator<MyObject> CREATOR = new Parcelable.Creator<MyObject>() {   // Статический метод с помощью которого создаем обьект
        public MyObject createFromParcel(Parcel in) {
            return new MyObject(in);
        }

        public MyObject[] newArray(int size) {
            return new MyObject[size];
        }
    };
}

